package kds

import ulid.ULID

sealed class Event {
    val id = "ev_" + ULID.randomULID()
    val createdAt = System.currentTimeMillis()

    class CreateInvoice(invoice: Invoice) : Event() {
        val invoiceId = invoice.id
        val invoiceType = invoice.type
    }
}
