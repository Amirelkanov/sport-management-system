import org.junit.Test
import ru.emkn.kotlin.sms.*
import kotlin.test.assertEquals

internal class InitGroupsTest {
    @Test
    fun test() {
        val collectives = loadCollectives("test-data/InitGroupsTestData/collectives")
        val distances = loadDistances("test-data/InitGroupsTestData/courses.txt")
        val l1 = mutableListOf<Participant>()
        val l2 = mutableListOf<Participant>()
        for (collective in collectives) {
            for (participant in collective.members) {
                if (participant.wantedGroup == "М10") {
                    l1.add(participant)
                } else {
                    l2.add(participant)
                }
            }
        }
        val group1 = Group("М10", l1, distances.find { it.name == "МЖ9 10" }!!)
        val group2 = Group("М12", l2, distances.find { it.name == "М12"}!!)
        val distancesForGroupsTest = loadDistancesForGroups("test-data/InitGroupsTestData/groups.txt")
        assertEquals(listOf(group1, group2), initGroups(distancesForGroupsTest, collectives, distances))
    }
}