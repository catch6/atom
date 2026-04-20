package cn.mindit.atom.opc.ua;

/**
 * @author Catch
 * @since 2024-06-25
 */
public interface OpcUaSubscriber {

    /**
     * 实例 ID, 为空则使用 OpcUaProperties.getId()
     */
    default String id() {
        return "";
    }

    /**
     * 订阅项目
     */
    String[] items();

    /**
     * 命名空间索引, 默认 0
     */
    default int[] namespaceIndices() {
        return new int[]{0};
    }

    /**
     * 订阅消息
     *
     * @param item  项目
     * @param value 项目值
     */
    void message(String item, String value);

}
