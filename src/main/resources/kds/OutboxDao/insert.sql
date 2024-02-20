INSERT INTO outbox (eventId, data) VALUES (:eventId, :data::jsonb)
