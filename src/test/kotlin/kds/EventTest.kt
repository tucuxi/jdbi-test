package kds

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EventTest {

    @Test
    fun `event id should have correct length and prefix`() {
        val event = HeartbeatEvent()
        assertEquals(29, event.id.length)
        assertTrue(event.id.startsWith("ev_"))
    }

    @Test
    fun `ids of events created almost simultaneously should be monotonic`() {
        val events = List(1000) { HeartbeatEvent("$it") }
        assertTrue(events.zipWithNext().all { (p, q) -> p.id < q.id })
    }
}
