package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import kotlin.random.Random


class Group(val name: String, var members: List<Participant>, var distance: Distance) {

    var bestResult = BigTime

    // Сразу для всех участников определяем номер и дату старта
    fun setStartTimes() =
        members.forEachIndexed { index, participant -> participant.startTime = StartTime.plus(60 * index) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (name != other.name) return false
        if (members != other.members) return false
        if (distance != other.distance) return false
        if (bestResult != other.bestResult) return false

        return true
    }


    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + distance.hashCode()
        result = 31 * result + bestResult.hashCode()
        return result
    }

}

fun writeGroupProtocolsToCSV(groups: List<Group>) :List<List<String>> {
    logger.info { "Записываем данные протокола для групп в .csv - файл..." }

    val rows = mutableListOf<List<String>>(listOf("Группа","№ п/п",
        "Номер",
        "Фамилия",
        "Имя",
        "Г.р.",
        "Разр.",
        "Команда",
        "Результат",
        "Место",
        "Отставание"))

    groups.forEach { rows.addAll(it.getFinishProtocolForGroup()) }
    return rows
}

fun checkFileDistancesForGroups(file: File) {
    val text = csvReader().readAll(file)
    check(text.isNotEmpty()) { throw WrongFileFormatException(file.name, "файл пустой") }
    check(text.all { it.size == 2 } && text[0][0].lowercase() == "название" && text[0][1].lowercase() == "дистанция") {
        throw WrongFileFormatException(file.name, "должно быть два столбца: \"Название\" и \"Дистанция\"")
    }
}

// group name - distance name
fun loadDistancesForGroups(filename: String): Map<String, String> {
    logger.info { "Получаем дистанции для групп..." }
    try {
        check(File(filename).exists()) { throw NoFileException("файла $filename не существует.") }
        val distancesForGroups = File(filename)
        checkFileDistancesForGroups(distancesForGroups)
    } catch (error: Exception) {
        logger.error(error) { error.message }
    }

    val result = mutableMapOf<String, String>()
    val text = csvReader().readAll(File(filename))
    for (line in text.subList(1, text.size)) {
        result[line[0]] = line[1]
    }
    logger.info { "Готово!" }
    return result
}

fun initGroups(
    distancesForGroups: Map<String, String>,
    collectives: List<Collective>,
    distances: List<Distance>,
): List<Group> {
    logger.info { "Инициализация групп..." }

    val groupsName = distancesForGroups.map { it.key }

    val groupsMembers = mutableMapOf<String, MutableList<Participant>>()

    for (collective in collectives) {
        for (member in collective.members) {
            if (!groupsName.contains(member.wantedGroup)) {
                throw WrongFileFormatException("описаний групп", "желаемая группа \"${member.wantedGroup}\"" +
                        " участника \n${member.surname} ${member.name}\n не существует")
            }
            if (!groupsMembers.containsKey(member.wantedGroup)) {
                groupsMembers[member.wantedGroup] = mutableListOf()
            }
            groupsMembers[member.wantedGroup]!!.add(member)
        }
    }

    val groups = mutableListOf<Group>()
    for (groupName in groupsName) {
        if (groupsMembers.containsKey(groupName)) {
            val distance = distances.find { it.name == distancesForGroups[groupName] } // ?:
            if (distance == null) {
                println("Дистанции ${distancesForGroups[groupName]} для группы $groupName не существует")
            } else {
                groups.add(Group(groupName, groupsMembers[groupName]!!, distance))
            }
        }
    }

    val groupsWithoutMembers = groupsName.filter { !groupsMembers.containsKey(it) }
    if (groupsWithoutMembers.isNotEmpty()) {
        println("Следующие группы не имеют участников: ")
        println(groupsWithoutMembers.joinToString(", "))
    }
    logger.info { "Готово!" }
    return groups
}

fun draw(groups: List<Group>) {
    logger.info { "Проходит жеребьевка..." }
    val rand = Random(228)
    val randomPermutation = (1..groups.sumOf { it.members.size }).toList().shuffled(rand)
    groups.flatMap { it.members }
        .forEachIndexed { index, participant -> participant.personalNumber = randomPermutation[index] }
    groups.forEach { it.setStartTimes() }
    logger.info { "Жеребьевка прошла успешно!" }
}
