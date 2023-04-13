# Quizduell
## Streamcast

[![IMAGE_ALT](https://img.youtube.com/vi/UmX4kyB2wfg/0.jpg)](https://www.youtube.com/watch?v=Y5ySBIC16K4)

## Projekt lokal ausführen

### Linux

- In der Command Line in den "server/" Ordner wechseln

        cd server

- Build Script ausführen

        ./maven_build.sh

- Docker Compose Command ausführen

        docker-compose up --build -d

- Server sollte nun erreichbar sein: http://localhost:8080

### Windows

- In der Command Line in den "server/" Ordner wechseln

        cd server

- Build Script ausführen

        ./maven_build.bat

- Docker Compose Command ausführen

        docker-compose up --build -d

- Server sollte nun erreichbar sein: http://localhost:8080

### run docker mariadb for testing

    docker run -d --name mariadb \
    -e MARIADB_USER=admin \
    -e MARIADB_PASSWORD=admin \
    -e MARIADB_ROOT_PASSWORD=root \
    -e MARIADB_DATABASE=quizduell \
    -p 3306:3306 \
    mariadb:latest

FINALER COMMIT PRÜFUNGSLEISTUNG
