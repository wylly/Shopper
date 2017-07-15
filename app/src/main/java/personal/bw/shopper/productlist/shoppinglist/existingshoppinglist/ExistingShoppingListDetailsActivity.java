package personal.bw.shopper.productlist.shoppinglist.existingshoppinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepositoryImpl;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;
import personal.bw.shopper.productlist.shoppinglist.ShoppingListDetailsFragment;

import static personal.bw.shopper.ActivitiesEnum.SHOPPING_LIST_DETAILS;

public class ExistingShoppingListDetailsActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new ExisitngShoppingListDetailsPresenter(
				(ShoppingListDetailsContract.View) setupViewFragment(),
				DataSourceDealer.getINSTANCE(getApplicationContext()),
				new StringResourcesRepositoryImpl(getBaseContext()),
				getIntent());
	}

	@Override
	public Fragment createFragment()
	{
		return new ShoppingListDetailsFragment();
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return SHOPPING_LIST_DETAILS;
	}

}
