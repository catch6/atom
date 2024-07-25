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

package org.openscada.opc.lib.da.browser;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Branch {

	private Branch _parent = null;

	private String _name = null;

	private Collection<Branch> _branches = new LinkedList<Branch>();

	private Collection<Leaf> _leaves = new LinkedList<Leaf>();

	/**
	 * Create a branch to the virtual root folder
	 */
	public Branch() {
		super();
	}

	/**
	 * Create a branch with a parent branch and a name of this branch.
	 *
	 * @param parent The parent of this branch
	 * @param name   The name of this branch
	 */
	public Branch(final Branch parent, final String name) {
		super();
		this._name = name;
		this._parent = parent;
	}

	/**
	 * Get all branches.
	 * <br/>
	 * They must be filled first with a fill method from the {@link TreeBrowser}
	 *
	 * @return The list of branches
	 */
	public Collection<Branch> getBranches() {
		return this._branches;
	}

	public void setBranches(final Collection<Branch> branches) {
		this._branches = branches;
	}

	/**
	 * Get all leaves.
	 * <br/>
	 * They must be filled first with a fill method from the {@link TreeBrowser}
	 *
	 * @return The list of leaves
	 */
	public Collection<Leaf> getLeaves() {
		return this._leaves;
	}

	public void setLeaves(final Collection<Leaf> leaves) {
		this._leaves = leaves;
	}

	public String getName() {
		return this._name;
	}

	public void setName(final String name) {
		this._name = name;
	}

	public Branch getParent() {
		return this._parent;
	}

	/**
	 * Get the list of names from the parent up to this branch
	 *
	 * @return The stack of branch names from the parent up this one
	 */
	public Collection<String> getBranchStack() {
		LinkedList<String> branches = new LinkedList<String>();

		Branch currentBranch = this;
		while (currentBranch.getParent() != null) {
			branches.add(currentBranch.getName());
			currentBranch = currentBranch.getParent();
		}

		Collections.reverse(branches);
		return branches;
	}

}
