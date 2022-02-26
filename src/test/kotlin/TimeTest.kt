import ru.emkn.kotlin.sms.*
import kotlin.test.*


internal class TimeTest  {
    @Test
    fun timeMethodsTest(){
        val time1 = Time(12,0,0)
        assertEquals(time1.plus(3660),Time(13,1,0))
        assertEquals(time1.timeToLeader(),"+12:00:00")
        assertEquals(time1,"12:00:00".toTime())
        assertEquals(time1.toString(),"12:00:00")
        assertEquals(difference(Time(13,15,45),time1),Time(1,15,45))
    }
}