package ru.emkn.kotlin.sms

fun <T> loadData(
    parameters: Parameters,
    parameterName: String,
    messageForUser: String,
    loadFunction: (String) -> T,
): T {
    if (parameters[parameterName] == null) {
        parameters[parameterName] = getValidFilename(messageForUser)
    }
    val result: T = loadFunction(parameters[parameterName]!!)
    updateParametersFile(parameters)
    return result
}

data class Data(val collectives: List<Collective>, val groups: List<Group>)

fun loadAllInitialData(): Data {
    val parameters = loadParameters()

    val distances = loadData(parameters, "distances",
        "Пожалуйста, введите путь до файла со всеми дистанциями.", ::loadDistances)

    val collectives = loadData(parameters, "collectives",
        "Пожалуйста, введите путь до папки со всеми коллективами.", ::loadCollectives)

    val distancesForGroups = loadData(parameters, "groups",
        "Пожалуйста, введите путь до файла со всеми группами", ::loadDistancesForGroups)
    val groups = initGroups(distancesForGroups, collectives, distances)

    val event = loadData(parameters, "event",
        "Пожалуйста, введите путь до файла с описанием события", ::loadEvent)

    return Data(collectives, groups)
}
