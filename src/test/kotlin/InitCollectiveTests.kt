import ru.emkn.kotlin.sms.getCollective
import kotlin.test.Test
import kotlin.test.assertEquals


internal class InitCollectivesTests {
    @Test
    fun initCollectivesTest() {
        val collective = getCollective("test-data/GetStartProtocolTestData/test1.csv")

        val members = collective.members.map{
            listOf(
                it.collective, it.wantedGroup, it.surname,
                it.name, it.birthYear, it.sportRank
            )
        }

        val participant1 = listOf("ПСКОВ", "М14",
            "ВАСИЛЕНКО", "ГРИГОРИЙ", 2007, ""
        )
        val participant2 = listOf("ПСКОВ", "М14",
            "ВАСИЛЕНКО", "МИХАИЛ", 2007, "1p"
        )

        assertEquals(listOf(participant1, participant2), members)
    }
}