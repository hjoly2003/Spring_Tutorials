// See https://www.baeldung.com/vault#starting_vault_server
// What follow is taken from the output of the following cmd: ~ vault server -dev
// Unseal Key: SLlY4Hk6xGcKLTvRmAHa4UR48ucbL0bwREDYmBtAYjI=
// export VAULT_ADDR='http://127.0.0.1:8200'
// export VAULT_TOKEN="hvs.j3qdu9TtwzeuhD675o8D9W0U"

storage "file" {
  path = "./vault-data"
}
listener "tcp" {
  address = "127.0.0.1:8200"
  tls_cert_file = "./src/test/vault-config/localhost.cert"
  tls_key_file = "./src/test/vault-config/localhost.key"
}