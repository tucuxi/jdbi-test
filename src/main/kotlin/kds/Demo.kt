package kds

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ulid.ULID

@RestController
class Demo {

    @Autowired
    private lateinit var invoiceDao: InvoiceDao

    @GetMapping("/invoices")
    @Transactional
    fun retrieveInvoices(): List<Invoice> {
        return invoiceDao.findAll()
    }

    @GetMapping("/invoices/{id}")
    @Transactional
    fun retrieveInvoice(@PathVariable id: String): Invoice {
        return invoiceDao.findById(id)
    }

    @PostMapping("/invoices")
    @Transactional
    fun insertRandomInvoice(): Invoice {
        val invoice = Invoice(
            id = randomInvoiceId(),
            type = "plain",
            recipient = "joe",
        )
        invoiceDao.insert(invoice)
        return invoice
    }
}

private fun randomInvoiceId() = "in_" + ULID.randomULID()