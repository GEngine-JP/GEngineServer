package info.xiaomo.core.encode.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compress {
	private static int cachesize = 1024*8; 
	
	protected static ThreadLocal<Inflater> cacheInflaters = new ThreadLocal<Inflater>(){
		protected Inflater initialValue() {
            return new Inflater();
        }
	};
	public static Inflater getInflater() {
		return cacheInflaters.get();
	}
	
	protected static ThreadLocal<Deflater> cacheDeflaters = new ThreadLocal<Deflater>(){
		protected Deflater initialValue() {
            return new Deflater();
        }
	};
	public static Deflater getDeflater() {
		return cacheDeflaters.get();
	}
	
	/**
	 * 压缩字节
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] input){
		Deflater compresser = getDeflater();
		compresser.reset(); 
//		compresser.setStrategy(Deflater.DEFAULT_STRATEGY);
		compresser.setInput(input); 
		compresser.finish(); 
		byte output[] = new byte[0]; 
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try 
		{ 
			byte[] buf = new byte[cachesize]; 
			int got; 
			while (!compresser.finished()) 
			{ 
				got = compresser.deflate(buf); 
				o.write(buf, 0, got); 
			} 
			output = o.toByteArray(); 
		} finally 
		{ 
			try 
			{ 
				o.close(); 
			} catch (IOException e)
			{ 
				e.printStackTrace(); 
			} 
		} 
		return output; 
	} 

	public static byte[] uncompress(byte[] input){
		if(input == null)
			return null;
		return uncompress(input,0,input.length);
	}
	
	public static byte[] uncompress(byte[] input, int offset,int len){
		byte output[] = new byte[0]; 
		Inflater decompresser = getInflater();
		decompresser.reset();
		decompresser.setInput(input,offset,len); 
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try 
		{ 
			byte[] buf = new byte[cachesize];
			int got; 
			while (!decompresser.finished()) 
			{ 
				got = decompresser.inflate(buf); 
				o.write(buf, 0, got); 
			} 
			output = o.toByteArray(); 
		}catch(Exception e)
		{ 
			e.printStackTrace(); 
		}finally 
		{ 
			try 
			{ 
				o.close(); 
			} catch (IOException e)
			{ 
				e.printStackTrace(); 
			} 
		} 
		return output; 
	}
}
