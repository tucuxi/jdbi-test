package kds

import org.jdbi.v3.spring.JdbiRepository
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
    fun findUnprocessed(@Bind processor: String, @Bind limit: Int): List<OutboxEntry>

    @SqlUpdate
    fun markProcessedUntil(@Bind processor: String, @Bind eventId: String)

    @SqlUpdate
    fun updateLastProcessedTime(@Bind processor: String)
}
