package info.xiaomo.server.server;

import info.xiaomo.gameCore.protocol.entity.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象消息，该消息实现了Message的一些方法
 *
 * @author 小莫
 */
public abstract class AbstractMessage extends Bean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessage.class);


}
