package org.scribe.utils;

import org.junit.*;

import org.scribe.BasePrivateConstructorTest;

public class PreconditionsTest extends BasePrivateConstructorTest
{
    protected Class getClazz(){
        return Preconditions.class;
    }
    private static final String ERROR_MSG = "";
    private static final String TEST_DOMAIN = "www.example.com";

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullObjects()
    {
    Preconditions.checkNotNull(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullStrings()
    {
    Preconditions.checkEmptyString(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyStrings()
    {
    Preconditions.checkEmptyString("", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSpacesOnlyStrings()
    {
    Preconditions.checkEmptyString("               ", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidUrls()
    {
    Preconditions.checkValidUrl("this/is/not/a/valid/url", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullUrls()
    {
    Preconditions.checkValidUrl(null, ERROR_MSG);
    }

    @Test
    public void shouldAllowValidUrls()
    {
    Preconditions.checkValidUrl("http://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test
    public void shouldAllowSSLUrls()
    {
    Preconditions.checkValidUrl("https://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test
    public void shouldAllowSpecialCharsInScheme()
    {
    Preconditions.checkValidUrl("custom+9.3-1://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test
    public void shouldAllowNonStandarProtocolsForAndroid()
    {
    Preconditions.checkValidUrl("x-url-custom://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowStrangeProtocolNames()
    {
    Preconditions.checkValidUrl("$weird*://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowUnderscoreInScheme()
    {
    Preconditions.checkValidUrl("http_custom://" + TEST_DOMAIN, ERROR_MSG);
    }

    @Test
    public void shouldAllowOutOfBandAsValidCallbackValue()
    {
    Preconditions.checkValidOAuthCallback("oob", ERROR_MSG);
    }

    @Test
    public void shouldAllowValidCallbackValue()
    {
        Preconditions.checkValidOAuthCallback("http://" + TEST_DOMAIN, ERROR_MSG);
    }
}
