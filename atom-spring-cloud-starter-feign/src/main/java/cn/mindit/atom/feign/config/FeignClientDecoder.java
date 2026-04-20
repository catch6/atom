package cn.mindit.atom.feign.config;

import cn.mindit.atom.core.util.Result;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "atom.feign.decode", matchIfMissing = true)
public class FeignClientDecoder extends SpringDecoder {

    public FeignClientDecoder(ObjectProvider<FeignHttpMessageConverters> converters) {
        super(converters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        int status = response.status();
        Object object = super.decode(response, type);
        if (object instanceof Result<?> result) {
            if (status < 400) {
                return result;
            }
            throw new ThirdException(status, result, response.request());
        }
        if (status < 400) {
            return object;
        }
        throw new ThirdException(status, response.request());
    }

}
