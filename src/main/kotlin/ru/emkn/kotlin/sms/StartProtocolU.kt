package ru.emkn.kotlin.sms

fun Group.getStartProtocol(): List<List<String>> {

    val csvFormatList = mutableListOf<List<String>>()
    csvFormatList.addAll(
        members.map {
            listOf(
                it.wantedGroup,
                it.personalNumber.toString(),
                it.surname,
                it.name,
                it.birthYear.toString(),
                if (it.sportRank.isNullOrEmpty()) "" else it.sportRank,
                it.startTime.toString())
        }
    )
    return csvFormatList.toList()
}

fun generateAllStartProtocols(groups: List<Group>): List<List<String>> {
    logger.info { "Генерируем все стартовые протоколы..." }
    val result = mutableListOf(listOf("Группа", "Номер", "Фамилия", "Имя", "Г.р", "Разряд", "Старт"))
    result.addAll(groups.flatMap { it.getStartProtocol() })
    logger.info { "Готово!" }
    return result
}
