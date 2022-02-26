import ru.emkn.kotlin.sms.WrongFileFormatException
import ru.emkn.kotlin.sms.checkCollectivesData
import java.io.FileNotFoundException
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class CheckCollectiveDataTests {

    @Test
    fun notExistingFileTest() {
        assertFailsWith<FileNotFoundException> { checkCollectivesData("ThisFileDoesNotExist.csv") }
    }

    @Test
    fun invalidCollectiveNameHeaderTest() {
        assertFailsWith<WrongFileFormatException> { checkCollectivesData("test-data/CheckCollectivesDataData/invalidCollectiveNameHeader.csv") }
    }

    @Test
    fun emptyCollectiveNameHeaderTest() {
        assertFailsWith<WrongFileFormatException> { checkCollectivesData("test-data/CheckCollectivesDataData/emptyCollectiveNameHeader.csv") }
    }

    @Test
    fun invalidHeadersTest() {
        assertFailsWith<WrongFileFormatException> { checkCollectivesData("test-data/CheckCollectivesDataData/invalidHeaders.csv") }
    }

    @Test
    fun wrongNumberOfColumns() {
        assertFailsWith<WrongFileFormatException> { checkCollectivesData("test-data/CheckCollectivesDataData/wrongNumberOfColumns.csv") }
    }
}