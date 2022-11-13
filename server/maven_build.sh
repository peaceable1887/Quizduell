#!/usr/bin/env bash

mvn install -DskipTests -f domain
mvn install -DskipTests -f application
mvn install -DskipTests -f common
mvn install -DskipTests -f persistence
mvn install -DskipTests -f authserver
mvn install -DskipTests -f lobbyserver