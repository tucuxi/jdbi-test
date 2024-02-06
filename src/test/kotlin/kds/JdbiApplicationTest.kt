package kds

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ulid.ULID
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
internal class JdbiApplicationTest {

    @Autowired
    private lateinit var invoiceDao: InvoiceDao

    @Test
    fun `Find invoice by id should return newly created invoice`() {
        val invoiceCreated = Invoice("in_" + ULID.randomULID(), "plain", "recipient")
        invoiceDao.insert(invoiceCreated)
        val invoiceRetrieved = invoiceDao.findById(invoiceCreated.id)
        assertEquals(invoiceCreated, invoiceRetrieved)
    }
}