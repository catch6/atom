package cn.mindit.atom.opc.ua;

import cn.mindit.atom.core.util.AbstractListenerProcessor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
public class OpcUaListenerProcessor extends AbstractListenerProcessor<OpcUaListener, OpcUaConsumer> {

    @Override
    protected Class<OpcUaListener> annotationType() {
        return OpcUaListener.class;
    }

    @Override
    protected OpcUaConsumer buildConsumer(Object bean, Method method, OpcUaListener listener) {
        return new OpcUaConsumer(listener.id(), listener.items(), listener.namespaceIndices(), (topic, value) -> {
            try {
                method.invoke(bean, topic, value);
            } catch (Exception e) {
                log.error("OPC UA invoke error", e);
            }
        });
    }

}
