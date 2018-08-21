import com.ppluobo.jtools.encrypt.RC4BytesEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class RC4EncryptorTest {

    final Logger log  = LoggerFactory.getLogger(RC4EncryptorTest.class);

    @Test
    public void test(){

        RC4BytesEncryptor encryptor = new RC4BytesEncryptor("12345!@#$%^");

        String str = "ABCDEFG123456";

        Charset utf8 = Charset.forName("UTF-8");

        byte[] encBytes = encryptor.encrypt(str.getBytes(utf8));
        Assert.assertEquals(str, new String(encryptor.decrypt(encBytes), utf8));



        String encHex = encryptor.encryptHex(str);
        log.info(encHex);
        Assert.assertEquals(str, encryptor.decryptHex(encHex));

        String encBase64 = encryptor.encryptBase64(str);
        log.info(encBase64);
        Assert.assertEquals(str, encryptor.decryptBase64(encBase64));
    }
}
