#!/usr/bin/env bash
cd $(dirname $0)
docker stop vault
docker rm -f vault

# See "Running Vault for Development" from https://hub.docker.com/_/vault
docker run --cap-add=IPC_LOCK \
-p 8200:8200 \
-d \
--name vault \
-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' \
-e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' \
vault
