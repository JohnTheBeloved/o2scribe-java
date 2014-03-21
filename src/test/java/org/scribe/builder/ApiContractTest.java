package org.scribe.builder;

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
import org.scribe.model.Verb;

/**
 * Test for Api Implementations.
 */
@RunWith(Parameterized.class)
public class ApiContractTest {
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
        UrlValidator urlValidator = new UrlValidator(new String[]{"http","https"});
        assertTrue("The access token endpoint breaches the provider Api, not a valid url", urlValidator.isValid(result));
    }
}
