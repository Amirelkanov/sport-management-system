package ru.emkn.kotlin.sms

fun Group.getFinishProtocolForGroup(): List<List<String>> {
    logger.info { "Получаем финишный протокол для группы..." }
    members = members.sortedWith(compareBy({ it.status == ParticipantStatus.FINISHED }, {
        it.resultTime(this).timeInSeconds()
    }))

    bestResult = members.firstOrNull()?.resultTime(this) ?: BigTime

    val result = mutableListOf<List<String>>()

    result.addAll(members.mapIndexed { index, member -> member.getPartitionInfoForFinish(index + 1, this, bestResult) })
    logger.info { "Готово!" }
    return result
}

fun Collective.getFinishProtocolForCollective(groups: List<Group>): List<List<String>> {
    logger.info { "Получаем финишный протокол для коллектива..." }
    val result = mutableListOf<List<String>>()
    result.add(
        listOf(name,
            members.sumOf { member -> member.scoreFromCompetition(groups.find { it.name == member.wantedGroup }!!) }
                .toString())
    ) //В отличие от группы тут будет ещё и сумма очков
    result.addAll(members.sortedBy { member ->
        -member.scoreFromCompetition(groups.find { it.name == member.wantedGroup }!!)
    }.mapIndexed
    { index, member -> member.getPartitionInfoForFinish(index + 1, groups.find { it.name == member.wantedGroup }!!) })
    logger.info { "Готово!" }
    return result
}
