package personal.bw.shopper.productlist.trashlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepositoryImpl;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;

import static personal.bw.shopper.ActivitiesEnum.TRASH_LIST;

public class TrashListActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new TrashListPresenter(
				(ShoppingListDetailsContract.View) setupViewFragment(),
				DataSourceDealer.getINSTANCE(getApplicationContext()),
				new StringResourcesRepositoryImpl(getBaseContext()),
				getIntent());
	}

	@Override
	public Fragment createFragment()
	{
		return new TrashListFragment();
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return TRASH_LIST;
	}
}
