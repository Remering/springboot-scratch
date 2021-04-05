package com.github.remering.scratch.springboot.config;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
@EnableConfigurationProperties(RsaConfigurationProperties.class)
public class RsaConfiguration {

    private final RsaConfigurationProperties properties;

    private final char[] jksPasswordCharArray;

    public RsaConfiguration(RsaConfigurationProperties properties) {
        this.properties = properties;
        jksPasswordCharArray = properties.getJksPassword().toCharArray();
    }

    @Bean
    @SneakyThrows
    public KeyStore keyStore() {
        val keyStore = KeyStore.getInstance("JKS");
        keyStore.load(properties.getJksLocation().getInputStream(), jksPasswordCharArray);
        return keyStore;
    }

    @Bean
    public RSAPublicKey publicKey(KeyStore keyStore) throws KeyStoreException {
        return (RSAPublicKey) keyStore.getCertificate(properties.getJksAlias()).getPublicKey();
    }

    @Bean
    public RSAPrivateKey privateKey(KeyStore keyStore) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        return (RSAPrivateKey) keyStore.getKey(properties.getJksAlias(), jksPasswordCharArray);
    }
}
