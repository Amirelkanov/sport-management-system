import ru.emkn.kotlin.sms.*
import kotlin.test.Test
import kotlin.test.assertEquals


internal class GetStartProtocolTests {
    @Test
    fun getStartProtocolTest() {
        val participant1 = Participant("Collective1", "Group1",
            "Testov", "TestWithoutSportRank", 1999)
        participant1.personalNumber = 1
        participant1.startTime = StartTime.plus(10)

        val participant2 = Participant("Collective2", "Group1",
            "Elkanov", "Amir", 2004, "ipsc")
        participant2.personalNumber = 2
        participant2.startTime = StartTime.plus(20)

        val group = Group("Group1",
            listOf(participant1, participant2),
            DistancesWithDuplicates("Distance1", listOf("1", "2", "3").map { ControlPoint(it) })
        )

        val result = listOf(
            listOf(group.name,"1", "Testov", "TestWithoutSportRank", "1999", "", "12:00:10"),
            listOf(group.name,"2", "Elkanov", "Amir", "2004", "ipsc", "12:00:20")
        )

        assertEquals(result, group.getStartProtocol())
    }
}