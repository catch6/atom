package cn.mindit.atom.opc.da;

import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.util.ExpressionResolverUtils;
import cn.mindit.atom.opc.da.config.OpcDaProperties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Slf4j
public class OpcDaConsumerProcessor {

    public static Map<String, List<OpcDaConsumer>> processConsumerMap(ConfigurableApplicationContext applicationContext, OpcDaProperties properties, List<OpcDaSubscriber> subscribers) {
        Map<String, List<OpcDaConsumer>> consumerMap = new ConcurrentHashMap<>();
        processListener(consumerMap, applicationContext, properties);
        processSubscriber(consumerMap, subscribers, properties);
        return consumerMap;
    }

    private static void processListener(Map<String, List<OpcDaConsumer>> consumerMap, ConfigurableApplicationContext applicationContext, OpcDaProperties properties) {
        OpcDaListenerProcessor processor = applicationContext.getBean(OpcDaListenerProcessor.class);
        List<OpcDaConsumer> consumers = processor.getConsumers();
        if (consumers == null || consumers.isEmpty()) {
            return;
        }

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        for (OpcDaConsumer consumer : consumers) {
            String id = consumer.getId();
            if (id == null || id.isEmpty()) {
                consumer.setId(properties.getId());
            }
            String[] items = ExpressionResolverUtils.resolveStringArray(consumer.getItems(), beanFactory, "OPC DA item");
            consumer.setItems(items);
            consumer.initialize();
            List<OpcDaConsumer> list = consumerMap.computeIfAbsent(consumer.getId(), k -> new ArrayList<>());
            list.add(consumer);
        }
    }

    private static void processSubscriber(Map<String, List<OpcDaConsumer>> consumerMap, List<OpcDaSubscriber> subscribers, OpcDaProperties properties) {
        if (subscribers == null || subscribers.isEmpty()) {
            return;
        }
        for (OpcDaSubscriber subscriber : subscribers) {
            String id = subscriber.id();
            if (id == null || id.isEmpty()) {
                id = properties.getId();
            }
            String[] items = subscriber.items();
            OpcDaConsumer consumer = new OpcDaConsumer(id, items, (item, value) -> {
                try {
                    subscriber.message(item, value);
                } catch (Exception e) {
                    log.error("OPC DA invoke error", e);
                }
            });
            consumer.initialize();
            List<OpcDaConsumer> list = consumerMap.computeIfAbsent(id, k -> new ArrayList<>());
            list.add(consumer);
        }
    }

}
