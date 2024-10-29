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
	 * @param <E>    节点类型
	 * @return 根节点
	 */
	public static <E extends TreeNode<E, K>, K> List<E> buildTree(List<E> nodes, K rootId) {
		Map<K, List<E>> parentMap = nodes.stream().collect(Collectors.groupingBy(TreeNode::getParentId));
		for (E node : nodes) {
			List<E> children = parentMap.get(node.getId());
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
	 * @param <E>    节点类型
	 * @return 根节点
	 */
	public static <E extends SortTreeNode<E, K>, K> List<E> buildSortTree(List<E> nodes, K rootId) {
		Map<K, List<E>> parentMap = nodes.stream().collect(Collectors.groupingBy(TreeNode::getParentId,
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> list.stream()
							.sorted(Comparator.comparingInt(SortTreeNode::getSort))
							.collect(Collectors.toList())
			)));
		for (E node : nodes) {
			List<E> children = parentMap.get(node.getId());
			if (children != null) {
				node.setChildren(children);
			}
		}
		return parentMap.get(rootId);
	}

}
