package kds

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.kotlin.attach
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ulid.ULID

@RestController
class Demo(private val jdbi: Jdbi) {

    @GetMapping("/invoices")
    fun retrieveInvoices(): List<Invoice> {
        return jdbi.inTransaction<List<Invoice>, Exception> { handle ->
            val invoiceDao = handle.attach(InvoiceDao::class)
            invoiceDao.listInvoices()
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
            val invoiceDao = handle.attach(InvoiceDao::class)
            invoiceDao.createInvoice(invoice)
        }
        return invoice
    }

}

private fun randomInvoiceId() = "in_" + ULID.randomULID()