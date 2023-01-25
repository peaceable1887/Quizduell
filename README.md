# Gruppe B Quizduell

## Projekt zum Testen erreichbar unter:

https://test2.burmeister.hamburg

## Projekt lokal ausführen

- In der Command Line in den "server/" Ordner wechseln
- Build Script ausführen

        ./maven_build.sh

- Docker Compose Command ausführen

        docker-compose stop && docker-compose rm -f && docker volume prune -f && docker-compose up --build --force-recreate -d

- Server sollte nun erreichbar sein: http://localhost:8080

### run docker mariadb for testing

    docker run -d --name mariadb \
    -e MARIADB_USER=admin \
    -e MARIADB_PASSWORD=admin \
    -e MARIADB_ROOT_PASSWORD=root \
    -e MARIADB_DATABASE=quizduell \
    -p 3306:3306 \
    mariadb:latest
