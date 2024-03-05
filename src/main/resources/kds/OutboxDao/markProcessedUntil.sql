INSERT INTO processed (processor, eventId) VALUES (:processor, :eventId)
ON CONFLICT (processor) DO UPDATE SET eventId = :eventId
