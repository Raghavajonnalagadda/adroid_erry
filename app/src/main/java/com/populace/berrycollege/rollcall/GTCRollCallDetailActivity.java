package com.populace.berrycollege.rollcall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.populace.berrycollege.R;
import com.populace.berrycollege.activities.MainActivity;
import com.populace.berrycollege.activities.Social_Activity;
import com.populace.berrycollege.fragments.SgaFragment;

/**
 * An activity representing a single GTCRollCall detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a
 * {@link GTCRollCallListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link GTCRollCallDetailFragment}.
 */
public class GTCRollCallDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollcall_detail);
		Social_Activity obj = new Social_Activity();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		// Show the Up button in the action bar.
		/*getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		 ImageView logo = (ImageView) findViewById(android.R.id.home);
	        logo.setImageResource(R.drawable.trans);*/
		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(
					GTCRollCallDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							GTCRollCallDetailFragment.ARG_ITEM_ID));
			GTCRollCallDetailFragment fragment = new GTCRollCallDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.rollcall_detail_container, fragment).commit();
		}
	}
@Override
public boolean onCreateOptionsMenu(Menu menu){
	getMenuInflater().inflate(R.menu.rollcall_menu, menu);
	return true;
}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					GTCRollCallListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



}
