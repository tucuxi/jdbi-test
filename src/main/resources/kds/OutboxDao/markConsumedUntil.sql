INSERT INTO consumed (consumer, eventId) VALUES (:consumer, :eventId)
ON CONFLICT (consumer) DO UPDATE SET eventId = :eventId
