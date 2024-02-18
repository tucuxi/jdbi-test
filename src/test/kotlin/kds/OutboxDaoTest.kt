package kds

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

internal class OutboxDaoTest {

    @Test
    fun `findAll should return newly created event`() {
        val outboxDao = postgresExtension.jdbi.onDemand<OutboxDao>()
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        val allEvents = outboxDao.findAll()
        assertEquals(1, allEvents.size)
        assertEquals(event.id, allEvents[0].id)
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
                .execute("CREATE TABLE outbox (id VARCHAR PRIMARY KEY, data VARCHAR NOT NULL)")
        }

        val mapper = jacksonObjectMapper()
    }
}
