#!/usr/bin/env bash
cd $(dirname $0)

# curl --header "X-Vault-Token: myroot" \

# See documentation at https://www.vaultproject.io/api-docs/secret/kv/kv-v2#read-secret-version
# The vault token is obtained from the result of running the createtoken.sh under "auth.client_token".
# curl --header "X-Vault-Token: hvs.CAESIIEJNCvhDmZY9xhoflEDzthWsI01izxxMJhswFZrF_U8Gh4KHGh2cy4xcTN3UW9ldlpQeERZUTlWcko2QXhsUk8" \
curl --header "X-Vault-Token: myroot" \
--request GET \
http://localhost:8200/v1/myengine/data/myspringapplication/staging