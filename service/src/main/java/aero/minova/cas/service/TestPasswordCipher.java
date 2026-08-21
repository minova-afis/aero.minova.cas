package aero.minova.cas.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Reversible AES-256-GCM encryption for the test-user passwords {@link TestUserService}/{@code TestUserController}
 * generate. Deliberately NOT how {@code xtcasUsers.Password} itself is stored — that's a one-way BCrypt hash (see
 * {@code UsersService#save}, {@code PasswordEncoder}) and stays that way, since the real login flow validates
 * against it. This class only encrypts the freshly-generated plaintext on its way out of the create-test-user
 * response, so it isn't sitting in logs/response bodies/network captures as raw plaintext — decryptable later via
 * {@code POST /test-users/decrypt-password} by whoever holds {@code ng.api.testUserPasswordSecret}.
 * <p>
 * No external service or library needed for this — {@code javax.crypto} (part of the JDK) does AES-GCM natively,
 * offline, at zero cost.
 * <p>
 * The configured secret (an arbitrary-length passphrase) is SHA-256'd down to a fixed 256-bit AES key — simplest
 * way to accept a human-typed passphrase of any length while still handing {@link Cipher} a fixed-size key. A
 * random 12-byte IV (GCM's recommended size) is generated per {@link #encrypt} call and prefixed to the
 * ciphertext+tag, so the same plaintext never produces the same output twice.
 */
@Component
public class TestPasswordCipher {

	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final int IV_LENGTH_BYTES = 12;
	private static final int GCM_TAG_LENGTH_BITS = 128;

	@Value("${ng.api.testUserPasswordSecret:}")
	private String configuredSecret;

	private final SecureRandom random = new SecureRandom();

	public String encrypt(String plaintext) {
		try {
			byte[] iv = new byte[IV_LENGTH_BYTES];
			random.nextBytes(iv);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, deriveKey(), new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
			byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

			byte[] combined = new byte[iv.length + ciphertext.length];
			System.arraycopy(iv, 0, combined, 0, iv.length);
			System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
			return Base64.getEncoder().encodeToString(combined);
		} catch (IllegalStateException e) {
			throw e; // deriveKey()'s "secret not configured" — let it surface as-is, don't wrap it below
		} catch (Exception e) {
			throw new IllegalStateException("Failed to encrypt test-user password", e);
		}
	}

	public String decrypt(String encoded) {
		byte[] combined;
		try {
			combined = Base64.getDecoder().decode(encoded);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Not a valid encrypted value (bad base64)", e);
		}
		if (combined.length <= IV_LENGTH_BYTES) {
			throw new IllegalArgumentException("Encrypted value is too short to contain an IV");
		}
		byte[] iv = new byte[IV_LENGTH_BYTES];
		byte[] ciphertext = new byte[combined.length - IV_LENGTH_BYTES];
		System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTES);
		System.arraycopy(combined, IV_LENGTH_BYTES, ciphertext, 0, ciphertext.length);

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, deriveKey(), new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
			return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
		} catch (IllegalStateException e) {
			throw e; // deriveKey()'s "secret not configured"
		} catch (Exception e) {
			// Deliberately generic — doesn't distinguish "wrong secret" from "tampered/garbage input" to the
			// caller; both are equally "this value can't be decrypted with the configured secret".
			throw new IllegalArgumentException("Could not decrypt value — wrong secret, or not a value this endpoint produced", e);
		}
	}

	private SecretKeySpec deriveKey() {
		if (configuredSecret == null || configuredSecret.isBlank()) {
			throw new IllegalStateException(
					"ng.api.testUserPasswordSecret is not configured — set it in application.properties before using the test-user endpoints");
		}
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			return new SecretKeySpec(sha256.digest(configuredSecret.getBytes(StandardCharsets.UTF_8)), "AES");
		} catch (Exception e) {
			throw new IllegalStateException("Failed to derive AES key from ng.api.testUserPasswordSecret", e);
		}
	}
}
