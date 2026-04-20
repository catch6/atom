package org.openscada.opc.lib.da;

import java.util.HashMap;
import java.util.Map;

public class AddFailedException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 5299486640366935298L;

	private Map<String, Integer> _errors = new HashMap<String, Integer>();

	private Map<String, Item> _items = new HashMap<String, Item>();

	public AddFailedException(final Map<String, Integer> errors, final Map<String, Item> items) {
		super();
		this._errors = errors;
		this._items = items;
	}

	/**
	 * Get the map of item id to error code
	 *
	 * @return the result map containing the failed items
	 */
	public Map<String, Integer> getErrors() {
		return this._errors;
	}

	/**
	 * Get the map of item it to item object
	 *
	 * @return the result map containing the succeeded items
	 */
	public Map<String, Item> getItems() {
		return this._items;
	}

}
