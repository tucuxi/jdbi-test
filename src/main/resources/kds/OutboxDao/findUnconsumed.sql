SELECT * FROM outbox WHERE eventId > (SELECT eventId FROM consumed WHERE consumer = :consumer) IS NOT FALSE ORDER BY eventId ASC LIMIT :limit
