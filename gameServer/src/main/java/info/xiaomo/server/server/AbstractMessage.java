package info.xiaomo.server.server;

import info.xiaomo.core.concurrent.IQueueDriverCommand;
import info.xiaomo.core.concurrent.queue.ICommandQueue;
import info.xiaomo.core.net.Message;
import info.xiaomo.core.net.kryo.KryoBean;
import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.core.net.kryo.KryoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象消息，该消息实现了Message的一些方法
 *
 * @author xiaomo
 */
public abstract class AbstractMessage extends KryoBean implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessage.class);

    /**
     * 命令队列
     */
    private ICommandQueue<IQueueDriverCommand> commandQueue;

    /**
     * 消息长度
     */
    private int length;

    /**
     * 一个额外的参数
     */
    protected Session session;

    /**
     * 队列ID
     */
    protected int queueId;

    /**
     * 过滤器
     */
    protected MessageFilter filter;

    protected short sequence;


    public short getSequence() {
        return sequence;
    }

    public void setSequence(short sequence) {
        this.sequence = sequence;
    }

    @Override
    public void decode(byte[] bytes) {
        KryoInput input = KryoUtil.getInput();
        input.setBuffer(bytes);
        read(input);
    }

    @Override
    public byte[] encode() {
        KryoOutput output = KryoUtil.getOutput();
        write(output);
        return output.toBytesAndClear();
    }

    @Override
    public void run() {
        try {
            if (filter != null && !filter.before(this)) {
                return;
            }
            doAction();
        } catch (Throwable e) {
            LOGGER.error("命令执行错误", e);
        }
    }

    @Override
    public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
        return commandQueue;
    }

    @Override
    public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public Session getParam() {
        return session;
    }

    @Override
    public void setParam(Object param) {
        this.session = (Session) param;
    }

    @Override
    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public int getQueueId() {
        return this.queueId;
    }


    public String toString() {
        return "[id->" + getId() + ",sequence->" + sequence + "]";
    }


}
