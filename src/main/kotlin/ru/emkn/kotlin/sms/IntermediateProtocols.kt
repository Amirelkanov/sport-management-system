package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

fun getIntermediateProtocols(groups: List<Group>) {
    fun chekArgs(string: String?): List<String> {
        if (string == null) return listOf()
        if (string.isEmpty()) return listOf("q")
        if (string.split("\\s+".toRegex()).size == 2 && string.split("\\s+".toRegex())[0] == "1") {
            return string.split("\\s+".toRegex())
        }
        if (string.split("\\s+".toRegex()).size == 3 && string.split("\\s+".toRegex())[0] == "2")
            return string.split("\\s+".toRegex())
        return listOf()
    }
    println("Вы можете получить промежуточный протокол для любого участника или любого контрольного пункта для группы.")
    println("Вводите в формате:")
    println("'1 <participant number>' или '2 <point name> <group name>'.")
    println("Чтобы закончить, введите quit.")
    while (true) {
        val data = chekArgs(readLine())
        when (data.firstOrNull()) {
            null -> println("Неправильный формат ввода. Повторите попытку")
            "1" -> getParticipantFromNumber(groups, data[1].toInt()).writeIntermediateProtocolForParticipantToCSV()
            "2" -> groups.find { it.name == data[2] }?.writeIntermediateProtocolForPointToCSV(data[1])
                ?: println("Название группы или дистанции не верно")
            "quit" -> break
        }
    }
}

fun initMemberFromPoints(fileName: String, groups: List<Group>) {
    logger.info { "Инициализация структуры типа <Участник> - <Контрольные пункты>..." }
    fileCheck(fileName)
    csvReader().readAll(File(fileName)).forEach { row ->
        val member = getParticipantFromNumber(groups, row.first().toInt())
        val (points, time) = row.filter { it.isNotEmpty() }.drop(1).partition { elem -> ":" !in elem }
        points.mapIndexed { idx, key -> member.memberPoints[ControlPoint(key)] = time[idx].toTime() }
        member.status = groups.find { it.name == member.wantedGroup }?.distance?.checkParticipantsStatus(member)
            ?: ParticipantStatus.DISQUALIFIED
    }
    println("Данные успешно обработаны!")
    logger.info { "Готово!" }

}

fun initMembersFromPoint(fileName: String, groups: List<Group>) {
    logger.info { "Инициализация структуры типа <Контрольный пункт> - <Участники>..." }

    fileCheck(fileName)
    csvReader().readAll(File(fileName)).forEach { row ->
        val point = row.first()
        val (memberNumbers, time) = row.filter { it.isNotEmpty() }.drop(1).partition { elem -> ":" !in elem }

        memberNumbers.mapIndexed { idx, memberNumber ->
            val member = getParticipantFromNumber(groups, memberNumber.toInt())
            member.memberPoints[ControlPoint(point)] = time[idx].toTime()
            member.status = groups.find { it.name == member.wantedGroup }?.distance?.checkParticipantsStatus(member)
                ?: ParticipantStatus.DISQUALIFIED
        }
    }
    println("Данные успешно обработаны!")
    logger.info { "Готово!" }

}

fun initMembersPointsUI(groups: List<Group>) {
    println("В каком формате принимать файл:\n" +
            "<Спортсмен> - <Контрольные пункты> или <Контрольный пункт> - <Спортсмены>? (1/2)")
    val wayToInput = readLine()
    print("Введите имя файла: ")
    val fileName = readLine()

    if (fileName != null) {
        fileCheck(fileName)
        parseWaysToInput(wayToInput, fileName, groups)
    } else println("Неправильно введены данные. Попробуйте еще раз.") // Подумать насчет while'a
}

fun parseWaysToInput(wayToInput: String?, fileName: String, groups: List<Group>) {
    when (wayToInput) {
        "1" -> initMemberFromPoints(fileName, groups)
        "2" -> initMembersFromPoint(fileName, groups)
        else -> println("Неправильно введены данные")
    }
}

fun Group.getIntermediateProtocolForPoint(point: String): List<List<String>> {
    logger.info { "Получаем промежуточный протокол для структуры <Контрольный пункт> - <Участники>..." }
    val result = mutableListOf<List<String>>()
    result.add(listOf("Участник", "Время"))
    result.addAll(members.filter { it.status != ParticipantStatus.DISQUALIFIED }
        .map { listOf(it.personalNumber.toString(), it.memberPoints[ControlPoint(point)].toString()) })
    logger.info { "Готово!" }
    return result
}

fun Group.writeIntermediateProtocolForPointToCSV(point: String) {
    logger.info { "Записываем промежуточный протокол для структуры <Контрольный пункт> - <Участники> в .csv - файл..." }

    println("Укажите путь до файла, куда Вы хотите сохранить промежуточный протокол контрольной точки $point для группы $name.")
    val filePath = readLine()
    if (filePath != null)
        csvWriter().writeAll(getIntermediateProtocolForPoint(point), File(filePath))
    logger.info { "Готово!" }
}

fun Participant.getIntermediateProtocolForParticipant(): List<List<String>> {
    logger.info { "Получаем промежуточный протокол для структуры <Участник> - <Контрольные пункты>..." }

    val result = mutableListOf<List<String>>()
    result.add(listOf("Контрольная точка", "Время"))
    result.addAll(memberPoints.keys.map { key -> listOf(key.name, memberPoints[key].toString()) })
    logger.info { "Готово!" }
    return result
}


fun Participant.writeIntermediateProtocolForParticipantToCSV() {
    logger.info { "Записываем промежуточный протокол для структуры <Участник> - <Контрольные пункты> в .csv-файл..." }

    println("Укажите путь до файла, куда Вы хотите сохранить промежуточный протокол участника $name.")
    val filePath = readLine()
    if (filePath != null)
        csvWriter().writeAll(getIntermediateProtocolForParticipant(), File(filePath))
    logger.info { "Готово!" }
}
