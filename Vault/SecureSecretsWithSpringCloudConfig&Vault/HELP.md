# Getting Started

Example taken from [Spring Cloud Config with Secrets Encryption](https://developer.okta.com/blog/2020/05/04/spring-vault)

## Otka Web Application Configuration
*   Issuer URI: https://dev-08977901.okta.com/oauth2/default
*   New app integration:
    * Sign-in method: OIDC - OpenID Connect
    * Application type: Web Application
    * Login redirect URI: http://192.168.0.127:8080/login/oauth2/code/okta
    * Logout redirect URI: http://192.168.0.127:8080
    * Base URIs: http://192.168.0.127:8080/
    * Grant type
        * Client Credentials
        * Authorization Code
        * Refresh Token

## Resulting Otka Application Specs
* Client ID: 0oa157u522Epu1Sm45d7
* Client secret: tWE1J9e6BVRqloOf_1bDKq6JdpiRFnQpAdYdmNpz
* Okta Domain: dev-08977901.okta.com
