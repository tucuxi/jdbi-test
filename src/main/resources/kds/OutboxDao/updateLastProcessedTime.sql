INSERT INTO processed (processor, lastProcessedTime) VALUES (:processor, NOW())
ON CONFLICT (processor) DO UPDATE SET lastProcessedTime = NOW()
