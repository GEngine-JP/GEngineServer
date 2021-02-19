package info.xiaomo.server.shared.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 用戶
 */
@Entity(value = "user", noClassnameStored = true)
@Data
public class User {

    /**
     * 用户ID
     */
    @Id
    @JSONField
    private long id;

    /**
     * 账号
     */
    @JSONField
    private String account;

    /**
     * 密码
     */
    @JSONField
    private String password;

    /**
     * 注册IP地址
     */
    private String ip;


}
