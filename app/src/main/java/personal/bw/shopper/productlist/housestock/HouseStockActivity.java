package personal.bw.shopper.productlist.housestock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepositoryImpl;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;

import static personal.bw.shopper.ActivitiesEnum.HOUSESTOCK;

public class HouseStockActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new HousestockProductsPresenter(
				(ShoppingListDetailsContract.View) setupViewFragment(),
				DataSourceDealer.getINSTANCE(getApplicationContext()),
				new StringResourcesRepositoryImpl(getBaseContext()),
				getIntent());
	}

	@Override
	public Fragment createFragment()
	{
		return new HousestockFragment();
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return HOUSESTOCK;
	}

}
