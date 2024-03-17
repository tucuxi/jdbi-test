package kds

import kotlin.time.measureTime
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

const val PROCESSOR_ID = "kafka"
const val LIMIT = 10
    
@Component
class OutboxProcessor(val outboxDao: OutboxDao) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedDelay = 60000, initialDelayString = "#{ T(java.util.concurrent.ThreadLocalRandom).current().nextInt(30000, 60000) }")
    fun processOutbox() {
        var eventsProcessed: Int
        val timeTaken = measureTime {
            eventsProcessed = processOldestUnprocessedEvents(PROCESSOR_ID, LIMIT)
        }
        logger.info { "Processed $eventsProcessed outbox entries in $timeTaken" }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    private fun processOldestUnprocessedEvents(processor: String, limit: Int): Int {
        // This has been designed to avoid race conditions when multiple instances
        // with this code run concurrently. Do not make changes unless you know
        // what you are doing.
        outboxDao.updateLastProcessedTime(processor)
        val events = outboxDao.findUnprocessed(processor, limit)
        if (events.isNotEmpty()) {
            // Send events...
            outboxDao.markProcessedUntil(processor, events.last().eventId)
        }
        return events.size
    }
}
