package com.feup.cmov.busphone_passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * An activity representing a list of ItemOptions. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemOptionDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemOptionListFragment} and the item details (if present) is a
 * {@link ItemOptionDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemOptionListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ItemOptionListActivity extends FragmentActivity implements
		ItemOptionListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_list);

		if (findViewById(R.id.itemoption_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemOptionListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.itemoption_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ItemOptionListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ItemOptionDetailFragment.ARG_ITEM_ID, id);
			
			if(id.equals("1")){
				//Implement the show tickets fragment
				Toast.makeText(this, "It's true!! Will show all tickets!", Toast.LENGTH_LONG).show();
			}
			else if(id.equals("2")){
				//Implement the buy tickets fragment
				Toast.makeText(this, "It's true!! Will show a way to buy tickets!", Toast.LENGTH_LONG).show();
			}
			
			ItemOptionDetailFragment fragment = new ItemOptionDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.itemoption_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					ItemOptionDetailActivity.class);
			detailIntent.putExtra(ItemOptionDetailFragment.ARG_ITEM_ID, id);
			
			if(id.equals("1")){
				//Implement the show tickets fragment
				Toast.makeText(this, "It's true!! Will show all tickets!", Toast.LENGTH_LONG).show();
			}
			else if(id.equals("2")){
				//Implement the buy tickets fragment
				Toast.makeText(this, "It's true!! Will show a way to buy tickets!", Toast.LENGTH_LONG).show();
			}
			
			startActivity(detailIntent);
		}
	}
}
