#!/usr/bin/env bash

./create_keyfiles.sh
mvn install -DskipTests -f domain
mvn install -DskipTests -f application
mvn install -DskipTests -f common
mvn install -DskipTests -f persistence
mvn install -DskipTests -f authserver
mvn install -DskipTests -f lobbyserver
mvn install -DskipTests -f quizserver
mvn install -DskipTests -f statsserver