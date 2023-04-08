#!/bin/bash

#docker compose -f docker/docker-compose.yml -p java-discord-help-bot down -v
#docker compose -f docker/docker-compose.yml -p java-discord-help-bot up -d

docker compose -f docker/docker-compose.yml exec db 'mkdir -p /backup/current; pg_dumpall -c -U ${POSTGRES_USER} > /backup/current/dump.sql'