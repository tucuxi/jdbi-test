CREATE TABLE processed (processor VARCHAR PRIMARY KEY, eventId VARCHAR REFERENCES outbox, lastProcessedTime TIMESTAMP)
