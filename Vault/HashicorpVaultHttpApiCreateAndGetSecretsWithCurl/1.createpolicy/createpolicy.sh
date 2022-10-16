#!/usr/bin/env bash
cd $(dirname $0)

# In the following curl call, the --data payload is a json litteral whose "policy" property must be a stringified version of mypolicy.hcl file. See https://github.com/hashicorp/vault/issues/582#issuecomment-390460260

# See https://stackoverflow.com/a/43967678/904231 on how to generate the stringified version of the *.hcl file
policy_string=$(cat mypolicy.hcl | sed 's/$/\\n/' | tr -d '\n' | sed 's/"/\\\"/g')

# See documentation at https://learn.hashicorp.com/tutorials/vault/policies#create-a-policy
# See API documentation at https://www.vaultproject.io/api-docs/system/policies#create-update-acl-policy
curl --header "X-Vault-Token: myroot" \
--request POST \
--data "{\"policy\":\"${policy_string}\"}" \
http://localhost:8200/v1/sys/policies/acl/mypolicy