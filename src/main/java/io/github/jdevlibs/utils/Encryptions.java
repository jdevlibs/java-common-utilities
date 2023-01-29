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
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private static final String DIGEST_MD5  	= "MD5";
    private static final String AES_MODE 		= "AES/GCM/NoPadding";
    private static final String SECRET_KEY  	= "AES_GCM_NOPADDING_KEY";
    private static final int BYTE 				= 1024;
    private static final int GCM_TAG_LENGTH 	= 16;
    
    private Encryptions() {}
    
    public static String md5(String value) throws SystemException {
        return md5(null, value);
    }
    
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
    
    public static String fileMD5(String file) throws SystemException {
        return fileMD5(new File(file));
    }
    
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

	public static String encodeBase64String(String value) {
		if (Validators.isEmpty(value)) {
			return Values.EMPTY;
		}
		return encodeBase64String(value.getBytes(StandardCharsets.UTF_8));
	}

	public static String encodeBase64String(byte[] values) {
		if (Validators.isEmpty(values)) {
			return Values.EMPTY;
		}

		return Base64.getEncoder().encodeToString(values);
	}

	public static String decodeBase64String(String base64Data) {
		if (Validators.isEmpty(base64Data)) {
			return Values.EMPTY;
		}

		return new String(decodeBase64(base64Data), StandardCharsets.UTF_8);
	}

	public static byte[] decodeBase64(String base64Data) {
		if (Validators.isEmpty(base64Data)) {
			return new byte[0];
		}

		return Base64.getDecoder().decode(base64Data);
	}
	
	public static String encryptAES(String data) throws SystemException {
		return encryptAES(data, SECRET_KEY);
	}
	
	public static String encryptAES(String data, String secret) throws SystemException {
		try {
			SecretKeySpec secretKey = createSecretKeySpec(secret);
			Cipher cipher = Cipher.getInstance(AES_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
			return encodeBase64String(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| IllegalBlockSizeException | BadPaddingException ex) {
			throw new SystemException(ex);
		}
	}

	public static String decryptAES(String data) throws SystemException {
		return decryptAES(data, SECRET_KEY);
	}

	public static String decryptAES(String data, String secret) throws SystemException {
		try {
			SecretKeySpec secretKey = createSecretKeySpec(secret);
			Cipher cipher = Cipher.getInstance(AES_MODE);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] values = decodeBase64(data);
			values = cipher.doFinal(values);
			return new String(values);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException ex) {
			throw new SystemException(ex);
		}
	}
	
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
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		keys = sha.digest(keys);
		keys = Arrays.copyOf(keys, GCM_TAG_LENGTH);
		return new SecretKeySpec(keys, "AES");
	}
}
