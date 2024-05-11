/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import io.github.jdevlibs.utils.exception.SystemException;

/**
 * Utility class for manage security encryption 
 * @author JDEV
 * @version 1.0
 */
public final class Encryptions {
	private static final String UTF8 			= "UTF-8";
	private static final String ALGORITHM_AES 	= "AES";
	private static final String ALGORITHM_SHA1 	= "SHA-1";
	private static final String ALGORITHM_RSA	= "RSA";
	private static final String ALGORITHM_RSA_ECB 	= "RSA/ECB/PKCS1Padding";
    private static final String DIGEST_MD5  	= "MD5";
    private static final String SECRET_KEY  	= "ENCRYPTION_DEFAULT_SECRET_KEY";
    private static final int BYTE 				= 1024;
    private static final int GCM_TAG_LENGTH 	= 16;
    
    private Encryptions() {}

	/**
	 * Encryption value to MD5 algorithm
	 * @param value Input value
	 * @return The value encryption with MD5
	 */
    public static String md5(String value) throws SystemException {
        return md5(null, value);
    }

	/**
	 * Encryption value to MD5 algorithm
	 * @param hashKey Hash key for MD5
	 * @param value Input value
	 * @return The value encryption with MD5
	 */
    public static String md5(String hashKey, String value) throws SystemException {
        try {
            if (Validators.isEmpty(value)) {
                return null;
            }
            
            MessageDigest md = MessageDigest.getInstance(DIGEST_MD5);            
            md.reset();
            
            String hashVal = value;
            if (Validators.isNotEmpty(hashKey)) {
                hashVal += "@" + hashKey;
            }

            byte[] passBytes = hashVal.getBytes();
            byte[] digested = md.digest(passBytes);
            
            StringBuilder sb = new StringBuilder();
			for (byte b : digested) {
				sb.append(Integer.toHexString(0xff & b));
			}
            
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new SystemException(ex);
        }
    }

	/**
	 * Encryption File to MD5 algorithm
	 * @param file Input file name
	 * @return The file encryption with MD5
	 */
    public static String fileMD5(String file) throws SystemException {
        return fileMD5(new File(file));
    }

	/**
	 * Encryption File to MD5 algorithm
	 * @param file Input file name
	 * @return The file encryption with MD5
	 */
	public static String fileMD5(File file) throws SystemException {

		if (!file.exists()) {
			return null;
		}
		
		try (FileInputStream fis = new FileInputStream(file)) {
			MessageDigest md = MessageDigest.getInstance(DIGEST_MD5);

			byte[] dataBytes = new byte[BYTE];
			int nread;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}

			StringBuilder sb = new StringBuilder();
			byte[] bytes = md.digest();
			for (byte aByte : bytes) {
				sb.append(Integer.toHexString(0xff & aByte));
			}

			return sb.toString();
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Encode value to BASE64 algorithm
	 * @param value Input value
	 * @return Result encode with BASE64
	 */
	public static String encodeBase64String(String value) {
		if (Validators.isEmpty(value)) {
			return Values.EMPTY;
		}
		return encodeBase64String(value.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Encode value to BASE64 algorithm
	 * @param values Input byte data value
	 * @return Result encode with BASE64
	 */
	public static String encodeBase64String(byte[] values) {
		if (Validators.isEmpty(values)) {
			return Values.EMPTY;
		}

		return Base64.getEncoder().encodeToString(values);
	}

	/**
	 * Decode value to BASE64 algorithm
	 * @param base64Data Input encode value
	 * @return Result encode with BASE64
	 */
	public static String decodeBase64String(String base64Data) {
		if (Validators.isEmpty(base64Data)) {
			return Values.EMPTY;
		}

		return new String(decodeBase64(base64Data), StandardCharsets.UTF_8);
	}

	/**
	 * Decode value to BASE64 algorithm
	 * @param base64Data Input encode value
	 * @return Result encode byte with BASE64
	 */
	public static byte[] decodeBase64(String base64Data) {
		if (Validators.isEmpty(base64Data)) {
			return new byte[0];
		}

		return Base64.getDecoder().decode(base64Data);
	}

	/**
	 * Encryption data with AES algorithm using a default secret key
	 * @param data Input data value
	 * @return The value of encryption
	 */
	public static String encryptAES(String data) throws SystemException {
		return encryptAES(data, SECRET_KEY);
	}

	/**
	 * Encryption data with AES algorithm
	 * @param data Input data value
	 * @param secret Secret key
	 * @return The value of encryption
	 */
	public static String encryptAES(String data, String secret) throws SystemException {
		try {
			SecretKeySpec secretKey = createSecretKeySpec(secret);
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
			return encodeBase64String(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| IllegalBlockSizeException | BadPaddingException ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Decryption data with AES algorithm using a default secret key
	 * @param data Input encryption value
	 * @return The value of decryption
	 */
	public static String decryptAES(String data) throws SystemException {
		return decryptAES(data, SECRET_KEY);
	}

	/**
	 * Decryption data with AES algorithm
	 * @param data Input encryption value
	 * @param secret Secret key
	 * @return The value of decryption
	 */
	public static String decryptAES(String data, String secret) throws SystemException {
		try {
			SecretKeySpec secretKey = createSecretKeySpec(secret);
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] values = decodeBase64(data);
			values = cipher.doFinal(values);
			return new String(values);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Generate KeyPairValue with a public/private key
	 * @return The KeyPairValue object
	 */
	public static KeyPairValue generateKeyPair() throws SystemException {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			KeyPairValue keyPairValue = new KeyPairValue();
			keyPairValue.setPublicKey(encodeBase64String(keyPair.getPublic().getEncoded()));
			keyPairValue.setPrivateKey(encodeBase64String(keyPair.getPrivate().getEncoded()));

			return keyPairValue;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Encryption data with RSA algorithm with a public key.
	 * @param message Input message value
	 * @param publicKeyValue The public key
	 * @return The value of encryption
	 */
	public static String encryptRSA(String message, String publicKeyValue) throws SystemException {
		try {
			byte[] keyBytes = decodeBase64(publicKeyValue);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA_ECB);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			return encodeBase64String(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Decryption data with RSA algorithm with a private key.
	 * @param message Input encryption message
	 * @param privateKeyValue The private key
	 * @return The value of decryption
	 */
	public static String decryptRSA(String message, String privateKeyValue) throws SystemException {
		try {
			byte[] keyBytes = decodeBase64(privateKeyValue);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey= keyFactory.generatePrivate(privateKeySpec);

			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA_ECB);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(decodeBase64(message)), StandardCharsets.UTF_8);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Encode URL
	 * @param value The URL
	 * @return The value of encoding
	 */
	public static String urlEncode(String value) {
		if (Validators.isEmpty(value)) {
			return "";
		}
		try {
			return URLEncoder.encode(value, UTF8);
		} catch (UnsupportedEncodingException ex) {
			return value;
		}
	}

	/**
	 * Decode URL
	 * @param value The URL
	 * @return The value of decode
	 */
	public static String urlDecode(String value) {
		if (Validators.isEmpty(value)) {
			return "";
		}
		try {
			return URLDecoder.decode(value, UTF8);
		} catch (UnsupportedEncodingException ex) {
			return value;
		}
	}

	private static SecretKeySpec createSecretKeySpec(String secret) throws NoSuchAlgorithmException {
		byte[] keys = secret.getBytes(StandardCharsets.UTF_8);
		MessageDigest sha = MessageDigest.getInstance(ALGORITHM_SHA1);
		keys = sha.digest(keys);
		keys = Arrays.copyOf(keys, GCM_TAG_LENGTH);
		return new SecretKeySpec(keys, ALGORITHM_AES);
	}

	public static final class KeyPairValue {
		private String publicKey;
		private String privateKey;

		public KeyPairValue() {

		}

		public KeyPairValue(String publicKey, String privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public String getPrivateKey() {
			return privateKey;
		}

		public void setPrivateKey(String privateKey) {
			this.privateKey = privateKey;
		}

		public String getPublicKey() {
			return publicKey;
		}

		public void setPublicKey(String publicKey) {
			this.publicKey = publicKey;
		}
	}
}
