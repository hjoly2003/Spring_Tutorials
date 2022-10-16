#!/usr/bin/env bash
cd $(dirname $0)

# An orphan token has no parent. This is to avoid the token beeing deleted as a normal token would on its parent deletion.
# See documentation at https://www.vaultproject.io/api-docs/auth/token#create-token
curl --header "X-Vault-Token: myroot" \
--request POST \
--data @createtoken.json \
http://localhost:8200/v1/auth/token/create-orphan