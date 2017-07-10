package info.xiaomo.core.encode.util;

public class StringFormat  
{  
    //字符串合并方法，返回一个合并后的字符串  
    public static String formatSlow(String str, Object... args)
    {  
  
        //这里用于验证数据有效性  
        if(str==null||"".equals(str))  
            return "";  
        if(args.length==0)  
        {  
            return str;  
        }  
  
//        StringBuffer result = new StringBuffer();
        
        String result = "";
        //这里的作用是只匹配{}里面是数字的子字符串  
        final java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{(\\d+)\\}");  
        final java.util.regex.Matcher m = p.matcher(str);  
  
        int lastend = 0;
        
        while(m.find())  
        {  
            //获取{}里面的数字作为匹配组的下标取值  
        	final int index= Integer.parseInt(m.group(1));
        	int start = m.start();
        	if(start>lastend)
        		result += str.substring(lastend,start);
        	 if(index<args.length)  
             {  
        		 if(args[index]!=null)
        			 result += args[index].toString();
        		 else
        			 result += "NULL";
             }
        	lastend = m.end();
        }  
        return result;  
    }  
    public static String format(String str, Object... args)
    {  
  
        //这里用于验证数据有效性  
        if(str==null||"".equals(str))  
            return ""; 
        final int length = args.length;
        
        if(length==0)  
        {  
            return str;  
        }  
  
        StringBuffer result = new StringBuffer();
        
        //这里的作用是只匹配{}里面是数字的子字符串  
        final java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{(\\d+)\\}");  
        final java.util.regex.Matcher m = p.matcher(str);  
  
        int lastend = 0;
        
        while(m.find())  
        {  
            //获取{}里面的数字作为匹配组的下标取值  
        	final int index= Integer.parseInt(m.group(1));
        	int start = m.start();
        	if(start>lastend)
        		result.append(str.substring(lastend,start));
        	 if(index<length)  
             {
        		 if(args[index]!=null)
        			 result.append(args[index].toString());
        		 else
        			 result.append("NULL");
        		 
             }
        	lastend = m.end();
        }
        if(lastend<str.length()){
        	result.append(str.substring(lastend));
        }
        return result.toString();  
    }  
}  