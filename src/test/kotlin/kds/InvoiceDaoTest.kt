package kds

import de.softwareforge.testing.postgres.junit5.EmbeddedPgExtension
import de.softwareforge.testing.postgres.junit5.MultiDatabaseBuilder
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.testing.junit5.JdbiExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import ulid.ULID
import kotlin.test.Test
import kotlin.test.assertEquals

internal class InvoiceDaoTest {

    val invoiceDao = postgresExtension.jdbi.onDemand<InvoiceDao>()

    @BeforeEach
    fun cleanTable() {
        postgresExtension.sharedHandle
            .execute("DELETE FROM invoices")
    }
    
    @Test
    fun `findById should return newly created invoice`() {
        val invoice = Invoice("in_" + ULID.randomULID(), "plain", "recipient")
        invoiceDao.insert(invoice)
        val invoiceRetrieved = invoiceDao.findById(invoice.id)
        assertEquals(invoice, invoiceRetrieved)
    }

    companion object {

        @JvmStatic
        @RegisterExtension
        val pg: EmbeddedPgExtension = MultiDatabaseBuilder.instanceWithDefaults().build()

        @JvmStatic
        @RegisterExtension
        val postgresExtension: JdbiExtension = JdbiExtension.postgres(pg).withPlugin(KotlinSqlObjectPlugin())

        @JvmStatic
        @BeforeAll
        fun createTable() {
            postgresExtension.sharedHandle
                .execute("CREATE TABLE invoices (id VARCHAR PRIMARY KEY, type VARCHAR, recipient VARCHAR)")
        }
    }
}
