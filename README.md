# Keycloak Proxied Hostname Provider

With this hostname provider, Keycloak will honor all X-Forwarded-* headers, specially the X-Forwarded-Prefix 
which is needed when Keycloak is proxied on a non-root base path. Setup `standalone.xml` as shown:

```xml
<spi name="hostname">
  <default-provider>proxied</default-provider>
</spi>
```
