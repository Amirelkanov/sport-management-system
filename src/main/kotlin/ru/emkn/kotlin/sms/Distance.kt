package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

interface Distance {
    val name: String
    val points: List<ControlPoint>
    fun checkParticipantsStatus(participant: Participant): ParticipantStatus
    fun getDefaultMemberPoints(participant: Participant): Map<ControlPoint, Time?>
    fun getResultTime(participant: Participant): Time
}

class DistancesWithChoose(override val name: String, private val amount: Int, override val points: List<ControlPoint>) :
    Distance {

    override fun checkParticipantsStatus(participant: Participant): ParticipantStatus {
        return when {
            participant.memberPoints.values.filterNotNull().size < amount -> ParticipantStatus.DISQUALIFIED //пройдено недостаточно КП
            participant.memberPoints.any { it.value != null && it.value!! < participant.startTime } -> ParticipantStatus.DISQUALIFIED // фальшь старт
            else -> ParticipantStatus.FINISHED
        }
    }

    override fun getDefaultMemberPoints(participant: Participant): Map<ControlPoint, Time?> =
        points.associate { point -> (point to null) }

    override fun getResultTime(participant: Participant): Time {
        if (participant.status == ParticipantStatus.DISQUALIFIED) return BigTime
        val lastPointTime = participant.memberPoints.values.filterNotNull().sortedBy { it.timeInSeconds() }[amount - 1]
        return difference(lastPointTime, participant.startTime)
    }

}

class DistancesWithDuplicates(override val name: String, override val points: List<ControlPoint>) : Distance {


    override fun checkParticipantsStatus(participant: Participant): ParticipantStatus {
        return when {
            participant.memberPoints.values.contains(null) -> ParticipantStatus.DISQUALIFIED //Не все точки пройдены.
            participant.memberPoints.keys.sortedBy {
                participant.memberPoints[it]?.timeInSeconds() ?: BigTime.timeInSeconds()
            }.toList() != points -> ParticipantStatus.DISQUALIFIED // прохождение точек в неправильном порядке
            participant.memberPoints.any {
                it.value != null && ((it.value ?: BigTime) < participant.startTime)
            } -> ParticipantStatus.DISQUALIFIED // фальшь старт
            else -> ParticipantStatus.FINISHED
        }
    }

    override fun getDefaultMemberPoints(participant: Participant): Map<ControlPoint, Time?> =
        points.associate { point -> (point to null) }

    override fun getResultTime(participant: Participant): Time {
        return if (participant.status == ParticipantStatus.DISQUALIFIED) BigTime
        else difference(
            participant.memberPoints.values.maxByOrNull { it?.timeInSeconds() ?: BigTime.timeInSeconds() } ?: BigTime,
            participant.startTime)
    }

}

fun saveDistancesWithDuplicatesFromCsv(row: List<String>): Distance = DistancesWithDuplicates(row.first(),
    row.drop(2).dropLastWhile { last -> last.isEmpty() }.map { pointName -> ControlPoint(pointName) })

fun saveDistancesWithChooseFromCsv(row: List<String>): Distance =
    DistancesWithChoose(row.first(), row.component3().toInt(),
        changedPointsToUnique(row.drop(3).dropLastWhile { last -> last.isEmpty() }
            .map { pointName -> ControlPoint(pointName) }))


//Меняет точки в списке на уникальные
fun changedPointsToUnique(points: List<ControlPoint>): List<ControlPoint> {
    val mapOfTimesOfAppearances = mutableMapOf<String, Int>()
    return points.map {
        mapOfTimesOfAppearances[it.name] = mapOfTimesOfAppearances[it.name]?.plus(1) ?: 0
        ControlPoint(it.name, mapOfTimesOfAppearances[it.name] ?: 1)
    }
}

fun loadDistances(filename: String): List<Distance> {
    val file = File(filename) // Возможно, проверка нужна
    val result = mutableListOf<Distance>()

    csvReader().readAll(file).drop(1).forEach {
        when (it.component2()) {
            "1" -> result.add(saveDistancesWithDuplicatesFromCsv(it)) //вид файла: имя дистанции, тип дистанции, 1,2...
            "2" -> result.add(saveDistancesWithChooseFromCsv(it))//вид файла: имя дистанции, тип дистанции, количество точек, 1,2...
            else -> throw Exception("Неправильный тип дистанции") //  TODO()
        }
    }

    return result

}
