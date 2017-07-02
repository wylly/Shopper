package personal.bw.shopper.shoppinglists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.data.datasource.DataSourceDealer;

public class ShoppingListsActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new ShoppingListsPresenter(
				(ShoppingListsContract.View) setupViewFragment(),
				DataSourceDealer.getINSTANCE(getApplicationContext())
		);
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return ActivitiesEnum.SHOPPING_LISTS;
	}

	@Override
	public Fragment createFragment()
	{
		return new ShoppingListsFragment();
	}
}
