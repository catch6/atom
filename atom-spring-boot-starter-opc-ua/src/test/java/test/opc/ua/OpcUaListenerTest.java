package test.opc.ua;

import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.opc.ua.OpcUaListener;
import org.springframework.stereotype.Component;

/**
 * @author Catch
 * @since 2024-08-07
 */
@Slf4j
@Component
public class OpcUaListenerTest {

    @OpcUaListener(items = "Server.ServerStatus.CurrentTime")
    public void onItemChange(String item, String value) {
        log.info("收到 OPC UA 数据: " + item + " = " + value);
    }

}
