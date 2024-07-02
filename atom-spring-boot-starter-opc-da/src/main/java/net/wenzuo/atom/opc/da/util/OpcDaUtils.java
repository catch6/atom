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

package net.wenzuo.atom.opc.da.util;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import org.openscada.opc.lib.da.browser.Leaf;
import org.openscada.opc.lib.da.browser.TreeBrowser;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.Executors;

/**
 * @author Catch
 * @since 2024-06-23
 */
public class OpcDaUtils {

	private OpcDaUtils() {
	}

	public static void showServerList(String host, String domain, String user, String password) {
		try {
			ServerList serverList = new ServerList(host, user, password, domain);
			Collection<ClassDetails> details = serverList.listServersWithDetails(new Category[]{Categories.OPCDAServer20}, new Category[]{});
			for (ClassDetails detail : details) {
				System.out.println("==================================================================");
				System.out.println("ProgId: " + detail.getProgId() + ", ClsId: " + detail.getClsId() + ", Description: " + detail.getDescription());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void treeBrowser(String host, String domain, String user, String password, String progId) {
		try {
			Server server = getServer(host, domain, user, password, progId);
			server.connect();
			TreeBrowser treeBrowser = server.getTreeBrowser();
			Branch browse = treeBrowser.browse();
			Collection<Branch> branches = browse.getBranches();
			System.out.println("==================================================================");
			dumpTree(browse, 0);
			for (Branch branch : branches) {
				System.out.println("Branch: " + branch.getName());
				Collection<Leaf> leaves = branch.getLeaves();
				for (Leaf leaf : leaves) {
					System.out.println("    Leaf: " + leaf.getName() + " [" + leaf.getItemId() + "]");
				}
			}
			server.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void dumpTree(final Branch branch, final int level) {
		String indent = "  ".repeat(Math.max(0, level));
		for (final Leaf leaf : branch.getLeaves()) {
			System.out.println(indent + "Leaf: " + leaf.getName() + " [" + leaf.getItemId() + "]");
		}
		for (final Branch subBranch : branch.getBranches()) {
			System.out.println(indent + "Branch: " + subBranch.getName());
			dumpTree(subBranch, level + 1);
		}
	}

	public static void dumpItemState(final Item item, final ItemState state) {
		System.out.printf("Item: %s, Value: %s, Timestamp: %tc, Quality: %d%n", item.getId(), state.getValue(), state.getTimestamp(), state.getQuality());
	}

	private static Server getServer(String host, String domain, String user, String password, String progId) throws UnknownHostException, JIException, AlreadyConnectedException {
		ServerList serverList = new ServerList(host, user, password, domain);

		ConnectionInformation ci = new ConnectionInformation();
		ci.setHost(host);
		ci.setDomain(domain);
		ci.setUser(user);
		ci.setPassword(password);
		ci.setProgId(progId);
		ci.setClsid(serverList.getClsIdFromProgId(progId));

		return new Server(ci, Executors.newSingleThreadScheduledExecutor());
	}

	public static void flatBrowser(String host, String domain, String user, String password, String progId) {
		try {
			Server server = getServer(host, domain, user, password, progId);
			server.connect();
			FlatBrowser flatBrowser = server.getFlatBrowser();
			Collection<String> tags = flatBrowser.browse();
			System.out.println("==================================================================");
			for (String tag : tags) {
				System.out.println(tag);
			}
			server.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
