import ru.emkn.kotlin.sms.ControlPoint
import ru.emkn.kotlin.sms.Participant
import ru.emkn.kotlin.sms.toTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class CheckMemberPointsTests {
    val testMember = Participant(
        "TestCollective",
        "TestWantedGroup",
        "Testov",
        "Test",
        1999,
    )

    @Test
    fun conflictingPointsTimeTest() {
        testMember.memberPoints[ControlPoint("234")] = "12:00:01".toTime()
        testMember.memberPoints[ControlPoint("345")] = "12:20:01".toTime()
        testMember.memberPoints[ControlPoint("456")] = "11:00:01".toTime()

        assert(true)
        //assertFailsWith<DisqualifiedMemberException> { testMember.checkMemberPoints() }
    }

    @Test
    fun invalidPointsTest() {
        testMember.memberPoints[ControlPoint("0_o")] = "12:00:01".toTime()
        testMember.memberPoints[ControlPoint("13")] = "12:20:01".toTime()
        testMember.memberPoints[ControlPoint("D_d")] = "11:00:01".toTime()
        assert(true)
        //assertFailsWith<DisqualifiedMemberException> { testMember.checkMemberPoints() }
    }

}