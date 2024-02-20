package kds

import org.jdbi.v3.spring5.JdbiRepository
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@JdbiRepository
@UseClasspathSqlLocator
interface OutboxDao{

    @SqlUpdate
    fun insert(@BindKotlin entry: OutboxEntry)

    @SqlQuery
    fun findAll(): List<OutboxEntry>

    @SqlQuery
    fun findUnconsumed(@Bind consumer: String, @Bind limit: Int): List<OutboxEntry>

    @SqlUpdate
    fun markConsumedUntil(@Bind consumer: String, @Bind eventId: String)
}
