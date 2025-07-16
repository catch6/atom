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

import lombok.Data;
import cn.mindit.atom.opc.da.config.OpcDaProperties;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Data
public class OpcDaConsumer {

    /**
     * 实例 ID, 为 null 则使用 {@link OpcDaProperties#getId()}
     */
    private String id;

    /**
     * 项目
     */
    private String[] items;

    /**
     * 消费对象
     */
    private BiConsumer<String, String> consumer;

    public OpcDaConsumer() {
    }

    public OpcDaConsumer(String id, String[] items, BiConsumer<String, String> consumer) {
        this.id = id;
        this.items = items;
        this.consumer = consumer;
    }

    public void initialize() {
        check();
    }

    public void check() {
        Assert.notNull(id, "OPC DA id must not be null");
        Assert.notEmpty(items, "OPC DA items must not be empty");
    }

}
