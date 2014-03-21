/*
Copyright 2010 Pablo Fernandez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.scribe.model;

/**
 * This class contains OAuth constants, used project-wide.
 *
 * @author Pablo Fernandez
 */
public enum OAuthConstants {
    TIMESTAMP("oauth_timestamp"),
    SIGNATURE("oauth_signature"),
    CONSUMER_KEY("oauth_consumer_key"),
    CALLBACK("oauth_callback"),
    NONCE("oauth_nonce"),
    REALM("realm"),
    TOKEN("oauth_token"),
    OUT_OF_BAND("oob"),
    SCOPE("scope"),

    //OAuth 2.0
    ACCESS_TOKEN("access_token"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    STATE("state"),
    RESPONSE_TYPE("response_type"),
    CODE("code"),
    GRANT_TYPE("grant_type");

    private String paramName;

    private OAuthConstants(final String inParamName) {

        this.paramName = inParamName;
    }

    public String getParamName() {
        return paramName;
    }
}
