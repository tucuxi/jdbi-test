SELECT * FROM outbox WHERE eventId > (SELECT eventId FROM processed WHERE processor = :processor) IS NOT FALSE ORDER BY eventId ASC LIMIT :limit
