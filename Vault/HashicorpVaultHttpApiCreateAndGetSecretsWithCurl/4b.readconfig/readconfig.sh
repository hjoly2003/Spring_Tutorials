#!/usr/bin/env bash
cd $(dirname $0)

# See documentation at https://www.vaultproject.io/api-docs/secret/kv/kv-v2#read-kv-engine-configuration
curl --header "X-Vault-Token: myroot" \
http://localhost:8200/v1/myengine/config