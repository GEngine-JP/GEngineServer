package info.xiaomo.core.encode.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeHelper {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeHelper.class);
	
	public static final int EveryHour = 1;
	public static final int EveryDay = 2;
	public static final int EveryWeek = 3;
	public static final int EveryMonth = 4;
	public static final int EveryYear = 5;
	
	public static ArrayList<Object> makeDateByString(String str){
		String[] arr =str.split(" ");
		ArrayList<Object> ot = new ArrayList<Object>();
		if(arr[0].equals("每时")){
			ot.add(EveryHour);
			int f = 0;
			if(arr.length>1){//每点几分
				f = Cast.toInteger(arr[1]);
			}
			ot.add(f);
		}
		else if(arr[0].equals("每日")){//每日，时，分
			ot.add(EveryDay);
			if(arr.length>1){
				if(arr[1].indexOf('-')>-1){//多个小时点
					int[] hs = Cast.stringToInts(arr[1], "-");
					ot.add(hs);
				}
				else
				{
					ot.add(new int[]{Cast.toInteger(arr[1])});
				}
				if(arr.length>2){//分钟一般固定
					ot.add(Cast.toInteger(arr[2]));
				}
				else
				{
					ot.add(0);
				}
			}
			else
			{
				ot.add(0);//0时
				ot.add(0);//0分
			}
		}
		else if(arr[0].equals("每周")){
			ot.add(EveryWeek);
			if(arr.length>1){
				if(arr[1].indexOf('-')>-1){//多个周几
					int[] hs = Cast.stringToInts(arr[1], "-");
					ot.add(hs);
				}
				else
				{
					ot.add(new int[]{Cast.toInteger(arr[1])});
				}
			}
			if(arr.length>2){//小时固定
				ot.add(Cast.toInteger(arr[2]));
			}
			else
			{
				ot.add(0);
			}
			if(arr.length>3){//分钟固定
				ot.add(Cast.toInteger(arr[3]));
			}
			else
			{
				ot.add(0);
			}
		}
		else if(arr[0].equals("每月")){
			ot.add(EveryMonth);
			if(arr.length>1){
				if(arr[1].indexOf('-')>-1){//多个日
					int[] hs = Cast.stringToInts(arr[1], "-");
					ot.add(hs);
				}
				else
				{
					ot.add(new int[]{Cast.toInteger(arr[1])});
				}
			}
			if(arr.length>2){//小时固定
				ot.add(Cast.toInteger(arr[2]));
			}
			else
			{
				ot.add(0);
			}
			if(arr.length>3){//分钟固定
				ot.add(Cast.toInteger(arr[3]));
			}
			else
			{
				ot.add(0);
			}
		}
		else if(arr[0].equals("每年")){
			ot.add(EveryYear);
			if(arr.length>1){
				if(arr[1].indexOf('-')>-1){//多个月份
					int[] hs = Cast.stringToInts(arr[1], "-");
					ot.add(hs);
				}
				else
				{
					ot.add(new int[]{Cast.toInteger(arr[1])});
				}
			}
			if(arr.length>2){//天
				if(arr[2].indexOf('-')>-1){//多个天
					int[] hs = Cast.stringToInts(arr[2], "-");
					ot.add(hs);
				}
				else
				{
					ot.add(new int[]{Cast.toInteger(arr[2])});
				}
			}
			else
			{
				ot.add(0);
			}
			if(arr.length>3){//小时固定
				ot.add(Cast.toInteger(arr[3]));
			}
			else
			{
				ot.add(0);
			}
			if(arr.length>4){//分钟固定
				ot.add(Cast.toInteger(arr[4]));
			}
			else
			{
				ot.add(0);
			}
		}
		return ot;
	}
	
	
	/**
	 * 比较curtime是否满足ot配的时间规则
	 * @param curtime
	 * @param ot
	 * @return
	 */
	public static boolean compareTimeToMakeDate(long curtime,ArrayList<Object> ot){
		int type = Cast.toInteger(ot.get(0));
		Date date = new Date(curtime);
		boolean check = false;
		switch(type){
		case EveryHour:
			if(ot.size() == 2){
				if(!ot.get(1).equals(date.getMinutes())){
					return false;
				}
			}
			break;
		case EveryDay:
			if(ot.size() == 3){
				check = isIntInArray((int[]) ot.get(1),date.getHours());
				if(!check)
					return false;
				if(!ot.get(2).equals(date.getMinutes()))
					return false;
			}
			break;
		case EveryWeek:
			if(ot.size() == 4){
				int[] days = (int[]) ot.get(1);
				check = isIntInArray(days,date.getDay());
				if(!check)
					return false;
				if(!ot.get(2).equals(date.getHours()))
					return false;
				if(!ot.get(3).equals(date.getMinutes()))
					return false;
			}
			break;
		case EveryMonth:
			if(ot.size() == 4){
				int[] days = (int[]) ot.get(1);
				check = isIntInArray(days,date.getDate());
				if(!check)
					return false;
				if(!ot.get(2).equals(date.getHours()))
					return false;
				if(!ot.get(3).equals(date.getMinutes()))
					return false;
			}
			break;
		case EveryYear:
			if(ot.size() == 5){
				check = isIntInArray((int[]) ot.get(1),date.getMonth()+1);
				if(!check)
					return false;
				check = isIntInArray((int[]) ot.get(2),date.getDate());
				if(!check)
					return false;
				if(!ot.get(3).equals(date.getHours()))
					return false;
				if(!ot.get(4).equals(date.getMinutes()))
					return false;
			}
			break;
		}
		return true;
	}
	
	private static boolean isIntInArray(int[] arr,int check){
		boolean ret = false;
		for(int day : arr){
			if(day == check){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param time
	 * @param timeunit
	 * @return
	 */
	public static long getDelayByFixTime(long time,TimeUnit unit){
		long cur = System.currentTimeMillis();
		long temp = unit.convert(cur, TimeUnit.MILLISECONDS);
		cur = ((int)(temp/time))*time+time;
		return cur-temp;
	}
	
	public static long getCurDate(){
		Date date = new Date();
		date = new Date(date.getYear(),date.getMonth(),date.getDate());
		return date.getTime();
	}
	
	
	public static void main(String[] args) {
//		String str = "每日 15 19";
//		ArrayList<Object> list = makeDateByString(str);
//		LOGGER.info(compareTimeToMakeDate(System.currentTimeMillis(), list));
		
		
		
		
		long period = 10;
		TimeUnit unit = TimeUnit.SECONDS;
		long initialDelay = getDelayByFixTime(period,unit);
		LOGGER.info(String.valueOf(initialDelay));
		ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(4);
		threadpool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				LOGGER.info(new Date()+","+ System.currentTimeMillis());
			}
		}, initialDelay, period, unit);
	}
}
