package ru.emkn.kotlin.sms

class NoInputException : Exception("Нет ввода пользователя.")

class NoFileException(filename: String) : Exception("Файла $filename не существует")

class InvalidParameterFileException(line: String) : Exception("Данные в параметрах повреждены: $line")

class WrongFileFormatException(filename: String, message: String) :
    Exception("Неверный формат в файле $filename: $message.")

class MemberNotFoundException : Exception("Участник не найден")
