package cn.mindit.atom.opc.ua;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.opc.ua.config.OpcUaProperties;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Catch
 * @since 2024-08-05
 */
@Slf4j
@RequiredArgsConstructor
public class OpcUaService {

    private static final long DEFAULT_TIMEOUT_SECONDS = 5L;

    private final ApplicationContext applicationContext;
    private final OpcUaProperties opcUaProperties;

    public String readItem(int namespaceIndex, String item) {
        return readItem(opcUaProperties.getId(), namespaceIndex, item);
    }

    public String readItem(String id, int namespaceIndex, String item) {
        OpcUaClient client = applicationContext.getBean(OpcUaProperties.CLIENT_BEAN_PREFIX + id, OpcUaClient.class);
        NodeId nodeId = new NodeId(namespaceIndex, item);
        try {
            DataValue dataValue = client.readValue(0.0, TimestampsToReturn.Neither, nodeId)
                .get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return JsonUtils.toJson(dataValue.getValue().getValue());
        } catch (TimeoutException e) {
            throw new RuntimeException("OPC UA read timeout: id=" + id + ", item=" + item, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("OPC UA read interrupted: id=" + id + ", item=" + item, e);
        } catch (Exception e) {
            throw new RuntimeException("OPC UA read failed: id=" + id + ", item=" + item, e);
        }
    }

    public void writeItem(int namespaceIndex, String item, Object value) {
        writeItem(opcUaProperties.getId(), namespaceIndex, item, value);
    }

    public void writeItem(String id, int namespaceIndex, String item, Object value) {
        OpcUaClient client = applicationContext.getBean(OpcUaProperties.CLIENT_BEAN_PREFIX + id, OpcUaClient.class);
        NodeId nodeId = new NodeId(namespaceIndex, item);
        DataValue newValue = new DataValue(new Variant(value));
        try {
            // 等待异步写完成,避免写操作未真正落地就返回.
            client.writeValue(nodeId, newValue).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException("OPC UA write timeout: id=" + id + ", item=" + item, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("OPC UA write interrupted: id=" + id + ", item=" + item, e);
        } catch (Exception e) {
            throw new RuntimeException("OPC UA write failed: id=" + id + ", item=" + item, e);
        }
    }

}
