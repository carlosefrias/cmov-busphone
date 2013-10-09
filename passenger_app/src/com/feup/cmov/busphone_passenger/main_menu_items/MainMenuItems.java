package com.feup.cmov.busphone_passenger.main_menu_items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MainMenuItems {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<MainMenuItem> ITEMS = new ArrayList<MainMenuItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, MainMenuItem> ITEM_MAP = new HashMap<String, MainMenuItem>();

	static {
		// Add 3 sample items.
		addItem(new MainMenuItem("1", "My Tickets"));
		addItem(new MainMenuItem("2", "Buy Ticket"));
		//addItem(new MainMenuItem("3", "Item 3"));
	}

	private static void addItem(MainMenuItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class MainMenuItem {
		public String id;
		public String content;

		public MainMenuItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
