package com.bank;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;


public class GenerateSecretKey {
    @Test

    void generateSecret(){
        SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey= DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.println("The Secret Key : "+encodedKey);
    }
}
