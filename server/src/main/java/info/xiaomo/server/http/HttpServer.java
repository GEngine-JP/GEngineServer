package info.xiaomo.server.http;

import info.xiaomo.server.server.GameContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/11/29 10:28.
 *
 * @author 周锟
 */
@RestController
@EnableAutoConfiguration
public class HttpServer {
    public static final int FAIL = -1; //失败
    public static final int SUCCESS = 1; //成功

    public static final String SALT = "k0RlsOyyWMoi";

    public static final String API_KEY = "0I3EaVD2WfElnw3O";

    private static ApplicationContext applicationContext;

    @PreAuthorize("permitAll")
    @RequestMapping("/")
    public int serverInfo() {
        return GameContext.getOption().getServerId();
    }


    static Map<String, Object> resMap(int status, String message, Object data) {
        Map<String, Object> res = new HashMap<>(3);
        res.put("status", status);
        res.put("message", message);
        if (data != null) {
            res.put("data", data);
        }
        return res;
    }

    private void checkParams(String sign, Object... params) {
        StringBuilder builder = new StringBuilder(256);
        for (Object param : params) {
            builder.append(String.valueOf(param));
        }
    }

    public static void start(String file) {
        applicationContext = SpringApplication.run(HttpServer.class, "--spring.config.location=" + file);
    }

    public static void stop() {
        SpringApplication.exit(applicationContext);
    }

    @ControllerAdvice
    public static class HttpErrorHandler {
        @ResponseBody
        @ExceptionHandler(HttpException.class)
        public Map<String, Object> httpError(HttpServletRequest req, HttpException exception) {
            return resMap(exception.getStatus(), exception.getMessage(), null);
        }
    }

}
