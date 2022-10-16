// This is the physical backend that Vault uses for storage.
storage "raft" {
  path    = "./vault/data"
  node_id = "node1"
}

// One or more listeners determine how Vault listens for API requests.
listener "tcp" {
  address     = "127.0.0.1:8200"
  tls_disable = "true"
}

// Specifies the address to advertise to route client requests.
api_addr = "http://127.0.0.1:8200"

// Indicates the address and port to be used for communication between the Vault nodes in a cluster.
cluster_addr = "https://127.0.0.1:8201"
ui = true

//Unseal Key 1: W3p40fmSlBMzP9ghCKjncqcqqZ+V1o3UCbQg0RK6Ax0Z
//Unseal Key 2: 01hXMk5P82+Up1kJ+A9f7K4eupZerPTYrbex1kthG6+q
//Unseal Key 3: 0UzW9tkIq+xaaBvM0M/Hxr6GLPlfyg8beJBZCXSbpy6h
//Unseal Key 4: hU7gY/oJqE0fBUxt5ZuGY4/NyWZro7mjjrtn5zTDQd5O
//Unseal Key 5: bSX98CPkREhrEulXZqTUR+3UHBnClagYUsRW4ZrpVZvN

//Initial Root Token: hvs.rozzVnD9axBhaarBxiGzDpEt

