#!/bin/bash

docker run --name mypostgres \
	-e POSTGRES_USER=myuser \
	-e POSTGRES_PASSWORD=mypassword \
	-e POSTGRES_DB=mydb \
	-v $(pwd)/init-data:/docker-entrypoint-initdb.d \
	-p 5432:5432 -d postgres:latest
