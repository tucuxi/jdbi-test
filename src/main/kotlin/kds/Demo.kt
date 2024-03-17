package kds

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Demo(val invoiceDao: InvoiceDao, val outboxDao: OutboxDao) {

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
        val invoice = Invoice(type = "plain", recipient = "joe")
        invoiceDao.insert(invoice)
        val event = DraftInvoiceEvent(invoice)
        outboxDao.insert(OutboxEntry.fromEvent(event))
        return invoice
    }

    @GetMapping("/outbox")
    fun retrieveEvents(): List<OutboxEntry> {
        return outboxDao.findAll()
    }
}
