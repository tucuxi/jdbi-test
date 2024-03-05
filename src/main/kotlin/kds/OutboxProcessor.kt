package kds

import kotlin.time.measureTime
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

const val PROCESSOR_ID = "kafka"
const val LIMIT = 10
    
@Component
class OutboxProcessor(val outboxDao: OutboxDao) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedDelay = 60000)
    fun processOutbox() {
        processOldestUnprocessedEvents(PROCESSOR_ID, LIMIT)
    }

    @Transactional
    private fun processOldestUnprocessedEvents(processor: String, limit: Int): Int {
        val events = outboxDao.findUnprocessed(processor, limit)
        if (events.isNotEmpty()) {
            val timeTaken = measureTime {
                // Send events...
                outboxDao.markProcessedUntil(processor, events.last().eventId)
            }
            logger.info { "Processed ${events.size} events in $timeTaken" }
        }
        return events.size
    }
}
