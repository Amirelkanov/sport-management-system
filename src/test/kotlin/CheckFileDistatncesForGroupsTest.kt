import org.junit.Test
import ru.emkn.kotlin.sms.WrongFileFormatException
import ru.emkn.kotlin.sms.checkFileDistancesForGroups
import java.io.File
import kotlin.test.assertFailsWith

internal class CheckFileDistatncesForGroupsTest {
    @Test
    fun testEmpty() {
        assertFailsWith<WrongFileFormatException> { checkFileDistancesForGroups(File("test-data/InitGroupsTestData/empty.txt")) }
    }

    @Test
    fun testWrongFormat() {
        assertFailsWith<WrongFileFormatException> { checkFileDistancesForGroups(File("test-data/InitGroupsTestData/wrongFormat.txt")) }
    }

}