package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.core.common.struct.Mail;
import info.xiaomo.core.script.IScript;

/**
 * 邮件
 *
 * <p>2017年9月21日 下午4:45:19
 */
public interface IMailScript extends IScript {

  /**
   * 发送邮件
   *
   * <p>2017年9月21日 下午4:26:31
   *
   * @param title
   * @param content
   * @param type
   * @param mailConsumer
   */
  default void sendMail(
      long senderId,
      long receiverId,
      String title,
      String content,
      Mail.MailType type,
      Consumer<Mail> mailConsumer) {}
}
