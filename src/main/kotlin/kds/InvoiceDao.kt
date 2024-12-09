package kds

import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@JdbiRepository
@UseClasspathSqlLocator
interface InvoiceDao {
    @SqlUpdate
    fun insert(@BindKotlin invoice: Invoice)

    @SqlQuery
    fun findAll(): List<Invoice>

    @SqlQuery
    fun findById(@Bind id: String): Invoice
}
