# Gruppe B Quizduell

## run docker mariadb for testing

    docker run -d --name mariadb \
    -e MARIADB_USER=admin \
    -e MARIADB_PASSWORD=admin \
    -e MARIADB_ROOT_PASSWORD=root \
    -e MARIADB_DATABASE=quizduell \
    -p 3306:3306 \
    mariadb:latest
