package kds

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

internal class OutboxDaoTest {

    val outboxDao = postgresExtension.jdbi.onDemand<OutboxDao>()
    
    @BeforeEach
    fun cleanTable() {
        with(postgresExtension.sharedHandle) {
            execute("DELETE FROM outbox")
            execute("DELETE FROM processed")
        }
    }

    @Test
    fun `findAll should return newly created event`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        val entries = outboxDao.findAll()
        assertEquals(1, entries.size)
        assertEquals(event.id, entries.first().eventId)
    }

    @Test
    fun `findUnprocessed should return unprocessed event`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        val unprocessedEvents1 = outboxDao.findUnprocessed("FirstProcessor", 1)
        assertEquals(1, unprocessedEvents1.size)
        assertEquals(event.id, unprocessedEvents1.first().eventId)
        val unprocessedEvents2 = outboxDao.findUnprocessed("SecondProcessor", 1)
        assertEquals(1, unprocessedEvents2.size)
        assertEquals(event.id, unprocessedEvents2.first().eventId)
    }

    @Test
    fun `findUnprocessed should not return processed event`() {
        val processor = "TestProcessor"
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        outboxDao.markProcessedUntil(processor, event.id)
        val unprocessedEvents = outboxDao.findUnprocessed(processor, 1)
        assertEquals(0, unprocessedEvents.size)
    }
    
    @Test
    fun `findUnprocessed should not be affected by other processors`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        outboxDao.markProcessedUntil("FirstProcessor", event.id)
        val unprocessedEvents = outboxDao.findUnprocessed("SecondProcessor", 1)
        assertEquals(1, unprocessedEvents.size)
        assertEquals(event.id, unprocessedEvents.first().eventId)
    }

    @Test
    fun `findUnprocessed should return oldest events first`() {
        val processor = "TestProcessor"
        val events = arrayOf(HeartbeatEvent(), HeartbeatEvent(), HeartbeatEvent())
        events.forEach { outboxDao.insert(OutboxEntry.fromEvent(it)) }
        outboxDao.markProcessedUntil(processor, events.first().id)
        val unprocessedEvents = outboxDao.findUnprocessed(processor, 3)
        assertEquals(2, unprocessedEvents.size)
        assertEquals(events[1].id, unprocessedEvents[0].eventId)
        assertEquals(events[2].id, unprocessedEvents[1].eventId)
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
        fun createTables() {
            with(postgresExtension.sharedHandle) {
                execute("CREATE TABLE outbox (eventid VARCHAR PRIMARY KEY, data JSONB NOT NULL)")
                execute("CREATE TABLE processed (processor VARCHAR PRIMARY KEY, eventid VARCHAR NOT NULL)")
            }
        }
      
        val mapper = jacksonObjectMapper()
    }
}
