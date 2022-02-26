package ru.emkn.kotlin.sms

//it is impossible to use Kotlin Local Time. It is terrible
class Time(var hours: Int = 0, var minutes: Int = 0, var seconds: Int = 0) {
    fun plus(amountOfSeconds: Int): Time {
        val time = Time(hours, minutes, seconds)
        time.seconds += amountOfSeconds
        time.minutes += time.seconds / 60
        time.hours += time.minutes / 60
        time.minutes %= 60
        time.seconds %= 60
        return time
    }

    override fun toString(): String = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${
        seconds.toString().padStart(2, '0')
    }"


    fun timeToLeader(): String {
        if (hours > 0) return "+$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        return "+${minutes.toString().padStart(1, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    fun timeInSeconds(): Int = 3600 * hours + 60 * minutes + seconds

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Time

        if (hours != other.hours) return false
        if (minutes != other.minutes) return false
        if (seconds != other.seconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hours
        result = 31 * result + minutes
        result = 31 * result + seconds
        return result
    }

    operator fun compareTo(with: Time): Int = this.timeInSeconds() - with.timeInSeconds()
}


val StartTime = Time(12, 0, 0)
val BigTime = Time(1000)

fun difference(start: Time, stop: Time): Time {
    return Time(0).plus(start.timeInSeconds() - stop.timeInSeconds())
}

fun String.toTime(): Time =
    Time(this.substring(0, 2).toInt(), this.substring(3, 5).toInt(), this.substring(6, 8).toInt())
