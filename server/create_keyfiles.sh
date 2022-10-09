#!/usr/bin/env bash

openssl genrsa -out authserver/src/main/resources/certs/keypair.pem 2048
openssl rsa -in authserver/src/main/resources/certs/keypair.pem -pubout -out authserver/src/main/resources/certs/public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in authserver/src/main/resources/certs/keypair.pem -out authserver/src/main/resources/certs/private.pem