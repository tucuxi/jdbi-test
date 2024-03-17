INSERT INTO processed (processor, eventId, lastProcessedTime) VALUES (:processor, :eventId, NOW())
ON CONFLICT (processor) DO UPDATE SET eventId = :eventId, lastProcessedTime = NOW()
