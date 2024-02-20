package kds

import ulid.ULID

sealed class Event(val action: String) {
    val id: String = nextEventId()
    val time: Long = System.currentTimeMillis()
    val domainObject = "event"

    companion object {
        private var ulid = ULID.nextULID()

        @Synchronized
        fun nextEventId(): String {
            ulid = ULID.Monotonic.nextULID(ulid)
            return "ev_$ulid"
        }
    }
}

data class HeartbeatEvent(val message: String = "") : Event("heartbeat")

data class DraftInvoiceEvent(val invoice: Invoice) : Event("draft_invoice")

