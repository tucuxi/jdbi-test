package kds

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ulid.ULID

@RestController
class Demo(private val jdbi: Jdbi) {

    @GetMapping("/invoices")
    fun retrieveInvoices(): List<Invoice> {
        return jdbi.inTransaction<List<Invoice>, Exception> { handle ->
            handle
                .select("SELECT * FROM invoices ORDER BY id DESC")
                .mapTo<Invoice>()
                .toList()
        }
    }

    @PostMapping("/invoices")
    fun insertRandomInvoice(): Invoice {
        val invoice = Invoice(
            id = randomInvoiceId(),
            type = "plain",
            recipient = "joe",
        )
        jdbi.useTransaction<Exception> { handle ->
            handle
                .createUpdate("INSERT INTO invoices(id, type, recipient) values(:id, :type, :recipient)")
                .bindBean(invoice)
                .execute()
        }
        return invoice
    }

}

private fun randomInvoiceId() = "in_" + ULID.randomULID()