package kds

import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface InvoiceDao {

    @SqlUpdate("INSERT INTO invoices (id, type, recipient) values (:id, :type, :recipient)")
    fun createInvoice(@BindKotlin invoice: Invoice)

    @SqlQuery("SELECT * FROM invoices ORDER BY id DESC")
    fun listInvoices(): List<Invoice>

}