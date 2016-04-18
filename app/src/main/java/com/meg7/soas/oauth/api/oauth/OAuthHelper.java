package com.meg7.soas.oauth.api.oauth;

import android.util.Base64;

import com.meg7.soas.oauth.api.ApiEndPoints;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

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
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
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
            OAUTH_VERSION = "oauth_version", VERSION_1 = "1.0", OAUTH = "OAuth ",
            OAUTH_SIGNATURE = "oauth_signature", OAUTH_CALLBACK = "oauth_callback", OAUTH_TOKEN = "oauth_token", OAUTH_VERIFIER = "oauth_verifier";

    //http://oauth.googlecode.com/svn/code/javascript/example/signature.html
    //https://dev.twitter.com/oauth/overview/creating-signatures
    public static String generateRequestTokenHeader(String consumerKey, String consumerSecret, String callbackUrl) {
        //remember to keep the sequence
        ArrayList<Parameter> params = new ArrayList<>();
        String httpMethod = POST;
        String postUrl = encode(ApiEndPoints.REQUEST_TOKEN_URL);

        // if callback url is not given, while authenticating, it may point the user to pin verification after authorization
        params.add(new Parameter(OAUTH_CALLBACK, callbackUrl));
        params.add(new Parameter(OAUTH_CONSUMER_KEY, consumerKey));
        String[] timeStampNNonce = getTimeStampNNonce();
        params.add(new Parameter(OAUTH_NONCE, timeStampNNonce[1]));
        params.add(new Parameter(OAUTH_SIGNATURE_METHOD, HMAC__SHA1));
        params.add(new Parameter(OAUTH_TIMESTAMP, timeStampNNonce[0]));
        params.add(new Parameter(OAUTH_VERSION, VERSION_1));
        //all above are required to generate the signature

        String signatureBaseString = generateBaseString(httpMethod, postUrl, params);
        Timber.d("SignatureBaseString = %s", signatureBaseString);
        String signatureString = generateHMACSignature(signatureBaseString, consumerSecret, "");
        Timber.d("SignatureString = %s", signatureString);
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
            return doSignWithSHA1(signatureBaseString, encode(consumerSecret) + "&" + encode(tokenSecret));
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate signature");
        }
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


    public static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
    public static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

    public static String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            try {
                return URLDecoder.decode(matcher.group(1), UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Response body is incorrect. Can't extract token and secret from this: '" + response + "'", null);

    }


    public static String generateAuthorizationUrl(String requestToken) {
        String authenticationUrl = ApiEndPoints.AUTHENTICATE_URL + "?oauth_token=" + requestToken;
        Timber.d("AuthenticationURL = %s", authenticationUrl);
        return authenticationUrl;
    }


    //For generation of access token
    public static String generateAccessToken(Token requestToken, String authVerifier, String consumerKey, String consumerSecret) {
        ArrayList<Parameter> params = new ArrayList<>();
        String httpMethod = POST;
        String postUrl = encode(ApiEndPoints.ACCESS_TOKEN_URL);
        //remember the sequence which must be in alphabetical order of encoded key
        params.add(new Parameter(OAUTH_CONSUMER_KEY, consumerKey));
        String[] timeStampNNonce = getTimeStampNNonce();
        params.add(new Parameter(OAUTH_NONCE, timeStampNNonce[1]));
        params.add(new Parameter(OAUTH_SIGNATURE_METHOD, HMAC__SHA1));
        params.add(new Parameter(OAUTH_TIMESTAMP, timeStampNNonce[0]));
        params.add(new Parameter(OAUTH_TOKEN, requestToken.token));
        params.add(new Parameter(OAUTH_VERIFIER, authVerifier));
        params.add(new Parameter(OAUTH_VERSION, VERSION_1));

        String signatureBaseString = generateBaseString(httpMethod, postUrl, params);
        Timber.d("SignatureBaseString = %s", signatureBaseString);
        String signatureString = generateHMACSignature(signatureBaseString, consumerSecret, requestToken.tokenSecret);
        Timber.d("SignatureString = %s", signatureString);
        params.add(new Parameter(OAUTH_SIGNATURE, signatureString));
        return generateHeaderString(params);
    }


    public static final String INCLUDE_ENTITIES = "include_entities";
    public static final String STATUS = "status";

    public static String generateStatusPostHeaderString(Token accessToken, String consumerKey, String consumerSecret, String status) {
        ArrayList<Parameter> params = new ArrayList<>();
        String httpMethod = POST;
        String postUrl = encode(ApiEndPoints.TWEET_POST_URL);

        params.add(new Parameter(INCLUDE_ENTITIES, "true"));
        params.add(new Parameter(OAUTH_CONSUMER_KEY, consumerKey));
        String[] timeStampNNonce = getTimeStampNNonce();
        params.add(new Parameter(OAUTH_NONCE, timeStampNNonce[1]));
        params.add(new Parameter(OAUTH_SIGNATURE_METHOD, HMAC__SHA1));
        params.add(new Parameter(OAUTH_TIMESTAMP, timeStampNNonce[0]));
        params.add(new Parameter(OAUTH_TOKEN, accessToken.token));
        params.add(new Parameter(OAUTH_VERSION, VERSION_1));
        params.add(new Parameter(STATUS, status));

        String signatureBaseString = generateBaseString(httpMethod, postUrl, params);
        //signatureBaseString ="POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
        Timber.d("SignatureBaseString = %s", signatureBaseString);
        String signatureString = generateHMACSignature(signatureBaseString, consumerSecret, accessToken.tokenSecret);
        Timber.d("SignatureString = %s", signatureString);
        params.add(new Parameter(OAUTH_SIGNATURE, signatureString));
        return generateHeaderString(params);

    }

    //All the signing test
    public static String doSignWithSHA1(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF_8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF_8));
        return Base64.encodeToString(bytes, Base64.CRLF).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }


    //MD5 hash generation
    public static String generateMD5Hash(String textToBeHashed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            //here encoding doesn't seem to create an effect
            //messageDigest.update(textToBeHashed.getBytes(UTF_8));
            messageDigest.update(textToBeHashed.getBytes());
            byte[] messageDigestByte = messageDigest.digest();
            StringBuffer MD5Hash = new StringBuffer();
            String h;
            for (int i = 0; i < messageDigestByte.length; ++i) {
                h = Integer.toHexString((0xFF & messageDigestByte[i]) | 0x100).substring(1, 3);
                MD5Hash.append(h);
            }
            return MD5Hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't generate MD5 hash for " + textToBeHashed);
        }
    }

    public static String generateMD5HashWithSalt(String textToBeHashed) {
        String salt = "&#@";
        return generateMD5Hash(textToBeHashed.concat(salt));
    }

    //SHA-1 has generation
    //http://stackoverflow.com/questions/5757024/make-sha1-encryption-on-android
    public static String generateSHA1Hash(String textToBeHashed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(textToBeHashed.getBytes());
            byte[] sha1hash = messageDigest.digest();
            return getHexString(sha1hash);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't generate SHA-1 hash for " + textToBeHashed);
        }
    }

    public static String generateSHA256Hash(String textToBeHashed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            messageDigest.update(textToBeHashed.getBytes());
            byte[] sha256Hash = messageDigest.digest();
            return getHexString(sha256Hash);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't generate SHA-256 has for " + textToBeHashed);
        }
    }

    private static String getHexString(byte[] data) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            stringBuffer.append(
                    Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    public static String getBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.CRLF).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

}
