package cn.mindit.atom.web.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.core.CoreConstants;
import cn.mindit.atom.core.util.JsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Catch
 * @since 2025-05-28
 */
@Slf4j
// @Aspect
// @Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingAspect {

    // 拦截所有Controller方法
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || " +
        "@within(org.springframework.stereotype.Controller)")
    public void controllerMethods() {
    }

    // 拦截带有RequestMapping系列注解的方法
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void mappingMethods() {
    }

    // 拦截自定义@Logging注解的方法
    @Pointcut("@annotation(cn.mindit.atom.web.core.Logging)")
    public void loggingMethods() {
    }

    // 组合切点
    @Pointcut("(controllerMethods() && mappingMethods()) || loggingMethods()")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long t = System.currentTimeMillis();
        try {
            loggingRequest(joinPoint);
            Object result = joinPoint.proceed();
            loggingResponse(result, t);
            return result;
        } catch (Exception e) {
            loggingException(e, t);
            throw e;
        } finally {
            MDC.remove(CoreConstants.TRACE_ID);
        }
    }

    private void loggingException(Exception e, long t) {
        StringBuilder loggingText = new StringBuilder();
        loggingText.append("ERROR:");
        long time = System.currentTimeMillis() - t;
        loggingText.append(" ").append(time).append("ms")
                   .append(" ").append(e.getMessage());
        log.info(loggingText.toString());
    }

    private void loggingResponse(Object result, long t) {
        StringBuilder loggingText = new StringBuilder();
        loggingText.append("RESPONSE:");
        long time = System.currentTimeMillis() - t;
        loggingText.append(" ").append(time).append("ms");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                loggingText.append(" ").append(response.getStatus());
            }
        }

        String resultText = parseResult(result);
        loggingText.append(" ").append(resultText);
        log.info(loggingText.toString());
    }

    private String parseResult(Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof byte[]) {
            return "[BYTE ARRAY]";
        }
        return JsonUtils.toJson(result);
    }

    private void loggingRequest(ProceedingJoinPoint joinPoint) {
        StringBuilder loggingText = new StringBuilder();
        loggingText.append("REQUEST:");

        // 请求头
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            loggingText.append(" ").append(request.getMethod())
                       .append(" ").append(request.getRequestURI());
        }

        // 请求参数
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return;
        }
        for (Object arg : args) {
            if (arg == null) {
                loggingText.append(" null");
                continue;
            }
            // 跳过Servlet相关对象
            if (arg instanceof HttpServletRequest ||
                arg instanceof HttpServletResponse) {
                continue;
            }
            // 跳过文件上传
            if (arg instanceof MultipartFile) {
                continue;
            }
            // 跳过流对象
            if (arg instanceof java.io.InputStream ||
                arg instanceof java.io.OutputStream) {
                continue;
            }
            loggingText.append(" ").append(JsonUtils.toJson(arg));
        }

        log.info(loggingText.toString());
    }

}
