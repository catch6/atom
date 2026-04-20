package cn.mindit.atom.jwt;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Catch
 * @since 2023-06-06
 */
public class GenerateKey {

    public static void main(String[] args) {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);
        System.out.println(Base64.getEncoder().encodeToString(sharedSecret));
    }

}
