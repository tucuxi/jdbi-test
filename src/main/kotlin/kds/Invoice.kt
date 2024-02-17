package kds

data class Invoice(
    val id: String,
    val type: String,
    val recipient: String
) {
    val domainObject = "invoice"
}
