package org.scribe.builder.authUrl;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.model.OAuthConstants;
import org.scribe.utils.OAuthEncoderUtils;
import org.scribe.utils.Preconditions;

/**
 *
 */
public class DefaultAuthUrlBuilder implements AuthUrlBuilder {
    /** Constant for the start of the query string. */
    private static final char INTIAL_PARAMETER_IDENTIFIER = '?';
    /** Constant for the equals char. */
    private static final char EQUALS = '=';
    /** Constant for the next parameter in the query string. */
    private static final char PARAMETETER_IDENTIFIER = '&';

    private String endpoint;
    private String clientId;
    private String redirectUrl;
    private String scope;
    private String state;
    private String responseType;

    @Override
    public AuthUrlBuilder setEndpoint(final String inEndpoint) {
        Preconditions.checkValidUrl(inEndpoint, "The endpoint should be a vaild url");
        this.endpoint = inEndpoint;
        return this;
    }

    @Override
    public AuthUrlBuilder setClientId(final String inClientId) {
        Preconditions.checkEmptyString(inClientId, "Client id is required");
        this.clientId = inClientId;
        return this;
    }

    @Override
    public AuthUrlBuilder setRedirectUrl(final String inRedirectUrl) {
        Preconditions.checkValidUrl(inRedirectUrl, "The redirect url should be a valid url");
        this.redirectUrl = inRedirectUrl;
        return this;
    }

    @Override
    public AuthUrlBuilder setScope(final String inScope) {
        this.scope = inScope;
        return this;
    }

    @Override
    public AuthUrlBuilder setState(final String inState) {
        this.state = inState;
        return this;
    }

    @Override
    public AuthUrlBuilder setResponseType(final String inResponseType) {
        this.responseType = inResponseType;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        Preconditions.checkValidUrl(endpoint, "A url endpoint is required.");
        Preconditions.checkEmptyString(clientId, "Client It is required");

        sb.append(endpoint)
                .append(INTIAL_PARAMETER_IDENTIFIER)
                .append(OAuthConstants.CLIENT_ID)
                .append(EQUALS)
                .append(clientId);

        addParameter(sb, OAuthConstants.REDIRECT_URI, OAuthEncoderUtils.encode(redirectUrl));

        addOptionalParameter(sb, OAuthConstants.SCOPE, scope);
        addOptionalParameter(sb, OAuthConstants.STATE, state);
        addOptionalParameter(sb, OAuthConstants.RESPONSE_TYPE, responseType);

        return sb.toString();
    }

    protected void addOptionalParameter(final StringBuilder sb, final String paramName, final String paramValue) {
        if (paramValue != null && !paramValue.isEmpty()) {
            addParameter(sb, paramName, paramValue);
        }
    }

    protected void addParameter(final StringBuilder sb, final String paramName, final String paramValue) {
        Preconditions.checkEmptyString(paramValue, paramName + " is required.");
        sb.append(PARAMETETER_IDENTIFIER)
                .append(paramName)
                .append(EQUALS)
                .append(paramValue);
    }


}
