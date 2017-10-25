package info.xiaomo.server.http;

import info.xiaomo.server.server.GameContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaomo
 */
@RestController
@EnableAutoConfiguration
public class HttpServer {

    private static ApplicationContext applicationContext;

    @PreAuthorize("permitAll")
    @RequestMapping("/")
    public int serverInfo() {
        return GameContext.getOption().getServerId();
    }


    static Result<Object> resMap(int status, String message, Object data) {
        return new Result<>(status, message, data);
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
        public Result<Object> httpError(HttpServletRequest req, HttpException exception) {
            return resMap(exception.getStatus(), exception.getMessage(), null);
        }
    }

}
