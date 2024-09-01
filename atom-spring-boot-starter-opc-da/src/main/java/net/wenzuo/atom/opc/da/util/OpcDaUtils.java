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

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author Catch
 * @since 2024-06-23
 */
@Slf4j
public class OpcDaUtils {

	private OpcDaUtils() {
	}

	public static void showDetails(String host, String user, String password) {
		showDetails(host, "", user, password);
	}

	public static void showDetails(String host, String domain, String user, String password) {
		try {
			ServerList serverList = new ServerList(host, user, password, domain);
			Collection<ClassDetails> details = serverList.listServersWithDetails(new Category[]{Categories.OPCDAServer10, Categories.OPCDAServer20, Categories.OPCDAServer30}, new Category[]{});
			for (ClassDetails detail : details) {
				log.info("ClassDetails - ProgId: {}, Description: {}, ClsId: {}", detail.getProgId(), detail.getDescription(), detail.getClsId());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void showItemList(String host, String user, String password, String progId) {
		showItemList(host, "", user, password, progId);
	}

	public static void showItemList(String host, String domain, String user, String password, String progId) {
		try {
			Server server = getServer(host, domain, user, password, progId);
			server.connect();
			FlatBrowser flatBrowser = server.getFlatBrowser();
			Collection<String> items = flatBrowser.browse();
			for (String item : items) {
				log.info(item);
			}
			server.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void showItemTree(String host, String user, String password, String progId) {
		showItemTree(host, "", user, password, progId);
	}

	public static void showItemTree(String host, String domain, String user, String password, String progId) {
		try {
			Server server = getServer(host, domain, user, password, progId);
			server.connect();
			TreeBrowser treeBrowser = server.getTreeBrowser();
			Branch branch = treeBrowser.browse();
			showItemTree(List.of(branch), "");
			server.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void showItemTree(List<Branch> branches, String prefix) {
		try {
			for (int i = 0; i < branches.size(); i++) {
				Branch branch = branches.get(i);
				if (i < branches.size() - 1) {
					log.info("{}{}{}", prefix, "├── ", branch.getName());
				} else {
					log.info("{}{}{}", prefix, "└── ", branch.getName());
				}
				prefix += "│   ";
				List<Leaf> leaves = branch.getLeaves().stream().toList();
				for (int j = 0; j < leaves.size(); j++) {
					Leaf leaf = leaves.get(j);
					if (j < leaves.size() - 1) {
						log.info("{}{}{}", prefix, "├── ", leaf.getName());
					} else {
						log.info("{}{}{}", prefix, "└── ", leaf.getName());
					}
				}
				List<Branch> subBranches = branch.getBranches().stream().toList();
				showItemTree(subBranches, prefix);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void showItemState(final Item item, final ItemState state) {
		log.info("Item: {}, Value: {}, Timestamp: {}, Quality: {}", item.getId(), state.getValue(), state.getTimestamp(), state.getQuality());
	}

	public static Server getServer(String host, String user, String password, String progId) throws UnknownHostException, JIException, AlreadyConnectedException {
		return getServer(host, "", user, password, progId);
	}

	public static Server getServer(String host, String domain, String user, String password, String progId) throws UnknownHostException, JIException, AlreadyConnectedException {
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

	public static String getString(JIVariant jiVariant) {
		if (jiVariant == null) {
			return null;
		}
		try {
			int type = jiVariant.getType();
			switch (type) {
				case JIVariant.VT_I2:
					short shortValue = jiVariant.getObjectAsShort();
					return String.valueOf(shortValue);
				case JIVariant.VT_I4:
					int intValue = jiVariant.getObjectAsInt();
					return String.valueOf(intValue);
				case JIVariant.VT_I8:
					long longValue = jiVariant.getObjectAsLong();
					return String.valueOf(longValue);
				case JIVariant.VT_R4:
					float floatValue = jiVariant.getObjectAsFloat();
					return String.valueOf(floatValue);
				case JIVariant.VT_R8:
					double doubleValue = jiVariant.getObjectAsDouble();
					return String.valueOf(doubleValue);
				case JIVariant.VT_BOOL:
					boolean boolValue = jiVariant.getObjectAsBoolean();
					return String.valueOf(boolValue);
				case JIVariant.VT_BSTR:
					return jiVariant.getObjectAsString2();
				case JIVariant.VT_DATE:
					Date dateValue = jiVariant.getObjectAsDate();
					return JsonUtils.toJson(dateValue);
				default:
					Object object = jiVariant.getObject();
					return JsonUtils.toJson(object);
			}
		} catch (JIException e) {
			throw new RuntimeException(e);
		}
	}

	public static JIVariant getJIVariant(Object value) {
		JIVariant jiVariant;
		if (value instanceof Short) {
			jiVariant = new JIVariant((short) value);
		} else if (value instanceof Integer) {
			jiVariant = new JIVariant((int) value);
		} else if (value instanceof Long) {
			jiVariant = new JIVariant((long) value);
		} else if (value instanceof Float) {
			jiVariant = new JIVariant((float) value);
		} else if (value instanceof Double) {
			jiVariant = new JIVariant((double) value);
		} else if (value instanceof Boolean) {
			jiVariant = new JIVariant((boolean) value);
		} else if (value instanceof String) {
			jiVariant = new JIVariant((String) value);
		} else if (value instanceof Date) {
			jiVariant = new JIVariant((Date) value);
		} else {
			jiVariant = new JIVariant(JsonUtils.toJson(value));
		}
		return jiVariant;
	}

}
