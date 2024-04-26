@echo off

docker build -t javaconcept .
docker run -d javaconcept
