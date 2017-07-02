package personal.bw.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import static personal.bw.shopper.ActivityUtils.addFragmentToActivity;
import static personal.bw.shopper.Menues.createDrawerMenu;
import static personal.bw.shopper.Menues.setupActionBar;

public abstract class BaseActivity extends AppCompatActivity
{
	private DrawerLayout drawerLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_with_menues_activity);
		setupActionBar(this);
		drawerLayout = createDrawerMenu(this, getCurrentActivity());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// Open the navigation drawer when the home icon is selected from the toolbar.
				drawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected Fragment setupViewFragment()
	{
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if (fragment == null)
		{
			fragment = createFragment();
			addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
		}
		return fragment;
	}

	public abstract ActivitiesEnum getCurrentActivity();

	public abstract Fragment createFragment();
}
