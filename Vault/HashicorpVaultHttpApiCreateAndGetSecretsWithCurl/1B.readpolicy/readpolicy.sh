#!/usr/bin/env bash
cd $(dirname $0)

# See documentation at https://learn.hashicorp.com/tutorials/vault/policies#display-a-policy
curl --header "X-Vault-Token: myroot" \
--request GET \
http://localhost:8200/v1/sys/policies/acl/mypolicy