package ru.emkn.kotlin.sms

import java.io.File

const val PATH_TO_PARAMETERS = "parameters.param"

/*
    key - string without whitespaces
*/

typealias Parameters = MutableMap<String, String>

fun checkDatabaseFile() {
    if (!File(PATH_TO_PARAMETERS).exists()) {
        File(PATH_TO_PARAMETERS).writeText("distances \ngroups \nevent \ncollectives ")
    }
}

fun loadParameters(): Parameters {
    logger.info { "Загрузка параметров..." }
    val parameters = mutableMapOf<String, String>()
    checkDatabaseFile()
    val lines = File(PATH_TO_PARAMETERS).readLines().filter { it.isNotEmpty() }
    for (line in lines) {
        val str = line.split(' ')
        check(str.size == 2) {
            File(PATH_TO_PARAMETERS).delete()
            throw InvalidParameterFileException(line)
        }
        parameters[str[0]] = str.subList(1, str.size).reduce { sum, element -> sum + element }
    }
    logger.info { "Параметры загружены!" }
    return parameters
}

fun updateParametersFile(parameters: Parameters) {
    checkDatabaseFile()
    val parametersBuilder = StringBuilder()
    parameters.map { parameter -> parametersBuilder.appendLine("${parameter.key} ${parameter.value}") }
    File(PATH_TO_PARAMETERS).writeText(parametersBuilder.toString())
    logger.info { "Параметры обновлены!" }
}
