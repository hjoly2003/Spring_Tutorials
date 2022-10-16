#!/usr/bin/env bash
cd $(dirname $0)

# See documentation at https://www.vaultproject.io/api-docs/secret/kv/kv-v2#configure-the-kv-engine
curl --header "X-Vault-Token: myroot" \
--request POST \
--data @writeconfig.json \
http://localhost:8200/v1/myengine/config