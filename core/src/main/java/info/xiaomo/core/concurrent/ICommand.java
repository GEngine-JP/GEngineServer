package info.xiaomo.core.concurrent;

/**
 * 游戏总的命令接口.</br>
 * 该命令可以在游戏中任何可以处理命令的地方执行
 * 
 * @author Administrator
 */
public interface ICommand extends Runnable {

	public void doAction();

}
