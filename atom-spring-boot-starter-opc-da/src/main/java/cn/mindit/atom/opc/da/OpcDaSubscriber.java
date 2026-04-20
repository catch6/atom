package cn.mindit.atom.opc.da;

/**
 * @author Catch
 * @since 2024-06-25
 */
public interface OpcDaSubscriber {

    /**
     * 实例 ID, 为 null 则使用 OpcDaProperties.getId()
     */
    default String id() {
        return "";
    }

    /**
     * 订阅项目
     */
    String[] items();

    /**
     * 订阅消息
     *
     * @param item  项目
     * @param value 项目值
     */
    void message(String item, String value);

}
