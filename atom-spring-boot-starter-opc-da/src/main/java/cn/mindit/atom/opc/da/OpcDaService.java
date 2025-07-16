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

package cn.mindit.atom.opc.da;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.opc.da.config.OpcDaConfiguration;
import cn.mindit.atom.opc.da.config.OpcDaProperties;
import cn.mindit.atom.opc.da.util.OpcDaUtils;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.Item;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-06-21
 */
@Slf4j
@RequiredArgsConstructor
public class OpcDaService {

    private final OpcDaProperties opcDaProperties;
    private final ApplicationContext applicationContext;

    public void subscriberItem(String item, BiConsumer<String, String> consumer) {
        subscriberItem(opcDaProperties.getId(), new String[]{item}, consumer);
    }

    public void subscriberItem(String id, String item, BiConsumer<String, String> consumer) {
        subscriberItem(id, new String[]{item}, consumer);
    }

    public void subscriberItem(Collection<String> item, BiConsumer<String, String> consumer) {
        subscriberItem(opcDaProperties.getId(), item.toArray(new String[0]), consumer);
    }

    public void subscriberItem(String id, Collection<String> item, BiConsumer<String, String> consumer) {
        subscriberItem(id, item.toArray(new String[0]), consumer);
    }

    public void subscriberItem(String[] items, BiConsumer<String, String> consumer) {
        subscriberItem(opcDaProperties.getId(), items, consumer);
    }

    public void subscriberItem(String id, String[] items, BiConsumer<String, String> consumer) {
        if (items == null || items.length == 0) {
            return;
        }
        AutoReconnectController controller = applicationContext.getBean(OpcDaProperties.CONNECTION_BEAN_PREFIX + id, AutoReconnectController.class);
        WriteableAccessBase access = applicationContext.getBean(OpcDaProperties.CLIENT_BEAN_PREFIX + id, WriteableSyncAccess.class);
        OpcDaConsumer opcDaConsumer = new OpcDaConsumer(id, items, consumer);
        OpcDaConfiguration.addListener(controller, access, List.of(opcDaConsumer));
    }

    public String readItem(String item) {
        return readItem(opcDaProperties.getId(), item);
    }

    public String readItem(String id, String item) {
        WriteableAccessBase access = applicationContext.getBean(OpcDaProperties.CLIENT_BEAN_PREFIX + id, WriteableSyncAccess.class);
        Item cachedItem = access.getItem(item);
        try {
            JIVariant jiVariant = cachedItem.read(false).getValue();
            return OpcDaUtils.getString(jiVariant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateItem(String item, Object value) {
        updateItem(opcDaProperties.getId(), item, value);
    }

    public void updateItem(String id, String item, Object value) {
        WriteableAccessBase access = applicationContext.getBean(OpcDaProperties.CLIENT_BEAN_PREFIX + id, WriteableSyncAccess.class);
        access.updateItem(item, value);
    }

    public void removeItem(String item) {
        removeItem(opcDaProperties.getId(), item);
    }

    public void removeItem(String id, String item) {
        WriteableAccessBase access = applicationContext.getBean(OpcDaProperties.CLIENT_BEAN_PREFIX + id, WriteableSyncAccess.class);
        access.removeItem(item);
    }

}
