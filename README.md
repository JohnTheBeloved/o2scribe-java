[![Build Status](https://travis-ci.org/set321go/o2scribe-java.svg?branch=master)](https://travis-ci.org/set321go/o2scribe-java)
[![Coverage Status](https://coveralls.io/repos/set321go/o2scribe-java/badge.png)](https://coveralls.io/r/set321go/o2scribe-java)

# Welcome to the home of O2Scribe , the simple OAuth2 Java lib!

This project started life as a fork of [scribe-java](https://github.com/fernandezpablo85/scribe-java) but due to the fragmentation of oauth2 as a result of the poor spec
the project has discontinued oauth2 support. This project aims to pick up where they left off. It is not intentionally compatible with scribe
and does not support oauth1 or oauth1a and has no intention of doing so in the future.

## Submitting a new provider

1. Your new provider needs to implement the [Api](src/main/java/org/scribe/builder/api/Api.java) Interface, most providers have common functionality
and extend [DefaultApi20](src/main/java/org/scribe/builder/api/DefaultApi20.java)
1. If your provider uses non standard parameters you will need to create a custom [AuthUrlBuilder](src/main/java/org/scribe/builder/AuthUrlBuilder.java)
the [DefaultAuthUrlBuilder](src/main/java/org/scribe/builder/authUrl/DefaultAuthUrlBuilder.java) will probably be sufficient for most providers.
1. Add a new example under the examples package
1. Ensure that [Checkstyles](http://checkstyle.sourceforge.net/) and [findBugs](http://findbugs.sourceforge.net/) are both passing. These are baked into
the default maven build.

## What, no....

* Twitter - they only offer a 1.0a authentication service, please take a look at [scribe-java](https://github.com/fernandezpablo85/scribe-java) for support

