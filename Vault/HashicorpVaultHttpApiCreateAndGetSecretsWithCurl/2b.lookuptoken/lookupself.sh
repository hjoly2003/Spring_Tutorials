#!/usr/bin/env bash
cd $(dirname $0)

# An orphan token has no parent. This is to avoid the token beeing deleted as a normal token would on its parent deletion.
# See documentation at https://www.vaultproject.io/api-docs/auth/token#renew-a-token-self
# The vault token is obtained from the result of running the createtoken.sh under "auth.client_token".
curl --header "X-Vault-Token: hvs.CAESIIEJNCvhDmZY9xhoflEDzthWsI01izxxMJhswFZrF_U8Gh4KHGh2cy4xcTN3UW9ldlpQeERZUTlWcko2QXhsUk8" \
--request GET \
http://localhost:8200/v1/auth/token/lookup-self