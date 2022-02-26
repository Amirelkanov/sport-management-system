package ru.emkn.kotlin.sms

import androidx.compose.foundation.ExperimentalFoundationApi
import mu.KotlinLogging
import ru.emkn.kotlin.sms.GUI.gui
import kotlin.system.exitProcess


val logger = KotlinLogging.logger {}

@OptIn(ExperimentalFoundationApi::class)
fun main() {
    logger.info { "Start program" }
    if (yesNo("Запустить программу в GUI-режиме?")) {
        logger.info { "Chose GUI format" }
        gui()
    } else {
        logger.info { "Chose console format" }
        try {
            if (yesNo("Сгенерировать данные?")) {
                generateAllData()
            }
            val (collectives, groups) = loadAllInitialData()
            draw(groups)
            initMembersPointsUI(groups)
            generateAllStartProtocols(groups)
            writeGroupProtocolsToCSV(groups)
            writeCollectiveProtocolsToCSV(collectives, groups)
            logger.info { "Finish program" }
        } catch (error: Exception) {
            logger.error { error.message }
            exitProcess(-1)
        }
    }
}
