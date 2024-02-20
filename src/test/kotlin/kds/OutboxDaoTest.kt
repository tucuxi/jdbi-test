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
            execute("DELETE FROM consumed")
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
    fun `findUnconsumed should return unconsumed event`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        val unconsumedEvents1 = outboxDao.findUnconsumed("FirstTestConsumer", 1)
        assertEquals(1, unconsumedEvents1.size)
        assertEquals(event.id, unconsumedEvents1.first().eventId)
        val unconsumedEvents2 = outboxDao.findUnconsumed("SecondTestConsumer", 1)
        assertEquals(1, unconsumedEvents2.size)
        assertEquals(event.id, unconsumedEvents2.first().eventId)
    }

    @Test
    fun `findUnconsumed should not return consumed event`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        outboxDao.markConsumedUntil("TestConsumer", event.id)
        val unconsumedEvents = outboxDao.findUnconsumed("TestConsumer", 1)
        assertEquals(0, unconsumedEvents.size)
    }
    
    @Test
    fun `findUnconsumed should not be affected by other consumers`() {
        val event = HeartbeatEvent()
        outboxDao.insert(OutboxEntry.fromEvent(event))
        outboxDao.markConsumedUntil("FirstTestConsumer", event.id)
        val unconsumedEvents = outboxDao.findUnconsumed("SecondTestConsumer", 1)
        assertEquals(1, unconsumedEvents.size)
        assertEquals(event.id, unconsumedEvents.first().eventId)
    }

    @Test
    fun `findUnconsumed should return oldest events first`() {
        val events = arrayOf(HeartbeatEvent(), HeartbeatEvent(), HeartbeatEvent())
        events.forEach { println(it.id) }
        events.forEach { outboxDao.insert(OutboxEntry.fromEvent(it)) }
        outboxDao.markConsumedUntil("TestConsumer", events.first().id)
        val unconsumedEvents = outboxDao.findUnconsumed("TestConsumer", 3)
        assertEquals(2, unconsumedEvents.size)
        assertEquals(events[1].id, unconsumedEvents[0].eventId)
        assertEquals(events[2].id, unconsumedEvents[1].eventId)
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
                execute("CREATE TABLE consumed (consumer VARCHAR PRIMARY KEY, eventid VARCHAR NOT NULL)")
            }
        }
      
        val mapper = jacksonObjectMapper()
    }
}
