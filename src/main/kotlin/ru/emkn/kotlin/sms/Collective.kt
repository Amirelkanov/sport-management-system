package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess


class Collective(val name: String, val members: List<Participant>)

fun checkCollectivesData(fileName: String) {
    check(File(fileName).isFile && File(fileName).exists()) { throw FileNotFoundException("Файл не найден") }
    val file = csvReader().readAll(File(fileName))
    val headers = listOf("Группа", "Фамилия", "Имя", "Г.р.", "Разр.")
    check(file.first().size == 5 && file.first().first().isNotEmpty() && file.first()[1].isEmpty()
            && file[1] == headers && file.drop(2).all { row -> row.dropLastWhile { it.isEmpty() }.size in 4..5 }
    ) { throw WrongFileFormatException(fileName, "") }
}

fun getCollective(fileName: String): Collective {
    checkCollectivesData(fileName)
    val file = csvReader().readAll(File(fileName))
    val nameOfCollective: String = file.first().first()
    val listOfMembers = file.drop(2).map {
        Participant(
            collective = nameOfCollective,
            wantedGroup = it[0],
            surname = it[1],
            name = it[2],
            birthYear = it[3].toInt(),
            sportRank = if (it.size == 5) it[4] else null
        )
    }
    return Collective(nameOfCollective, listOfMembers)
}

fun loadCollectives(directoryName: String): List<Collective> {
    logger.info { "Получаем коллективы с $directoryName..." }
    val result = mutableListOf<Collective>()
    File(directoryName).walk().forEach { f ->
        if (f.isFile) {
            result.add(getCollective(f.absolutePath))
        }
    }
    logger.info { "Готово!" }
    return result
}

fun getCollectivePoints(collectives: List<Collective>, groups: List<Group>): List<List<String>> {

    val result = mutableListOf(listOf("Название", "Очки"))
    collectives.forEach { it ->
        result.add(listOf(it.name,
            groups.sumOf { g ->
                g.members.filter { m -> m.collective == it.name }
                    .sumOf { member -> member.scoreFromCompetition(groups.find { it.name == member.wantedGroup }!!) }
            }
                .toString()))
    }
    return result

}


fun writeCollectiveProtocolsToCSV(collectives: List<Collective>, groups: List<Group>) {
    println("Укажите путь до файла, куда Вы хотите сохранить протокол для всех коллективов.")
    logger.info { "Записываем данные протокола для коллективов в .csv - файл..." }
    val filePath = readLine() ?: exitProcess(0)
    val result = mutableListOf(listOf("Протокол результатов коллективов."))
    result.add(listOf("№ п/п",
        "Номер",
        "Фамилия",
        "Имя",
        "Г.р.",
        "Разр.",
        "Результат"))
    collectives.forEach {
        val rows = mutableListOf(listOf("Имя коллектива", "Сумма очков"))
        rows.addAll(it.getFinishProtocolForCollective(groups))
        result.addAll(rows)
    }
    csvWriter().writeAll(result, File(filePath))
    logger.info { "Готово!" }
    println("Данные сохранены!")
}