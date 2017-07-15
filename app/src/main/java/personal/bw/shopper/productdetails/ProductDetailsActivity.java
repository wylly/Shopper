package personal.bw.shopper.productdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.R;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.productlist.BaseProductListFragment;

import static personal.bw.shopper.ActivitiesEnum.PRODUCT_DETAILS;
import static personal.bw.shopper.productlist.BaseProductListFragment.CLICKED_PRODUCT;

public class ProductDetailsActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		switch (getExtraCommand())
		{
			case EDIT_PRODUCT:
			{
				new ProductDetailsPresenter(
						(ProductDetailsContract.View) setupViewFragment(),
						DataSourceDealer.getINSTANCE(getApplicationContext()),
						retrieveEditedItemId()
				);
				break;
			}
			case NEW_PRODUCT:
			{
				new NewProductDetailsPresenter(
						(ProductDetailsContract.View) setupViewFragment(),
						DataSourceDealer.getINSTANCE(getApplicationContext())
				);
				break;
			}
		}
	}

	private BaseProductListFragment.Action getExtraCommand()
	{
		return (BaseProductListFragment.Action) getIntent().getSerializableExtra(getString(R.string.intent_action));
	}

	private int retrieveEditedItemId()
	{
		return getIntent().getIntExtra(CLICKED_PRODUCT, 0);
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return PRODUCT_DETAILS;
	}

	@Override
	public Fragment createFragment()
	{
		return new ProductDetailsFragment();
	}
}
