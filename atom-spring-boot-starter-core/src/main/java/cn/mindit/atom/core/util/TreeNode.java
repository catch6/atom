package cn.mindit.atom.core.util;

import java.util.List;

/**
 * @author Catch
 * @since 2024-10-16
 */
public interface TreeNode<E, K> {

    K getId();

    K getParentId();

    List<E> getChildren();

    void setChildren(List<E> children);

}
