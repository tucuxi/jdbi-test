package kds

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ulid.ULID

@RestController
class Demo(private val jdbi: Jdbi) {

    @GetMapping("/invoices")
    fun retrieveInvoices(): List<Invoice> {
        return jdbi
            .onDemand<InvoiceDao>()
            .listInvoices()
    }

    @PostMapping("/invoices")
    fun insertRandomInvoice(): Invoice {
        val invoice = Invoice(
            id = randomInvoiceId(),
            type = "plain",
            recipient = "joe",
        )
        jdbi
            .onDemand<InvoiceDao>()
            .createInvoice(invoice)

        return invoice
    }

}

private fun randomInvoiceId() = "in_" + ULID.randomULID()