package info.xiaomo.server.rpg.test;

import info.xiaomo.gengine.mail.EMailManager;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 发送邮件测试
 *
 * <p>2017年8月22日 下午6:02:34
 */
@Ignore
public class BaseEMailManagerTest {

    @Test
    public void testSendMail() {
        EMailManager.getInstance()
                .sendTextMailAsync(
                        "hh", "dd", "suzukaze.hazuki2020@gmail.com", "xiaomo@xiaomo.info");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
