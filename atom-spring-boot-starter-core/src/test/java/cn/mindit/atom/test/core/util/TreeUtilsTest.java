/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.SortTreeNode;
import cn.mindit.atom.core.util.TreeNode;
import cn.mindit.atom.core.util.TreeUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TreeUtilsTest {

    static class Node implements TreeNode<Node, Long> {
        Long id;
        Long parentId;
        List<Node> children;

        Node(Long id, Long parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public Long getParentId() {
            return parentId;
        }

        @Override
        public List<Node> getChildren() {
            return children;
        }

        @Override
        public void setChildren(List<Node> children) {
            this.children = children;
        }
    }

    static class SortNode implements SortTreeNode<SortNode, Long, Integer> {
        Long id;
        Long parentId;
        Integer sort;
        List<SortNode> children;

        SortNode(Long id, Long parentId, Integer sort) {
            this.id = id;
            this.parentId = parentId;
            this.sort = sort;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public Long getParentId() {
            return parentId;
        }

        @Override
        public Integer getSort() {
            return sort;
        }

        @Override
        public List<SortNode> getChildren() {
            return children;
        }

        @Override
        public void setChildren(List<SortNode> children) {
            this.children = children;
        }
    }

    @Test
    void buildTreeAssemblesChildrenUnderRoot() {
        List<Node> flat = List.of(
            new Node(1L, 0L),
            new Node(2L, 1L),
            new Node(3L, 1L),
            new Node(4L, 2L)
        );
        List<Node> roots = TreeUtils.buildTree(flat, 0L);
        assertThat(roots).hasSize(1);
        Node root = roots.get(0);
        assertThat(root.getId()).isEqualTo(1L);
        assertThat(root.getChildren()).extracting(Node::getId).containsExactlyInAnyOrder(2L, 3L);
        Node n2 = root.getChildren().stream().filter(n -> n.getId().equals(2L)).findFirst().orElseThrow();
        assertThat(n2.getChildren()).extracting(Node::getId).containsExactly(4L);
    }

    @Test
    void buildTreeReturnsNullForUnknownRoot() {
        List<Node> flat = List.of(new Node(1L, 0L));
        assertThat(TreeUtils.buildTree(flat, 999L)).isNull();
    }

    @Test
    void buildSortTreeOrdersChildrenBySortAscending() {
        List<SortNode> flat = List.of(
            new SortNode(1L, 0L, 1),
            new SortNode(2L, 1L, 3),
            new SortNode(3L, 1L, 1),
            new SortNode(4L, 1L, 2)
        );
        List<SortNode> roots = TreeUtils.buildSortTree(flat, 0L);
        assertThat(roots).hasSize(1);
        assertThat(roots.get(0).getChildren())
            .extracting(SortNode::getSort)
            .containsExactly(1, 2, 3);
    }

}
