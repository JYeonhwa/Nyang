package com.nyang.nyangService.service;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import com.nyang.nyangService.dto.AppleUserDto;
import com.nyang.nyangService.dto.UserResponse;
//import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.security.ec.ECPrivateKeyImpl;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.text.ParseException;
import java.util.*;

@Service
public class AppleService {
    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;
    @Value("${apple.login.key}")
    private String APPLE_LOGIN_KEY;
    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;
    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;
    @Value("${apple.key.path}")
    private String APPLE_KEY_PATH;
    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    private LinkedHashMap<String, Object> data;

    public String getAppleLogin() {
        return APPLE_AUTH_URL + "/auth/authorize" +
                "?client_id=" + APPLE_CLIENT_ID +
                "&redirect_url=" + APPLE_REDIRECT_URL +
                "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }

    public UserResponse.LoginSuccessDto getAppleInfo(String identityToken, String authorizationCode) throws Exception {
        if (identityToken == null || authorizationCode == null) throw new Exception("Failed get identity token or authorization code");

        String clientSecret = createClientSecret();
        String userId = "";
        String email  = "";
        String accessToken = "";
        String refreshToken = "";
        Long expireTime;

        try {
            //헤더 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            //파라미터 생성
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , APPLE_CLIENT_ID);
            params.add("client_secret", clientSecret);
            params.add("code"         , authorizationCode);
            params.add("redirect_uri" , APPLE_REDIRECT_URL);


            //http 구조 만들기
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
            //response 구조 만들기
            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_AUTH_URL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser(response.getBody());

            data = jsonParser.parseObject();

            accessToken = String.valueOf(data.get("access_token"));

            refreshToken = String.valueOf(data.get("refresh_token"));

            expireTime = Long.valueOf(String.valueOf(data.get("expires_in")));


        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        return UserResponse.LoginSuccessDto.builder()
                .type("Bearer")
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .refreshTokenExpirationTime(expireTime)
                .build();
    }

    private String createClientSecret() throws Exception {
        //apple login key로 JWT 만들기
        Date now = new Date();

        String jwts = Jwts.builder()
                .setHeaderParam("kid", APPLE_LOGIN_KEY)
                .setHeaderParam("alg", "ES256")
                .setIssuer(APPLE_TEAM_ID)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 3600000))
                .setAudience(APPLE_AUTH_URL)
                .setSubject(APPLE_CLIENT_ID)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();

        return jwts;
    }

    private byte[] getPrivateKey() throws Exception {

        byte[] content = null;
        File file = null;

        URL res = getClass().getResource(APPLE_KEY_PATH);

        if ("jar".equals(res.getProtocol())) {
            try {
                InputStream input = getClass().getResourceAsStream(APPLE_KEY_PATH);
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);

                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                out.close();
                file.deleteOnExit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            file = new File(res.getFile());
        }

        if (file.exists()) {
            try (FileReader keyReader = new FileReader(file);
                 PemReader pemReader = new PemReader(keyReader))
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("File " + file + " not found");
        }

        return content;
    }

    public String getUserData() {
        JSONObject payload = null;
        //ID TOKEN을 통해 회원 고유 식별자 받기
//            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(data.get("id_token")));
//            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
        String decodedJWT = new String(Base64.getUrlDecoder().toString());
        JsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonArray = jsonParser.parseMap(decodedJWT);
//            ObjectMapper objectMapper = new ObjectMapper();
//            payload = objectMapper.readValue(decodedJWT().toJSONString(), JSONObject.class);

        return String.valueOf(payload.get("sub"));
    }

}
