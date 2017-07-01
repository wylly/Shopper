package personal.bw.shopper.productdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.R;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.shoppinglistdetails.ShoppingListDetailsFragment;

import static personal.bw.shopper.ActivityUtils.addFragmentToActivity;
import static personal.bw.shopper.Menues.createDrawerMenu;
import static personal.bw.shopper.Menues.setupActionBar;

public class ProductDetailsActivity extends AppCompatActivity
{
	private final static ActivitiesEnum CURRENT = ActivitiesEnum.NONE;
	private DrawerLayout drawerLayout;
	private ProductDetailsContract.Presenter presenter;
	private ProductDetailsFragment productDetailsFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_with_menues_activity);

		drawerLayout = createDrawerMenu(this, CURRENT);
		setupActionBar(this);

		switch (getExtraCommand())
		{
			case EDIT_PRODUCT:
			{
				presenter = new ProductDetailsPresenter(
						setupProductDetailsFragment(),
						DataSourceDealer.getINSTANCE(getApplicationContext()),
						retrieveEditedItemId()
				);
				break;
			}
			case NEW_PRODUCT:
			{
				presenter = new NewProductDetailsPresenter(
						setupProductDetailsFragment(),
						DataSourceDealer.getINSTANCE(getApplicationContext())
				);
				break;
			}
		}
	}

	private int retrieveEditedItemId()
	{
		return getIntent().getIntExtra(ShoppingListDetailsFragment.CLICKED_PRODUCT, 0);
	}

	private ShoppingListDetailsFragment.Action getExtraCommand()
	{
		return (ShoppingListDetailsFragment.Action) getIntent().getSerializableExtra(getString(R.string.intent_action));
	}

	private ProductDetailsFragment setupProductDetailsFragment()
	{
		productDetailsFragment = (ProductDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if (productDetailsFragment == null)
		{
			productDetailsFragment = new ProductDetailsFragment();
			addFragmentToActivity(getSupportFragmentManager(), productDetailsFragment, R.id.contentFrame);
		}
		return productDetailsFragment;
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
}
