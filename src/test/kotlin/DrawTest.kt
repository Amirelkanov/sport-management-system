import ru.emkn.kotlin.sms.*
import java.io.File
import kotlin.random.Random
import kotlin.test.*


internal class DrawTest {
    @Test
    fun testInitDistances(){
        val distancesFromFile1=loadDistances("test-data/testInitDistances/Test1.csv")
        assertEquals(distancesFromFile1.last().name,"Ж16  студенты")
        assertEquals(distancesFromFile1[0].points, listOf("31", "32", "33", "34", "35","36","37","38","39", "40", "41", "39", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "49", "52", "53").map { ControlPoint(it) })
        assertEquals(distancesFromFile1[1].points, listOf("32","46","34","33","53").map { ControlPoint(it) })  //проверяем, что не осталось "пустых" КП (в данной таблице их могло быть до 25).
    }

    // TODO
/*
    @Test
    fun testDraw(){
        val participantsInGroup1= mutableListOf<Participant>()
        for(i in 1..15) participantsInGroup1.add(Participant("collective $i","group1",i.toString(),"",1970))
        val group1 = Group("group1",participantsInGroup1, Distance("1", listOf("1","2","3")))
        val groups = listOf(group1)
        draw(groups)
        assertEquals(participantsInGroup1.map {it.surname.toInt()}.toSet(),(1..15).toSet()) // проверяем, что все номера с 1 по 15 есть по разу
        assertNotNull(group1.members.find { it.startTime == Time(12,5,0) })
    }
*/

}