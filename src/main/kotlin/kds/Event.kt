package kds

import ulid.ULID

sealed class Event {
    val id = "ev_" + ULID.randomULID()
    val time = System.currentTimeMillis()
    val domainObject = "event"

    data class CreateInvoice(val invoice: Invoice) : Event() {
        val action = "create_invoice"
    }
}
