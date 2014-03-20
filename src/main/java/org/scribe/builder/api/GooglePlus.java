package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;
import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;

/**
 * The Google+ Api Oauth2 provider implementation.
 */
public class GooglePlus extends DefaultApi20 {
    private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
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
    public boolean hasGrantType() { return true; }

    @Override
    public String getGrantType() {
        return "authorization_code";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig oAuthConfig, final String state) {
        AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

        builder.setEndpoint(AUTHORIZATION_URL)
                .setClientId(oAuthConfig.getApiKey())
                .setRedirectUrl(oAuthConfig.getCallback())
                .setScope(oAuthConfig.getScope())
                .setState(state)
                .setResponseType(OAuthConstants.CODE);
        return builder.build();
    }
}
