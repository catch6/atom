/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
