package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import kotlin.system.exitProcess

data class Event(val name: String, val date: String)


fun loadEvent(filename: String): Event {
    logger.info { "Инициализация соревнования..." }
    val text = csvReader().readAll(File(filename))
    try {
        check(text.size == 2 && text.all { it.size == 2 }
                && text[0][0].lowercase() == "название" && text[0][1].lowercase() == "дата") {
            throw WrongFileFormatException(filename, "должно быть два столбца и две строки - название и время")
        }
    } catch (error: WrongFileFormatException) {
        logger.error(error) { error.message }
        exitProcess(-1)
    }
    logger.info { "Инициализация соревнования завершена!" }
    return Event(text[1][0], text[1][1])
}
