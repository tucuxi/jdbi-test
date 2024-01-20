package kds

import com.fasterxml.uuid.Generators
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin

data class Invoice(
    val id: String,
    val type: String,
    val recipient: String
)

fun main() {
    val jdbi = Jdbi.create("jdbc:postgresql://localhost/first", "user", "")
    jdbi.installPlugin(KotlinPlugin())

    jdbi.inTransaction<Unit, Exception> { transactionHandle ->
        repeat(5) {
            insertRandomInvoice(transactionHandle)
        }
    }

    val sample = jdbi.inTransaction<List<Invoice>, Exception> { transactionHandle ->
        retrieveInvoices(transactionHandle).take(10)
    }

    sample.forEach { println(it) }
}

fun createTable(jdbi: Jdbi) {
    jdbi.useHandle<Exception> { handle ->
        handle.execute("CREATE TABLE invoices(id CHAR(39) PRIMARY KEY, type VARCHAR, recipient VARCHAR)")
    }
}

fun insertRandomInvoice(handle: Handle) {
    val invoice = Invoice(
        id = "in_" + Generators.timeBasedEpochGenerator().generate(), // UUIDv7
        type = "plain",
        recipient = "joe",
    )
    handle.execute(
        "INSERT INTO invoices(id, type, recipient) values(?, ?, ?)",
        invoice.id, invoice.type, invoice.recipient
    )
}

fun retrieveInvoices(handle: Handle): List<Invoice> {
    return handle.select("SELECT id, type, recipient FROM invoices ORDER BY id DESC")
        .mapTo(Invoice::class.java)
        .toList()
}