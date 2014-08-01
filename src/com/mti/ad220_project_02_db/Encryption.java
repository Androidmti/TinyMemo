package com.mti.ad220_project_02_db;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import android.annotation.SuppressLint;
import android.util.Base64;

public class Encryption {

	private String CHARSET = "UTF8";
	private String ALGORITHM = "DES";

	@SuppressLint("TrulyRandom")
	public String encrypt(String key, String data) {
		if (key == null || data == null)
			return null;
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(CHARSET));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			byte[] dataBytes = data.getBytes(CHARSET);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.encodeToString(cipher.doFinal(dataBytes), Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		}
	} // String encrypt(String key, String data)

	public String decrypt(String key, String data) {
		if (key == null || data == null)
			return null;
		try {
			byte[] dataBytes = Base64.decode(data, Base64.DEFAULT);
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(CHARSET));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
			return new String(dataBytesDecrypted);
		} catch (Exception e) {
			return null;
		}
	} // String decrypt(String key, String data)
} // class Encryption