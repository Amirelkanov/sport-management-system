import ru.emkn.kotlin.sms.*
import java.io.File
import java.util.*
import kotlin.test.*

internal class TestParticipant {
    @Test
    fun testAge() {
        val participant = Participant("Russia", "M18", "Minnekhanov", "Bulat", 2003)
        assertEquals(Calendar.getInstance().get(Calendar.YEAR) - participant.birthYear, participant.age)
    }

}