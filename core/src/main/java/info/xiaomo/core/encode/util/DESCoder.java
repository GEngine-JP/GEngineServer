package info.xiaomo.core.encode.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

public class DESCoder   
{  
    private static String key = "SSOFOUNDER"; // 字节数必须是8的倍数
    public static byte[] desEncrypt(byte[] plainText) throws Exception
    {  
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;  
        byte encryptedData[] = cipher.doFinal(data);  
        return encryptedData;  
    }  
      
    public static byte[] desDecrypt(byte[] encryptText) throws Exception
    {  
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks =  new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        return decryptedData;  
    }  
      
    public static String encrypt(String input) throws Exception
    {  
        return base64Encode(desEncrypt(input.getBytes()));  
    }  
      
    public static String decrypt(String input) throws Exception
    {  
        byte[] result = base64Decode(input);  
        return new String(desDecrypt(result));
    }  
      
    public static String base64Encode(byte[] s)
    {  
        if (s == null)  
            return null;  
        BASE64Encoder b = new sun.misc.BASE64Encoder();
        return b.encode(s);  
    }  
      
    public static byte[] base64Decode(String s) throws IOException
    {  
        if (s == null)  
        {  
           return null;  
        }  
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);  
        return b;  
    }

	public static void setKey(String key) {
		DESCoder.key = key;
	}
    
}  

