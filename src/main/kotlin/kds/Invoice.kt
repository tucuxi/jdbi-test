package kds

import ulid.ULID

data class Invoice(val type: String, val recipient: String) {
    val id: String = nextInvoiceId()
    val domainObject = "invoice"

    companion object {
        private var ulid = ULID.nextULID()

        @Synchronized
        fun nextInvoiceId(): String {
            ulid = ULID.Monotonic.nextULID(ulid)
            return "in_$ulid"
        }
    }
}
