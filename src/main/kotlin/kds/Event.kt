package kds

import ulid.ULID

sealed class Event(
    val id: String = "ev_" + ULID.randomULID(),
    val time: Long = System.currentTimeMillis(),
    val action: String,
) {
    val domainObject = "event"
}

class HeartbeatEvent() : Event(action = "heartbeat")

class DraftInvoiceEvent(val invoice: Invoice) : Event(action = "draft_invoice")

