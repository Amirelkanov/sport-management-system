package ru.emkn.kotlin.sms

import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList
import kotlin.system.exitProcess

fun getValidFilename(message: String): String {
    println(message)
    while (true) {
        val path = readLine() ?: throw NoInputException()
        if (path == "quit") {
            exitProcess(0)
        }
        if (File(path).exists()) {
            return path
        }
        println("Данный путь некорректен или такого файла не существует.")
    }
}

fun yesNo(message: String): Boolean {
    print("$message (да/нет): ")
    while (true) {
        when (readLine() ?: exitProcess(0)) {
            "да" -> return true
            "нет" -> return false
            "quit" -> exitProcess(0)
            else -> println("(да/нет/quit)")
        }
    }
}

fun fileCheck(fileName: String) =
    check(File(fileName).isFile && File(fileName).exists()) { throw FileNotFoundException("Файл не найден") }

fun getAllFilesInResources(path: String): List<String> {
    val resourcesPath = Paths.get(path)
    return Files.walk(resourcesPath)
        .filter { item -> Files.isRegularFile(item) }
        .filter { item -> item.toString().endsWith(".csv") }.map { it.fileName.toString().replace(".csv", "") }.toList()
}
