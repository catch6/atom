package cn.mindit.atom.core.util;

/**
 * @author Catch
 * @since 2024-10-16
 */
public interface SortTreeNode<E, K, S extends Comparable<S>> extends TreeNode<E, K> {

    S getSort();

}
