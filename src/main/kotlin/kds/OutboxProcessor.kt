package kds

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
        processOldestUnprocessedEvents()
    }

    @Transactional
    private fun processOldestUnprocessedEvents() {
        val events = outboxDao.findUnprocessed(PROCESSOR_ID, LIMIT)
        if (events.isNotEmpty()) {
            outboxDao.markProcessedUntil(PROCESSOR_ID, events.last().eventId)
        }
        logger.info("Processed ${events.size} events")
    }
}
