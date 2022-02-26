import org.junit.Test
import ru.emkn.kotlin.sms.loadDistancesForGroups
import kotlin.test.assertEquals

internal class ReadDistancesForGroupsTest {

    @Test
    fun test() {
        assertEquals(mapOf("М10" to "МЖ9 10", "М12" to "М12"),
            loadDistancesForGroups("test-data/InitGroupsTestData/groups.txt"))
    }

}