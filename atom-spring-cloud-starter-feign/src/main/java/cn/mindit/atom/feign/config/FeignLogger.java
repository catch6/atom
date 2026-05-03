package cn.mindit.atom.feign.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

/**
 * @author Catch
 * @since 2023-08-09
 */
@ConditionalOnProperty(value = "atom.feign.logging", matchIfMissing = true)
@Component
public class FeignLogger extends Logger {

    private final org.slf4j.Logger logger;

    public FeignLogger() {
        this(Logger.class);
    }

    public FeignLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public FeignLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    FeignLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (!this.logger.isInfoEnabled()) {
            return;
        }
        String bodyText = null;
        if (request.body() != null && request.charset() != null) {
            bodyText = new String(request.body(), request.charset());
        }
        if (bodyText == null) {
            this.logger.info("THIRD-REQUEST: {} {}", request.httpMethod().name(), request.url());
            return;
        }
        this.logger.info("THIRD-REQUEST: {} {} {}", request.httpMethod().name(), request.url(), bodyText);
    }

    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        int status = response.status();
        boolean noContent = status == HttpStatus.NO_CONTENT.value() || status == HttpStatus.RESET_CONTENT.value();
        if (response.body() != null && !noContent) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            if (this.logger.isInfoEnabled()) {
                String bodyText = decodeOrDefault(bodyData, UTF_8, null);
                this.logger.info("THIRD-RESPONSE: {}ms {} {}", elapsedTime, status, bodyText);
            }
            return response.toBuilder().body(bodyData).build();
        }
        if (this.logger.isInfoEnabled()) {
            this.logger.info("THIRD-RESPONSE: {}ms {}", elapsedTime, status);
        }
        return response;
    }

    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format(methodTag(configKey) + format, args));
        }
    }

}
