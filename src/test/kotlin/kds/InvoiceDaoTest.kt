package kds

import de.softwareforge.testing.postgres.junit5.EmbeddedPgExtension
import de.softwareforge.testing.postgres.junit5.MultiDatabaseBuilder
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.testing.junit5.JdbiExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.RegisterExtension
import ulid.ULID
import kotlin.test.Test
import kotlin.test.assertEquals

internal class InvoiceDaoTest {

    @Test
    fun `Find invoice by id should return newly created invoice`() {
        val invoiceDao = postgresExtension.jdbi.onDemand<InvoiceDao>()
        val invoiceCreated = Invoice("in_" + ULID.randomULID(), "plain", "recipient")
        invoiceDao.insert(invoiceCreated)
        val invoiceRetrieved = invoiceDao.findById(invoiceCreated.id)
        assertEquals(invoiceCreated, invoiceRetrieved)
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
                .execute("CREATE TABLE IF NOT EXISTS invoices(id VARCHAR PRIMARY KEY, type VARCHAR, recipient VARCHAR)")
        }

    }
}
