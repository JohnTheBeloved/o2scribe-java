package org.scribe.builder;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.reflections.Reflections;
import org.scribe.builder.api.Api;
import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.processors.extractors.TokenExtractor;

/**
 * Test for Api Implementations.
 */
@RunWith(Parameterized.class)
public class ApiContractTest {
    private UrlValidator urlValidator = new UrlValidator(new String[]{"http","https"});
    private Api toTest;

    public ApiContractTest(Api impl, String name){
        this.toTest = impl;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> generateData() throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections("org.scribe.builder.api");
        Set<Class<? extends Api>> allSubTypes = reflections.getSubTypesOf(Api.class);

        List<Object[]> allConcreteSubTypes = new ArrayList<Object[]>();
        for (Class<? extends Api> candidate : allSubTypes) {
            if(!Modifier.isAbstract(candidate.getModifiers())){
                allConcreteSubTypes.add(new Object[]{candidate.newInstance(), candidate.getSimpleName()});
            }
        }

        return allConcreteSubTypes;
    }

    @Test
    public void validAccessTokenVerb(){
        Verb result = toTest.getAccessTokenVerb();

        assertTrue("The returned access token verb breaches the provider Api contract", Arrays.asList(Verb.values()).contains(result));
    }

    @Test
    public void ifHasGrantTypeReturnsValidGrantType(){
        if(toTest.hasGrantType()){
            assertTrue("getGrantType Breaches the provider Api contract, " +
                    "If you specify the api uses grant_type you must provide a valid grant type", StringUtils.isNotBlank(toTest.getGrantType()));
        }
    }

    @Test
    public void validAccessTokenEncoding(){
        Encoding result = toTest.getAccessTokenEncoding();

        assertTrue("The returned access token encoding breaches the provider Api contract", Arrays.asList(Encoding.values()).contains(result));
    }

    @Test
    public void isAccessTokenEndpointValidURL(){
        String result = toTest.getAccessTokenEndpoint();

        assertTrue("The access token endpoint breaches the provider Api, not a valid url", urlValidator.isValid(result));
    }

    @Test
    public void createValidAuthorizationURL(){
        OAuthConfig defaultConfig = new OAuthConfig("key", "secret", "http://www.example.com", null, null, null);
        String authUrl = toTest.getAuthorizationUrl(defaultConfig, "state");

        assertTrue("The access token endpoint breaches the provider Api contract, not a valid url", urlValidator.isValid(authUrl));
    }

    @Test
    public void validAccessTokenExtractor(){
        TokenExtractor extractor = toTest.getAccessTokenExtractor();

        assertNotNull("The Token extractor breaches the provider api contract, must be implemented", extractor);
    }

    @Test
    public void createService(){
        OAuthConfig defaultConfig = new OAuthConfig("key", "secret", "http://www.example.com", null, null, null);
        OAuthService service = toTest.createService(defaultConfig);

        assertNotNull("Oauth Service cannot be null this breaches the Api contract", service);
    }
}
