package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;

/**
 *
 */
public class GitHub extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://github.com/login/oauth/access_token";
    }

    @Override
    public TokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public Encoding getAccessTokenEncoding() { return Encoding.FORM; }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config, final String stateToken) {
        AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

        builder.setEndpoint(AUTHORIZE_URL)
                .setClientId(config.getApiKey())
                .setRedirectUrl(config.getCallback())
                .setScope(config.getScope())
                .setState(stateToken);
        return builder.build();
    }
}
