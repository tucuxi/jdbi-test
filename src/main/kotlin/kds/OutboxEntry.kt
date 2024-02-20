package kds

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class OutboxEntry(val eventId: String, val data: String) {
    companion object {
        private val mapper = jacksonObjectMapper()

        fun fromEvent(event: Event) = OutboxEntry(
            eventId = event.id,
            data = mapper.writeValueAsString(event),
        )   
    }
}
