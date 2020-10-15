package io.github.mths0x5f.keycloak.url;

import org.keycloak.models.KeycloakSession;
import org.keycloak.urls.HostnameProvider;
import org.keycloak.urls.HostnameProviderFactory;

public class ProxiedHostnameProviderFactory implements HostnameProviderFactory {

    @Override
    public HostnameProvider create(KeycloakSession session) {
        return new ProxiedHostnameProvider(session);
    }

    @Override
    public String getId() {
        return "default";
    }

}
