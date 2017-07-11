package info.xiaomo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 通过二进制的每位0和1来表示是否拥有某一个值
 * @author chzcb
 *
 */
public class BooleanSet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BooleanSet.class);
	protected byte[] data;
	
	public synchronized void set(int key,boolean value){
		int len = (key>>3);
		if(data == null || (data.length<=len && value)){//扩展data或者初始化data
			byte[] newData = new byte[len+1];
			if(data != null)
				System.arraycopy(data, 0, newData, 0, data.length);
			this.data = newData;
		}
		int index = (key&7);
		byte v;
		if(value){
			v = (byte) (1<<index);
			data[len] = (byte) (data[len]^v);
		}
		else if(data.length>len)
		{
			v = (byte) (~(1 << index));
			data[len] = (byte) (data[len]&v);
			int newlen = data.length;
			while(data[newlen-1] == 0){
				newlen--;
			}
			if(data.length!=newlen){
				byte[] newData = new byte[newlen];
				System.arraycopy(data, 0, newData, 0, newlen);
				this.data = newData;
			}
		}
	}
	
	public synchronized boolean get(int key) {
		int len = (key >> 3);
		int index = (key & 7);
		return this.data.length > len && (data[len] & (1 << index)) > 0;
	}
	
	public static void main(String[] args) {
		BooleanSet e = new BooleanSet();
		e.set(12, true);
		e.set(20, true);
		e.set(122, true);
		LOGGER.info(String.valueOf(e.get(122)));
		e.set(122, false);
		LOGGER.info(String.valueOf(e.get(1)));
		
		LOGGER.info(String.valueOf(e.get(21)));
		LOGGER.info(String.valueOf(e.data.length));
	}
}

