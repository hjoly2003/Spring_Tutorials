#!/usr/bin/env bash
cd $(dirname $0)

# See documentation at https://www.vaultproject.io/api-docs/secret/kv/kv-v2#create-update-secret
curl --header "X-Vault-Token: myroot" \
--request POST \
--data @newsecretpayload.json \
http://localhost:8200/v1/myengine/data/myspringapplication/staging