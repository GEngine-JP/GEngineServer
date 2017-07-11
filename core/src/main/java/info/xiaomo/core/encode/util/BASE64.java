package info.xiaomo.core.encode.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64加密解密
 */
public class BASE64 {

    private static final Logger LOGGER = LoggerFactory.getLogger(BASE64.class);

    /**
     * BASE64解密 * @param key * @return * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密 * @param key * @return * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    public static void main(String[] args) throws Exception {

        String data = BASE64.encryptBASE64("http://aub.iteye.com/".getBytes());
        LOGGER.info("加密后：" + data);

        byte[] byteArray = BASE64.decryptBASE64(data);
        LOGGER.info("解密后：" + new String(byteArray));
    }
} 