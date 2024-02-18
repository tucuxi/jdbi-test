package kds

import ulid.ULID

data class Invoice(
    val id: String = "in_" + ULID.randomULID(),
    val type: String,
    val recipient: String
) {
    val domainObject = "invoice"
}
