package com.nyang.nyangService.service;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import com.nyang.nyangService.dto.AppleUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nyang.nyangService.dto.AppleUserDto;
import com.nyang.nyangService.dto.IdentityToken;
import com.nyang.nyangService.dto.Keys;
import com.nyang.nyangService.dto.UserResponse;
//import io.jsonwebtoken.JwtBuilder;
import com.nyang.nyangService.repository.UserRepository;
import com.nyang.nyangService.utils.HttpClientUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.*;

@Slf4j
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

    private final ResourceLoader resourceLoader;

    private LinkedHashMap<String, Object> data;

    public AppleService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//    public String getAppleLogin() {
//        return APPLE_AUTH_URL + "/auth/authorize" +
//                "?client_id=" + APPLE_CLIENT_ID +
//                "&redirect_url=" + APPLE_REDIRECT_URL +
//                "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
//    }

    public LinkedHashMap<String, Object> getAppleInfo(String identityToken, String authorizationCode) throws Exception {
        log.info("getAppleInfo 서비스 시작");
        if (identityToken == null || authorizationCode == null) throw new Exception("Failed get identity token or authorization code");


        String clientSecret = createClientSecret(identityToken);
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
            params.add("code"         , authorizationCode.trim());
//            params.add("redirect_uri" , APPLE_REDIRECT_URL);
            log.info("getAppleInfo 서비스 헤더 및 파라미터 생성 완");
            log.info(params.toString());


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
            log.info("getAppleInfo 서비스 restapi 완");

            JSONParser jsonParser = new JSONParser(response.getBody());

            data = jsonParser.parseObject();



            log.info("getAppleInfo 서비스 jsonparse 완");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception: " + e.getMessage(), e);
            throw new Exception("API call or userSave failed");
        }
        return data;

    }

//    public void userSave(String id_token, String accessToken, String refreshToken) {
//        userSave(id_token, accessToken, refreshToken);
//    }

    public String createClientSecret(String identityToken) throws Exception {
        log.info("createClientSecret 서비스 시작");

        if (verifyIdentityToken(identityToken)) {

            //apple login key로 JWT 만들기
            Date now = new Date();
//
//            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(APPLE_CLIENT_ID).build();
//            JWTClaimsSet claimsSet = new JWTClaimsSet();
//
//            claimsSet.setIssuer(APPLE_TEAM_ID);
//            claimsSet.setIssueTime(now);
//            claimsSet.setExpirationTime(new Date(now.getTime() + 7948800));
//            claimsSet.setAudience(APPLE_AUTH_URL);
//            claimsSet.setSubject(APPLE_CLIENT_ID);
//            log.info("createClientSecret 헤더랑 클레임셋 완성");
//
//
//            SignedJWT jwt = new SignedJWT(header, claimsSet);
//            try {
//                log.info("createClientSecret jwt와 ECPrivatekey 만들기 시작");
//
//                //            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl();
//                JWSSigner jwsSigner = new ECDSASigner(getPrivateKey(APPLE_KEY_PATH).getS());
//                jwt.sign(jwsSigner);
//                log.info("createClientSecret jwt 사인 완료");
//
//            } catch (JOSEException e) {
//                e.printStackTrace();
//            }

        String jwt = Jwts.builder()
                .setHeaderParam("kid", APPLE_LOGIN_KEY)
                .setHeaderParam("alg", "ES256")
                .setIssuer(APPLE_TEAM_ID)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 7948800))
                .setAudience(APPLE_AUTH_URL)
                .setSubject(APPLE_CLIENT_ID)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey(APPLE_KEY_PATH))
                .compact();
            log.info("jwt 완성");
            return jwt;
        }
        return "createClientSecret is weird...";
    }

    private ECPrivateKey getPrivateKey(String path) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("getPrivateKey 시작");
        log.info(path);


//        Resource resource = resourceLoader.getResource(path);
        ClassPathResource resource = new ClassPathResource(path);
        byte[] content = null;
        try (InputStream inputStream = resource.getInputStream()) {
            log.info("pem 읽기 시작");
//            log.info(resource.getFilename());
//            FileReader keyReader = new FileReader();
//            log.info(keyReader.toString());
            PemReader pemReader = new PemReader(new InputStreamReader(inputStream));
            log.info(pemReader.toString());
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();
            log.info("pem 읽기 완료");
            log.info(path);
            log.info(content.toString());

        } catch (IOException e) {
            log.info(resource.toString());
//            log.info();
            log.error("IOException occurred while reading the PEM file: " + e.getMessage(), e);;
        }


        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);

        log.info("private key 완성");


        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public boolean verifyIdentityToken(String identityToken) {
        log.info("verifyIdentityToken 시작");
        log.info(identityToken);

        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();
            log.info(payload.toJSONObject().toJSONString());

            Date now = new Date(System.currentTimeMillis());
            if (!now.before(payload.getExpirationTime())) {
                log.info("f1");
                return false;
            }
            if (!"https://appleid.apple.com".equals(payload.getIssuer()) || !APPLE_CLIENT_ID.equals(payload.getAudience().get(0))) {
                log.info("f2");
                log.info(payload.getIssuer());
                log.info(payload.getAudience().get(0));
                return false;
            }
            if (verifyPublicKey(signedJWT)) {
                log.info("true");
                return true;
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean verifyPublicKey(SignedJWT signedJWT) {
        log.info("verifyPublickKey");
        //https://appleid.apple.com/auth/keys
        try {
            String publicKeys = HttpClientUtils.doGet("https://appleid.apple.com/auth/keys");
            log.info(publicKeys);
            ObjectMapper objectMapper = new ObjectMapper();
            Keys keys = objectMapper.readValue(publicKeys, Keys.class);
            for (IdentityToken key : keys.getKeys()) {
                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);

                if (signedJWT.verify(verifier)) {

                    log.info("true");

                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getUserData() {
        log.info("getUserData 시작");
        JSONObject payload = null;
        //ID TOKEN을 통해 회원 고유 식별자 받기
//            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(data.get("id_token")));
//            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
        String decodedJWT = new String(Base64.getUrlDecoder().toString());
        log.info(decodedJWT);
        JsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonArray = jsonParser.parseMap(decodedJWT);
//            ObjectMapper objectMapper = new ObjectMapper();
//            payload = objectMapper.readValue(decodedJWT().toJSONString(), JSONObject.class);
        log.info("jsonArray 완?");


        return String.valueOf(jsonArray.get("sub"));
    }

}
