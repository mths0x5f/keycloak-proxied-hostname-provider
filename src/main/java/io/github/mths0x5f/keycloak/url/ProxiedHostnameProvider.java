package io.github.mths0x5f.keycloak.url;

import org.keycloak.models.KeycloakSession;
import org.keycloak.urls.HostnameProvider;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

public class ProxiedHostnameProvider implements HostnameProvider {

    private static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT = "X-Forwarded-Port";
    private static final String X_FORWARDED_PREFIX = "X-Forwarded-Prefix";

    private final KeycloakSession session;

    public ProxiedHostnameProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public String getScheme(UriInfo originalUriInfo) {
        String proxiedScheme = session.getContext().getRequestHeaders().getHeaderString(X_FORWARDED_PROTO);
        String scheme = originalUriInfo.getBaseUri().getScheme();
        return "http".equalsIgnoreCase(proxiedScheme) || "https".equalsIgnoreCase(proxiedScheme) ? proxiedScheme : scheme;
    }

    @Override
    public String getHostname(UriInfo originalUriInfo) {
        String proxiedHost = session.getContext().getRequestHeaders().getHeaderString(X_FORWARDED_HOST);
        String host = originalUriInfo.getBaseUri().getHost();
        try {
            return new URI(null, proxiedHost != null ? proxiedHost : host, null, null).getHost();
        } catch (URISyntaxException e) {
            return host;
        }
    }

    @Override
    public int getPort(UriInfo originalUriInfo) {
        String proxiedPort = session.getContext().getRequestHeaders().getHeaderString(X_FORWARDED_PORT);
        int port = originalUriInfo.getBaseUri().getPort();
        proxiedPort = getScheme(originalUriInfo).equals("http") && "80".equals(proxiedPort) ? "-1" : proxiedPort;
        proxiedPort = getScheme(originalUriInfo).equals("https") && "443".equals(proxiedPort) ? "-1" : proxiedPort;
        try {
            return proxiedPort != null ? Integer.parseInt(proxiedPort) : port;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getContextPath(UriInfo originalUriInfo) {
        String proxiedPathPrefix = session.getContext().getRequestHeaders().getHeaderString(X_FORWARDED_PREFIX);
        String contextPath = originalUriInfo.getBaseUri().getPath();
        return URI.create((proxiedPathPrefix == null ? "" : proxiedPathPrefix) + contextPath).normalize().toString();
    }

}
