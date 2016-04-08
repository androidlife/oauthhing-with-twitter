package com.meg7.soas.oauth.api.oauth;

import android.util.Base64;

import com.meg7.soas.oauth.api.ApiEndPoints;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Shamelessly copied methods and techniques from
 * https://github.com/scribejava/scribejava
 * BTW it is an awesome library for making any OAuth Request.
 * The reason, Scribe is taken is that , by studying Scribe and its process,
 * one can get clear understanding of how OAuth works
 * https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/utils/OAuthEncoder.java
 */
public class OAuthHelper {

    /**
     * ####
     * Methods to convert params to URL encoded form
     */
    private static final String UTF_8 = "UTF-8";
    private static final Map<String, String> ENCODING_RULES;
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String PARAM_SEPARATOR = ", ";

    static {
        Map<String, String> rules = new HashMap<String, String>();
        rules.put("*", "%2A");
        rules.put("+", "%20");
        rules.put("%7E", "~");
        ENCODING_RULES = Collections.unmodifiableMap(rules);
    }

    public static String encode(String plain) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(plain, UTF_8);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Charset not found while encoding string: " + UTF_8, uee);
        }
        for (Map.Entry<String, String> rule : ENCODING_RULES.entrySet()) {
            encoded = applyRule(encoded, rule.getKey(), rule.getValue());
        }
        return encoded;
    }


    private static String applyRule(String encoded, String toReplace, String replacement) {
        return encoded.replaceAll(Pattern.quote(toReplace), replacement);
    }

    /**
     * ####
     */

    //Follow OAuth specifications to know more about these value http://oauth.net/core/1.0/#encoding_parameters
    public static final String HTTP_METHOD = "httpMethod", POST = "POST", URL = "url",
            OAUTH_CONSUMER_KEY = "oauth_consumer_key", OAUTH_NONCE = "oauth_nonce",
            OAUTH_SIGNATURE_METHOD = "oauth_signature_method", HMAC__SHA1 = "HMAC-SHA1",
            OAUTH_TIMESTAMP = "oauth_timestamp",
            OAUTH_VERSION = "oauth_version", VERSION_1 = "1.0", OAUTH = "OAuth ", OAUTH_SIGNATURE = "oauth_signature", OAUTH_CALLBACK = "oauth_callback";

    //http://oauth.googlecode.com/svn/code/javascript/example/signature.html
    //https://dev.twitter.com/oauth/overview/creating-signatures
    public static String generateRequestTokenHeader(String consumerKey, String consumerSecret, String callbackUrl) {
        //remember to keep the sequence
        ArrayList<Parameter> params = new ArrayList<>();
        String httpMethod = POST;
        String postUrl = ApiEndPoints.REQUEST_TOKEN_URL;

        // if callback url is not given, while authenticating, it may point the user to pin verification after authorization
        params.add(new Parameter(OAUTH_CALLBACK, ApiEndPoints.CALLBACK_URL));
        params.add(new Parameter(OAUTH_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_KEY));
        String[] timeStampNNonce = getTimeStampNNonce();
        params.add(new Parameter(OAUTH_NONCE, timeStampNNonce[1]));
        params.add(new Parameter(OAUTH_SIGNATURE_METHOD, HMAC__SHA1));
        params.add(new Parameter(OAUTH_TIMESTAMP, timeStampNNonce[0]));
        params.add(new Parameter(OAUTH_VERSION, VERSION_1));
        //all above are required to generate the signature

        String signatureBaseString = generateBaseString(httpMethod, postUrl, params);
        String signatureString = generateHMACSignature(signatureBaseString, ApiEndPoints.TWITTER_CONSUMER_SECRET, "");
        //now time to generate Authorization header string
        params.add(new Parameter(OAUTH_SIGNATURE, signatureString));
        return generateHeaderString(params);
    }

    public static String generateHeaderString(ArrayList<Parameter> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OAUTH);
        for (Parameter param : params) {
            if (stringBuilder.length() > OAUTH.length())
                stringBuilder.append(PARAM_SEPARATOR);
            stringBuilder.append(String.format("%s=\"%s\"", param.key, encode(param.value)));
        }
        return stringBuilder.toString();
    }

    /**
     * HMAC Signature generation
     * ####
     */
    //https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/services/HMACSha1SignatureService.java
    public static String generateHMACSignature(String signatureBaseString, String consumerSecret, String tokenSecret) {
        try {
            return doSign(signatureBaseString, encode(consumerSecret) + "&" + encode(tokenSecret));
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate signature");
        }
    }

    private static String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF_8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF_8));
        return Base64.encodeToString(bytes, Base64.CRLF).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    public static String generateBaseString(String verb, String url, ArrayList<Parameter> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < params.size(); ++i) {
            stringBuilder.append('&').append(params.get(i).asUrlEncodedPair());
        }
        //consumer secret at last
        String appendedParam = encode(stringBuilder.toString().substring(1));
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, appendedParam);
    }


    /**
     * ####
     */

    //https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/services/TimestampServiceImpl.java
    private static final Random random = new Random();

    public static String[] getTimeStampNNonce() {
        String[] values = new String[2];
        Long ts = System.currentTimeMillis() / 1000;
        //timeStamp
        values[0] = String.valueOf(ts);
        //nonce ( must be a random number)
        values[1] = String.valueOf(ts + random.nextInt());
        return values;
    }

}
