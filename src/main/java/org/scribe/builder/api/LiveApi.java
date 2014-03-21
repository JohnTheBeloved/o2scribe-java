package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;

/**
 * The LiveApi Oauth2 provider implementation.
 */
public class LiveApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://oauth.live.com/authorize";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.live.com/token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config, final String state) {
        AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

        builder.setEndpoint(AUTHORIZE_URL)
                .setClientId(config.getApiKey())
                .setRedirectUrl(config.getCallback())
                .setScope(config.getScope())
                .setState(state)
                .setResponseType(OAuthConstants.CODE.getParamName());
        return builder.build();
    }

    @Override
    public TokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
