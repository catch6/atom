package cn.mindit.atom.opc.da;

import cn.mindit.atom.core.util.AbstractListenerProcessor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
public class OpcDaListenerProcessor extends AbstractListenerProcessor<OpcDaListener, OpcDaConsumer> {

    @Override
    protected Class<OpcDaListener> annotationType() {
        return OpcDaListener.class;
    }

    @Override
    protected OpcDaConsumer buildConsumer(Object bean, Method method, OpcDaListener listener) {
        return new OpcDaConsumer(listener.id(), listener.items(), (topic, value) -> {
            try {
                method.invoke(bean, topic, value);
            } catch (Exception e) {
                log.error("OPC DA invoke error", e);
            }
        });
    }

}
