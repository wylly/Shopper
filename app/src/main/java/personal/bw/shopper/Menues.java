package personal.bw.shopper;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import personal.bw.shopper.productlist.housestock.HousestockActivity;
import personal.bw.shopper.productlist.scanbarcode.ScanBarcodeActivity;
import personal.bw.shopper.productlist.trashlist.TrashListActivity;
import personal.bw.shopper.shoppinglists.ShoppingListsActivity;


public class Menues
{

	public static void setupActionBar(BaseActivity parent)
	{
		Toolbar toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
		parent.setSupportActionBar(toolbar);
		ActionBar ab = parent.getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
		ab.setDisplayHomeAsUpEnabled(true);
	}

	public static DrawerLayout createDrawerMenu(BaseActivity parent, ActivitiesEnum current)
	{

		DrawerLayout drawerLayout = (DrawerLayout) parent.findViewById(R.id.drawer_layout);
		drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
		NavigationView navigationView = (NavigationView) drawerLayout.findViewById(R.id.drawer_menu);
		if (navigationView != null)
		{
			navigationView.setNavigationItemSelectedListener(getListener(current, drawerLayout, parent));
		}
		return drawerLayout;
	}


	private static OnNavigationItemSelectedListener getListener(
			final ActivitiesEnum current,
			final DrawerLayout mDrawerLayout,
			final BaseActivity parent)
	{
		return new OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem)
			{
				switch (menuItem.getItemId())
				{
					case R.id.shopping_lists_list_navigation_menu_item:
						if (!current.equals(ActivitiesEnum.SHOPPING_LISTS))
						{
							Intent intent = new Intent(parent, ShoppingListsActivity.class);
							parent.startActivity(intent);
						}
						break;
					case R.id.scan_barcode_navigation_menu_item:
						if (!current.equals(ActivitiesEnum.BARCODE))
						{
							Intent intent = new Intent(parent, ScanBarcodeActivity.class);
							parent.startActivity(intent);
						}

						break;
					case R.id.house_stock_list_navigation_menu_item:
						if (!current.equals(ActivitiesEnum.HOUSESTOCK))
						{
							Intent intent = new Intent(parent, HousestockActivity.class);
							parent.startActivity(intent);
						}
//
						break;

					case R.id.trash_list_navigation_menu_item:
						if (!current.equals(ActivitiesEnum.TRASH_LIST))
						{
							Intent intent = new Intent(parent, TrashListActivity.class);
							parent.startActivity(intent);
						}
						break;
					default:
						break;
				}
				// Close the navigation drawer when an item is selected.
				menuItem.setChecked(true);
				mDrawerLayout.closeDrawers();
				return true;
			}
		};
	}
}
