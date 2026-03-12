package com.example.demo.service;

import com.example.demo.config.ApnsConfig;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class ApnsJwtService {

    private final ApnsConfig apnsConfig;

    public ApnsJwtService(ApnsConfig apnsConfig){
        this.apnsConfig = apnsConfig;
    }

    /**
     * APNs için JWT token üretir.
     */
    public String getToken() throws Exception {
        ECPrivateKey privateKey = loadPrivateKey();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(apnsConfig.getKeyId())
                .build();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(apnsConfig.getTeamId())
                .issueTime(new Date())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claims);
        signedJWT.sign(new ECDSASigner(privateKey));
        return signedJWT.serialize();
    }

    private ECPrivateKey loadPrivateKey() throws Exception {
        String path = apnsConfig.getKeyPath();
        String content = Files.readString(Paths.get(path).toAbsolutePath().normalize());
        String base64 = content
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return (ECPrivateKey) kf.generatePrivate(spec);
    }
}
