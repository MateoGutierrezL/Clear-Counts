SHELL := /bin/bash
COMPOSE := docker compose
BASE := -f docker-compose.yml
DEV := -f docker-compose.dev.yml
PROJECT := Pagina-web-ClearCounts

.PHONY: help up down logs ps build restart db-shell be-shell fe-shell prune

help:
	@echo "Targets disponibles:"
	@echo "  make up         # Levanta stack DEV"
	@echo "  make down       # Apaga stack"
	@echo "  make logs       # Logs agregados"
	@echo "  make ps         # Estado servicios"
	@echo "  make build      # Build imagenes"
	@echo "  make restart    # Reinicia contenedores"
	@echo "  make db-shell   # Entra a psql"
	@echo "  make be-shell   # Shell en backend"
	@echo "  make fe-shell   # Shell en frontend"
	@echo "  make prune      # Limpieza de recursos no usados"

up:
	$(COMPOSE) $(BASE) $(DEV) up -d --build

down:
	$(COMPOSE) $(BASE) $(DEV) down

logs:
	$(COMPOSE) $(BASE) $(DEV) logs -f --tail=200

ps:
	$(COMPOSE) $(BASE) $(DEV) ps

build:
	$(COMPOSE) $(BASE) $(DEV) build

restart:
	$(COMPOSE) $(BASE) $(DEV) restart

#db-shell:
#	@docker exec -it mediapp_db psql -U $${POSTGRES_USER:-mediapp} -d $${POSTGRES_DB:-mediapp_db}

be-shell:
	@docker exec -it Pagina-web-backend sh -lc "bash || sh"

fe-shell:
	@docker exec -it Pagina-web-frontend sh -lc "bash || sh"

prune:
	docker system prune -f