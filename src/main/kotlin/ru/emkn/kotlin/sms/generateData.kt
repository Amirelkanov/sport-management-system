package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import kotlin.random.Random

fun randomString(): String {
    val len = (6..10).random()
    var result = ""
    repeat(len) {
        result += ('a'.code + (0..25).random()).toChar()
    }
    return result
}

fun fillUp(a: MutableList<MutableList<String>>) {
    val len = a.maxOf { it.size }
    a.forEach {
        while (it.size < len) {
            it.add("")
        }
    }
}

fun generateListString(): List<String> {
    val len = (6..10).random()
    val result = mutableListOf<String>()
    while (result.size < len) {
        val s = randomString()
        if (result.find { it == s } == null) {
            result.add(s)
        }
    }
    return result
}

fun generateParticipant(collectivesNames: List<String>, groupsNames: List<String>): Participant {
    return Participant(collectivesNames.random(), groupsNames.random(), randomString(),
        randomString(), 1980 + (0..40).random(), if ((0..10).random() < 7) randomString() else null)
}

fun generateDistances(): List<Distance> {
    val names = generateListString()
    val result = mutableListOf<Distance>()
    for (name in names) {
        result.add(DistancesWithDuplicates(name, generateListString().map { ControlPoint(it) }))
    }
    return result
}

fun writeCollective(collective: Collective, filename: String) {
    val text = MutableList(2 + collective.members.size) { MutableList(5) { "" } }
    text[0][0] = collective.name
    // Группа,Фамилия,Имя,Г.р.,Разр.
    text[1][0] = "Группа"
    text[1][1] = "Фамилия"
    text[1][2] = "Имя"
    text[1][3] = "Г.р."
    text[1][4] = "Разр."
    for (i in collective.members.indices) {
        text[2 + i] = mutableListOf(collective.members[i].wantedGroup,
            collective.members[i].surname,
            collective.members[i].name,
            collective.members[i].birthYear.toString(),
            collective.members[i].sportRank ?: "")
    }
    fillUp(text)
    csvWriter().writeAll(text, File("$filename.csv"))
}

fun writeCollectives(gCollectives: List<Collective>) {
    File("generated-data/applications").mkdir()
    for (collective in gCollectives) {
        writeCollective(collective, "generated-data/applications/${collective.name}")
    }
}

fun writeEvent() {
    File("generated-data/event.csv").createNewFile()
    File("generated-data/event.csv").appendText("Название,Дата\n" +
            "Первенство пятой бани,01.01.2022")
}

fun writeGroups(gGroups: List<Group>) {
    File("generated-data/classes.csv").createNewFile()
    val text = MutableList(1 + gGroups.size) { MutableList(2) { "" } }
    text[0] = mutableListOf("Название", "Дистанция")
    for (i in gGroups.indices) {
        text[1 + i] = mutableListOf(gGroups[i].name, gGroups[i].distance.name)
    }

    fillUp(text)
    csvWriter().writeAll(text, File("generated-data/classes.csv"))
}

fun writeDistances(gDistances: List<Distance>) {
    File("generated-data/courses.csv").createNewFile()
    val text = MutableList(1 + gDistances.size)
    { MutableList(2 + gDistances.maxOf { it.points.size }) { "" } }
    text[0][0] = "Название"
    text[0][1] = "Тип дистанции"
    for (i in 2 until text[0].size) {
        text[0][i] = i.toString()
    }
    for (i in gDistances.indices) {
        text[1 + i][0] = gDistances[i].name
        text[1 + i][1] = "1"
        for (j in gDistances[i].points.indices) {
            text[1 + i][2 + j] = gDistances[i].points[j].name
        }
    }

    fillUp(text)
    csvWriter().writeAll(text, File("generated-data/courses.csv"))
}

fun generateTime(participant: Participant, points: List<ControlPoint>) {
    var startTime = participant.startTime
    for (point in points) {
        val add = (1..120).random()
        startTime = startTime.plus(add)
        participant.memberPoints[point] = startTime
    }
}

fun generateAllData() {
    logger.info { "Генерируем все данные..." }
    val rand = Random(228)

    val collectivesNames = generateListString()
    val groupsNames = generateListString()
    val participants = mutableListOf<Participant>()

    repeat((30..100).random()) {
        participants.add(generateParticipant(collectivesNames, groupsNames))
    }

    val gCollectives = mutableListOf<Collective>()
    for (collectiveName in collectivesNames) {
        gCollectives.add(Collective(collectiveName, participants.filter { it.collective == collectiveName }))
    }

    val gDistances = generateDistances()

    val gGroups = mutableListOf<Group>()
    for (name in groupsNames) {
        gGroups.add(Group(name, participants.filter { it.wantedGroup == name }, gDistances.random()))
    }

    if (File("generated-data/").exists()) {
        File("generated-data/").deleteRecursively()
    }
    File("generated-data/").mkdir()
    writeCollectives(gCollectives)
    writeGroups(gGroups)
    writeEvent()
    writeDistances(gDistances)

    val randomPermutation = (1..gGroups.sumOf { it.members.size }).toList().shuffled(rand)
    gGroups.flatMap { it.members }
        .forEachIndexed { index, participant -> participant.personalNumber = randomPermutation[index] }
    gGroups.forEach { it.setStartTimes() }

    File("generated-data/start-protocol").mkdir()
    for (group in gGroups) {
        File("generated-data/start-protocol/${group.name}.csv").createNewFile()
        csvWriter().writeAll(group.getStartProtocol(), File("generated-data/start-protocol/${group.name}.csv"))
    }

    File("generated-data/splits.csv").createNewFile()
    val text = mutableListOf<MutableList<String>>()
    for (group in gGroups) {
        for (member in group.members) {
            generateTime(member, group.distance.points)
            val times = mutableListOf<String>()
            times.add(member.personalNumber.toString())
            for (point in group.distance.points) {
                times.add(point.name)
                times.add(member.memberPoints[point].toString())
            }
            text.add(times)
        }
    }

    fillUp(text)
    csvWriter().writeAll(text, File("generated-data/splits.csv"))
    text.clear()
    for (distance in gDistances) {
        for (point in distance.points) {
            val times = mutableListOf<String>()
            times.add(point.name)
            for (group in gGroups) {
                if (group.distance == distance) {
                    for (member in group.members) {
                        times.add(member.personalNumber.toString())
                        times.add(member.memberPoints[point].toString())
                    }
                }
            }
            text.add(times)
        }
    }

    fillUp(text)
    csvWriter().writeAll(text, File("generated-data/splitsPoint.csv"))


    val parameters = loadParameters()
    parameters["distances"] = "generated-data/courses.csv"
    parameters["collectives"] = "generated-data/applications"
    parameters["event"] = "generated-data/event.csv"
    parameters["groups"] = "generated-data/classes.csv"
    updateParametersFile(parameters, )

    logger.info { "Готово!" }
}
