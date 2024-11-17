DB_EXEC_CMD := docker-compose exec db bash -c
LOGS_CMD := docker-compose logs --follow --tail=5
SVC_DB := db

run-db:
	@docker-compose up -d $(SVC_DB)

stop-db:
	@docker-compose stop $(SVC_DB)

logs-db:
	@$(LOGS_CMD) $(SVC_DB)

connect-db:
	@$(DB_EXEC_CMD) 'mysql -u backend-db -pbackend-db backend-db'
