package info.xiaomo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class EncrypRSA {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncrypRSA.class);
	
	/**
	 * 加密
	 * @param publicKey
	 * @param srcBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if(publicKey!=null){
			//Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			//根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}
	
	/**
	 * 解密 
	 * @param privateKey
	 * @param srcBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if(privateKey!=null){
			//Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			//根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(srcBytes);
		}
		return null;
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		EncrypRSA rsa = new EncrypRSA();
		String msg = "郭XX-精品相声";
		//KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		//初始化密钥对生成器，密钥大小为1024位
		keyPairGen.initialize(1024);
		//生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		//得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		//得到公钥
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
		
		LOGGER.info(String.valueOf(privateKey));
		LOGGER.info(String.valueOf(publicKey));
		
		long starttime = System.currentTimeMillis();
		//用公钥加密
		byte[] srcBytes = msg.getBytes();
		byte[] resultBytes = rsa.encrypt(publicKey, srcBytes);
		LOGGER.info(String.valueOf(System.currentTimeMillis()-starttime));
		starttime = System.currentTimeMillis();
		//用私钥解密
		byte[] decBytes = rsa.decrypt(privateKey, resultBytes);
		LOGGER.info(String.valueOf(System.currentTimeMillis()-starttime));
		
		BigInteger modulus = publicKey.getModulus();
		BigInteger exponent = publicKey.getPublicExponent();
		
		BigInteger privateExponent = privateKey.getPrivateExponent();
		
		RSAPrivateKeySpec privatespes = new RSAPrivateKeySpec(modulus, privateExponent);
		
		RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		try {
			LOGGER.info(String.valueOf(keyFactory.generatePublic(spec)));
			LOGGER.info(String.valueOf(keyFactory.generatePrivate(privatespes)));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
//		LOGGER.info(publicKey);
//		LOGGER.info(spec);
		LOGGER.info("明文是:" + msg);
		LOGGER.info("加密后是:" + new String(resultBytes));
		LOGGER.info("解密后是:" + new String(decBytes));
	}

}
