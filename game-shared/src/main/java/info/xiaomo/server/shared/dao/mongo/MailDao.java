package info.xiaomo.server.shared.dao.mongo;

import com.mongodb.WriteResult;
import java.util.List;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.shared.entity.Mail;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 * 邮件
 * <p>
 * <p>
 * 2017年9月21日 下午3:32:10
 */
public class MailDao extends BasicDAO<Mail, Long> {
	private static volatile MailDao mailDao;

	public MailDao(AbsMongoManager mongoManager) {
		super(Mail.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static MailDao getDB(AbsMongoManager mongoManager) {
		if (mailDao == null) {
			synchronized (MailDao.class) {
				if (mailDao == null) {
					mailDao = new MailDao(mongoManager);
				}
			}
		}
		return mailDao;
	}

	/**
	 * 玩家所有邮件
	 * <p>
	 * <p>
	 * 2017年9月21日 下午4:04:46
	 *
	 * @param receiverId
	 * @return
	 */
	public static List<Mail> getMails(long receiverId) {
		return mailDao.createQuery().filter("receiverId", receiverId).asList();
	}

	/**
	 * 获取公共邮件
	 * <p>
	 * <p>
	 * 2017年9月21日 下午4:12:44
	 *
	 * @return
	 */
	public static List<Mail> getPublicMails() {
		return mailDao.createQuery().filter("type", Mail.MailType.PUBLIC_SYSTEM.ordinal()).asList();
	}

	/**
	 * 存储邮件
	 * <p>
	 * <p>
	 * 2017年9月21日 下午4:13:37
	 *
	 * @param Mail
	 */
	public static void saveMail(Mail Mail) {
		mailDao.save(Mail);
	}

	/**
	 * 改变邮件状态
	 * <p>
	 * <p>
	 * 2017年9月21日 下午4:15:10
	 *
	 * @param id
	 * @param mailState
	 */
	public static Mail modifyMailState(long id, int mailState) {
		Query<Mail> query = mailDao.createQuery().filter("id", id);
		UpdateOperations<Mail> operations = mailDao.createUpdateOperations().set("state", mailState);
		return mailDao.getDs().findAndModify(query, operations);
	}

	/**
	 * 删除邮件
	 * <p>
	 * <p>
	 * 2017年9月21日 下午4:23:52
	 *
	 * @param id
	 * @return
	 */
	public static int deleteMail(long id) {
		WriteResult writeResult = mailDao.deleteById(id);
		return writeResult.getN();
	}

	/**
	 * 获取邮件
	 * <p>
	 * <p>
	 * 2017年9月21日 下午5:25:11
	 *
	 * @param mailId
	 * @return
	 */
	public static Mail getMail(long mailId) {
		return mailDao.get(mailId);
	}

}
