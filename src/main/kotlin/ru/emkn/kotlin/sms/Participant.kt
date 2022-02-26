package ru.emkn.kotlin.sms

import java.util.*
import kotlin.Int
import kotlin.String


enum class ParticipantStatus { REGISTERED, FINISHED, DISQUALIFIED }

class Participant(
    val collective: String, val wantedGroup: String, val surname: String, val name: String,
    val birthYear: Int, val sportRank: String? = null,
) {

    // КП: время, затраченное на его прохождение
    // Заполняется во время считывания splits.csv
    val memberPoints = mutableMapOf<ControlPoint, Time?>()

    var personalNumber: Int = 0

    var startTime: Time = StartTime

    val age: Int
        get() = Calendar.getInstance().get(Calendar.YEAR) - birthYear

    var status = ParticipantStatus.REGISTERED

    fun resultTime(group: Group) = group.distance.getResultTime(this)


    fun getPartitionInfoForFinish(number: Int, group: Group, bestTime: Time): List<String> {
        return listOf(
            group.name,
            number.toString(), personalNumber.toString(), surname, name, birthYear.toString(),
            if (sportRank.isNullOrEmpty()) "" else sportRank,
            collective,
            if (status == ParticipantStatus.DISQUALIFIED) "снят" else resultTime(group).toString(),
            number.toString(),
            if (status == ParticipantStatus.DISQUALIFIED) "" else {
                difference(resultTime(group), bestTime).timeToLeader()
            }
        )
    }

    fun getPartitionInfoForFinish(number: Int, group: Group): List<String> {
        return listOf(
            number.toString(), personalNumber.toString(), surname, name, birthYear.toString(),
            if (sportRank.isNullOrEmpty()) "" else sportRank,
            if (status == ParticipantStatus.DISQUALIFIED) "снят" else scoreFromCompetition(group).toString()
        )
    }

    fun scoreFromCompetition(group: Group): Int {
        if (status == ParticipantStatus.DISQUALIFIED) return 0
        return maxOf(
            0f,
            100f * (2 - resultTime(group).timeInSeconds()
                .toFloat() / ((group.bestResult.timeInSeconds())))
        ).toInt()
    }

    override fun toString(): String {
        return "Participant(collective='$collective', wantedGroup='$wantedGroup', surname='$surname', name='$name', birthYear=$birthYear, sportRank=$sportRank, memberPoints=$memberPoints, personalNumber=$personalNumber, startTime=$startTime, status=$status)"
    }
}


fun getParticipantFromNumber(groups: List<Group>, number: Int) =
    groups.flatMap { group -> group.members }.find { member ->
        member.personalNumber == number
    } ?: throw MemberNotFoundException()
