package pl.uam.wmi.niezbednikstudenta.services;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.exceptions.LoginException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class AuthorizeService {

    @Value("${base.uri.usos}")
    private String baseUri;

    @Value("${consumer.key}")
    private String consumerKey;

    @Value("${consumer.secret}")
    private String consumerSecret;

    private static final String ENC = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static Base64 base64 = new Base64();

    public JSONObject callRequestToken(String oauth_callback) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("oauth_callback", oauth_callback));
        queryParams.add(new BasicNameValuePair("oauth_consumer_key", consumerKey));
        queryParams.add(new BasicNameValuePair("oauth_nonce", "" + (int) (Math.random() * 100000000)));
        queryParams.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        queryParams.add(new BasicNameValuePair("oauth_timestamp", "" + (System.currentTimeMillis() / 1000)));
        queryParams.add(new BasicNameValuePair("oauth_version", "1.0"));
        queryParams.add(new BasicNameValuePair("scopes", "offline_access"));
        String oauth_signature = createSignature("GET", URLEncoder.encode(baseUri + "/oauth/request_token", ENC),
                URLEncoder.encode(URLEncodedUtils.format(queryParams, ENC), ENC), "");
        queryParams.add(new BasicNameValuePair("oauth_signature", oauth_signature));
        String url = baseUri + "/oauth/request_token?" + URLEncodedUtils.format(queryParams, ENC);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String tokens = response.body();
        JSONObject linkToAuthorize = new JSONObject();
        linkToAuthorize.put("loginUrl", baseUri + "/oauth/authorize?" + tokens);
        return linkToAuthorize;
    }

    public JSONObject callAccessToken(String oauth_token, String oauth_token_secret, String oauth_verifier) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException, LoginException {

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("oauth_consumer_key", consumerKey));
        queryParams.add(new BasicNameValuePair("oauth_nonce", "" + (int) (Math.random() * 100000000)));
        queryParams.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        queryParams.add(new BasicNameValuePair("oauth_timestamp", "" + (System.currentTimeMillis() / 1000)));
        queryParams.add(new BasicNameValuePair("oauth_token", oauth_token));
        queryParams.add(new BasicNameValuePair("oauth_verifier", oauth_verifier));
        queryParams.add(new BasicNameValuePair("oauth_version", "1.0"));
        String oauth_signature = createSignature("GET", URLEncoder.encode(baseUri + "/oauth/access_token", ENC),
                URLEncoder.encode(URLEncodedUtils.format(queryParams, ENC), ENC), oauth_token_secret);
        queryParams.add(new BasicNameValuePair("oauth_signature", oauth_signature));
        String url = baseUri + "/oauth/access_token?" + URLEncodedUtils.format(queryParams, ENC);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {

            String tokensForConvert = response.body();
            String tokensDivided[] = tokensForConvert.split("&");
            int beginSubstring = tokensDivided[0].indexOf("=") + 1;
            JSONObject tokens = new JSONObject();
            tokens.put("oauthToken", tokensDivided[0].substring(beginSubstring, tokensDivided[0].length()));
            beginSubstring = tokensDivided[1].indexOf("=") + 1;
            tokens.put("oauthTokenSecret", tokensDivided[1].substring(beginSubstring, tokensDivided[1].length()));


            return tokens;
        } else
            throw new LoginException("Error during login. Status code: " + response.statusCode());
    }

    public String logOut(String oauth_token, String oauth_token_secret) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("oauth_consumer_key", consumerKey));
        queryParams.add(new BasicNameValuePair("oauth_nonce", "" + (int) (Math.random() * 100000000)));
        queryParams.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        queryParams.add(new BasicNameValuePair("oauth_timestamp", "" + (System.currentTimeMillis() / 1000)));
        queryParams.add(new BasicNameValuePair("oauth_token", oauth_token));
        queryParams.add(new BasicNameValuePair("oauth_version", "1.0"));
        String oauth_signature = createSignature("GET", URLEncoder.encode(baseUri + "/oauth/revoke_token", ENC),
                URLEncoder.encode(URLEncodedUtils.format(queryParams, ENC), ENC), oauth_token_secret);
        queryParams.add(new BasicNameValuePair("oauth_signature", oauth_signature));
        String url = baseUri + "/oauth/revoke_token?" + URLEncodedUtils.format(queryParams, ENC);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String createSignature(String httpMethod, String url, String params, String secretToken) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        StringBuilder base = new StringBuilder();
        base.append(httpMethod);
        base.append("&");
        base.append(url);
        base.append("&");
        base.append(params);

        byte[] keyBytes = (consumerSecret + "&" + secretToken).getBytes(ENC);

        SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);

        // encode it, base64 it, change it to string and return.
        return new String(base64.encode(mac.doFinal(base.toString().getBytes(
                ENC))), ENC).trim();
    }

}
