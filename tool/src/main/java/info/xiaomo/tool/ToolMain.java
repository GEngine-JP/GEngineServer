package info.xiaomo.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 13:17
 * desc  : 消息生成工具
 * Copyright(©) 2017 by xiaomo.
 */
public class ToolMain {
    private static final String CONFIG_URL = "F:\\ChessGame\\tool\\src\\main\\resources\\tool.properties";

    private static String input;
    private static String output;

    public static void main(String[] args) throws IOException {
        InputStream in = null;
            try {
                in = new FileInputStream(CONFIG_URL);
                Properties properties = new Properties();
                properties.load(in);
                input = (String) properties.get("input");
                output = (String) properties.get("output");
                System.out.println(input);
                System.out.println(output);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (in!= null){
                    in.close();
                }
            }
        }
    }

