#!/bin/sh
docker pull postgres
docker-compose -f docker-compose-postgres.yaml up -d
./gradlew bootRun