package kds

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ulid.ULID

@RestController
class Demo(val invoiceDao: InvoiceDao, val eventDao: EventDao) {

    private val mapper = jacksonObjectMapper()

    @GetMapping("/invoices")
    fun retrieveInvoices(): List<Invoice> {
        return invoiceDao.findAll()
    }

    @GetMapping("/invoices/{id}")
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
        val event = Event.CreateInvoice(invoice)
        eventDao.insert(event.id, event.createdAt, mapper.writeValueAsString(event))
        return invoice
    }
}

private fun randomInvoiceId() = "in_" + ULID.randomULID()
