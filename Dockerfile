FROM postgres:latest

COPY init-data /docker-entrypoint-initdb.d/

