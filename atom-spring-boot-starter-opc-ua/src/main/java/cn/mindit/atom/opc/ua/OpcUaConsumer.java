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

package cn.mindit.atom.opc.ua;

import lombok.Data;
import cn.mindit.atom.opc.ua.config.OpcUaProperties;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-08-05
 */
@Data
public class OpcUaConsumer {

    /**
     * 实例 ID, 为 null 则使用 {@link OpcUaProperties#getId()}
     */
    private String id;

    /**
     * 项目
     */
    private String[] items;

    /**
     * 命名空间索引, 默认 0
     */
    private int[] namespaceIndices;

    /**
     * 消费对象
     */
    private BiConsumer<String, String> consumer;

    public OpcUaConsumer() {
    }

    public OpcUaConsumer(String id, String[] items, int[] namespaceIndices, BiConsumer<String, String> consumer) {
        this.id = id;
        this.items = items;
        this.namespaceIndices = namespaceIndices;
        this.consumer = consumer;
    }

    public void initialize() {
        check();
        alignLength();
    }

    public void check() {
        Assert.notNull(id, "OPC UA id must not be null");
        Assert.notEmpty(items, "OPC UA items must not be empty");
        Assert.isTrue(namespaceIndices != null && namespaceIndices.length > 0, "OPC UA namespaceIndices must not be empty");
    }

    public void alignLength() {
        if (namespaceIndices.length == items.length) {
            return;
        }
        if (namespaceIndices.length == 1) {
            int[] targetNamespaceIndices = new int[items.length];
            Arrays.fill(targetNamespaceIndices, namespaceIndices[0]);
            namespaceIndices = targetNamespaceIndices;
        }
        throw new RuntimeException("OPC UA namespaceIndices length must be 1 or equal to items length");
    }

}
