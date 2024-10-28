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

package net.wenzuo.atom.core.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2024-10-16
 */
public class TreeUtils {

	private TreeUtils() {
	}

	/**
	 * 构建树形结构
	 *
	 * @param nodes  平铺的节点列表
	 * @param rootId 根节点的ID
	 * @param <T>    节点类型
	 * @return 根节点
	 */
	public static <T extends TreeNode<T, P>, P> List<T> buildTree(List<T> nodes, P rootId) {
		Map<P, List<T>> parentMap = nodes.stream().collect(Collectors.groupingBy(TreeNode::getParentId));
		for (T node : nodes) {
			List<T> children = parentMap.get(node.getId());
			if (children != null) {
				node.setChildren(children);
			}
		}
		return parentMap.get(rootId);
	}

	/**
	 * 构建有序树形结构
	 *
	 * @param nodes  平铺的节点列表
	 * @param rootId 根节点的ID
	 * @param <T>    节点类型
	 * @return 根节点
	 */
	public static <T extends SortTreeNode<T, P>, P> List<T> buildSortTree(List<T> nodes, P rootId) {
		List<T> tree = buildTree(nodes, rootId);
		sortChildren(tree);
		return tree;
	}

	/**
	 * 递归对节点进行排序
	 *
	 * @param nodes 节点列表
	 * @param <T>   节点类型
	 */
	public static <T extends SortTreeNode<T, P>, P> void sortChildren(List<T> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			return;
		}
		nodes.sort(Comparator.comparing(SortTreeNode::getSort));
		for (SortTreeNode<T, P> node : nodes) {
			sortChildren(node.getChildren());
		}
	}

}
