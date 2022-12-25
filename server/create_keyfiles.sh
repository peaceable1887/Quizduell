#!/usr/bin/env bash

mkdir -p authserver/src/main/resources/certs
mkdir -p lobbyserver/src/main/resources/certs
mkdir -p quizserver/src/main/resources/certs
mkdir -p statsserver/src/main/resources/certs

openssl genrsa -out authserver/src/main/resources/certs/keypair.pem 2048
openssl rsa -in authserver/src/main/resources/certs/keypair.pem -pubout -out authserver/src/main/resources/certs/public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in authserver/src/main/resources/certs/keypair.pem -out authserver/src/main/resources/certs/private.pem

cp authserver/src/main/resources/certs/* lobbyserver/src/main/resources/certs/
cp authserver/src/main/resources/certs/* quizserver/src/main/resources/certs/
cp authserver/src/main/resources/certs/* statsserver/src/main/resources/certs/