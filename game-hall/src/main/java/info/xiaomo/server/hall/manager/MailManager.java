package info.xiaomo.server.hall.manager;

import java.util.function.Consumer;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.hall.script.IMailScript;
import info.xiaomo.server.shared.dao.mongo.MailDao;
import info.xiaomo.server.shared.entity.Mail;
import info.xiaomo.server.shared.protocol.hall.chat.MailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件
 *
 * 个人邮件单独存储，系统通用邮件只存一封,直接操作mongodb，不缓存
 * </p>
 *
 *
 * 2017年9月21日 下午3:25:17
 */
public class MailManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailManager.class);
	private static volatile MailManager mailManager;

	private MailManager() {

	}

	public static MailManager getInstance() {
		if (mailManager == null) {
			synchronized (MailManager.class) {
				if (mailManager == null) {
					mailManager = new MailManager();
				}
			}
		}
		return mailManager;
	}

	public Mail getMail(long mailId) {
		return (Mail) MailDao.getMail(mailId);
	}

	/**
	 * 发送邮件
	 *
	 *
	 * 2017年9月21日 下午4:26:31
	 *
	 * @param title
	 * @param content
	 * @param type
	 * @param mailConsumer
	 */
	public void sendMail(long senderId, long receiverId, String title, String content, Mail.MailType type, Consumer<Mail> mailConsumer) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IMailScript.class,
				script -> script.sendMail(senderId, receiverId, title, content, type, mailConsumer));
	}

	/**
	 * 构建邮箱信息
	 *
	 *
	 * 2017年9月21日 下午5:45:09
	 *
	 * @param mail
	 * @return
	 */
	public MailInfo buildMailInfo(Mail mail) {
		MailInfo.Builder builder = MailInfo.newBuilder();
		builder.setContent(mail.getContent());
		builder.setTitle(mail.getTitle());
		builder.setCreateTime(mail.getCreateTime().getTime());
		builder.setId(mail.getId());
		builder.setSenderId(mail.getSenderId());
		builder.setState(mail.getState());
		return builder.build();
	}
}
