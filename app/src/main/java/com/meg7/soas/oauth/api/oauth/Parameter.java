package com.meg7.soas.oauth.api.oauth;

/**
 * Taken from
 * https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/model/Parameter.java
 */
public class Parameter {
    public final String key, value;

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String asUrlEncodedPair() {
        return OAuthHelper.encode(key).concat("=").concat(OAuthHelper.encode(value));
    }


}
