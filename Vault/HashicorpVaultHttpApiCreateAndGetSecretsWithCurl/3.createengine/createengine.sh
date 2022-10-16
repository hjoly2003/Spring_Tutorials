#!/usr/bin/env bash
cd $(dirname $0)

# See documentation at https://www.vaultproject.io/api-docs/system/mounts#enable-secrets-engine
# When invoking "$> vault secrets enable -path=myengine -version=2 kv", use the -output-curl-string option to get the curl version of this CLI command.
curl -X POST \
    -H "X-Vault-Token: myroot" \
    -d @createengine.json \
    http://localhost:8200/v1/sys/mounts/myengine
