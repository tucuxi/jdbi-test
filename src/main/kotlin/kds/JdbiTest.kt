package kds

import com.fasterxml.uuid.Generators
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

    repeat(5) {
        insertRandomInvoice(jdbi)
    }

    retrieveInvoices(jdbi)
        .take(10)
        .forEach { println(it) }
}

fun createTable(jdbi: Jdbi) {
    jdbi.useHandle<Exception> { handle ->
        handle.execute("CREATE TABLE invoices(id CHAR(39) PRIMARY KEY, type VARCHAR, recipient VARCHAR)")
    }
}

fun insertRandomInvoice(jdbi: Jdbi) {
    val invoice = Invoice(
        id = "in_" + Generators.timeBasedEpochGenerator().generate(), // UUIDv7
        type = "plain",
        recipient = "joe",
    )
    jdbi.useHandle<Exception> { handle ->
        handle.execute(
            "INSERT INTO invoices(id, type, recipient) values(?, ?, ?)",
            invoice.id, invoice.type, invoice.recipient
        )
    }
}

fun retrieveInvoices(jdbi: Jdbi): List<Invoice> {
    return jdbi.withHandle<List<Invoice>, Exception> { handle ->
        handle.select("SELECT id, type, recipient FROM invoices ORDER BY id DESC")
            .mapTo(Invoice::class.java)
            .toList()
    }
}