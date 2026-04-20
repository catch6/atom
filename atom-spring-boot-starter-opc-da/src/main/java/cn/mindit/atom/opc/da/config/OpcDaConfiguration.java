package cn.mindit.atom.opc.da.config;

import cn.hutool.core.util.StrUtil;
import cn.mindit.atom.opc.da.*;
import cn.mindit.atom.opc.da.util.OpcDaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIVariant;
import org.jspecify.annotations.NonNull;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.AutoReconnectState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.ServerList;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-07-24
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class OpcDaConfiguration implements ApplicationListener<ApplicationStartedEvent>, Ordered, DisposableBean {

    private final OpcDaProperties opcDaProperties;
    private final List<OpcDaSubscriber> opcDaSubscribers;
    private final List<AutoReconnectController> managedControllers = new ArrayList<>();

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        List<OpcDaProperties.OpcDaInstance> instances = opcDaProperties.getInstances();
        if (instances == null || instances.isEmpty()) {
            return;
        }

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Map<String, List<OpcDaConsumer>> consumerMap = OpcDaConsumerProcessor.processConsumerMap(applicationContext, opcDaProperties, opcDaSubscribers);

        for (OpcDaProperties.OpcDaInstance instance : instances) {
            if (!instance.getEnabled()) {
                continue;
            }
            try {
                if (StrUtil.isEmpty(instance.getClsId())) {
                    ServerList serverList = new ServerList(instance.getHost(), instance.getUser(), instance.getPassword(), instance.getDomain());
                    instance.setClsId(serverList.getClsIdFromProgId(instance.getProgId()));
                }
                ConnectionInformation ci = new ConnectionInformation();
                ci.setHost(instance.getHost());
                ci.setDomain(instance.getDomain());
                ci.setUser(instance.getUser());
                ci.setPassword(instance.getPassword());
                ci.setProgId(instance.getProgId());
                ci.setClsid(instance.getClsId());

                Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
                AutoReconnectController autoReconnectController = new AutoReconnectController(server);
                autoReconnectController.connect();
                managedControllers.add(autoReconnectController);

                WriteableAccessBase access;
                if (instance.isAsync()) {
                    JISystem.setJavaCoClassAutoCollection(false);
                    access = new WriteableAsync20Access(server, instance.getPeriod(), instance.getInitialRefresh());
                } else {
                    access = new WriteableSyncAccess(server, instance.getPeriod());
                }

                List<OpcDaConsumer> consumers = consumerMap.get(instance.getId());
                addListener(autoReconnectController, access, consumers);

                beanFactory.registerSingleton(OpcDaProperties.CLIENT_BEAN_PREFIX + instance.getId(), access);
                beanFactory.registerSingleton(OpcDaProperties.CONNECTION_BEAN_PREFIX + instance.getId(), autoReconnectController);
            } catch (Exception e) {
                throw new RuntimeException("OPC DA connect error: " + e.getMessage(), e);
            }
        }
    }

    public static void addListener(AutoReconnectController controller, WriteableAccessBase access, List<OpcDaConsumer> consumers) {
        controller.addListener(state -> {
            if (state == AutoReconnectState.DISABLED) {
                try {
                    controller.disconnect();
                } catch (Exception ignore) {
                }
                try {
                    controller.connect();
                } catch (Exception ignore) {
                }
            }
        });
        controller.addListener(state -> {
            if (log.isDebugEnabled()) {
                log.debug("AutoReconnectState: {}", state);
            }
            if (state == AutoReconnectState.CONNECTED) {
                if (consumers == null || consumers.isEmpty()) {
                    return;
                }
                for (OpcDaConsumer consumer : consumers) {
                    String[] items = consumer.getItems();
                    BiConsumer<String, String> itemsConsumer = consumer.getConsumer();
                    for (String item : items) {
                        try {
                            access.addItem(item, (it, itState) -> {
                                try {
                                    Short quality = itState.getQuality();
                                    if (quality == 192) { // 192 为 Good 信号,数据可以正常获取
                                        JIVariant jiVariant = itState.getValue();
                                        String value = OpcDaUtils.getString(jiVariant);
                                        itemsConsumer.accept(item, value);
                                    }
                                } catch (Exception e) {
                                    log.error("OPC DA invoke error", e);
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                access.bind();
            }
        });
    }

    @Override
    public int getOrder() {
        return opcDaProperties.getOrder();
    }

    @Override
    public void destroy() {
        for (AutoReconnectController controller : managedControllers) {
            try {
                controller.disconnect();
            } catch (Exception e) {
                log.warn("Failed to disconnect OPC DA controller", e);
            }
        }
        managedControllers.clear();
    }

}
