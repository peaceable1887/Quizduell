package gruppe_b.quizduell.common.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Objekt zum Binden des private und public rsa keys für die jwt.
 * 
 * @author Christopher Burmeister
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}