package personal.bw.shopper.productdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.ActivityUtils;
import personal.bw.shopper.Menues;
import personal.bw.shopper.R;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.shoppinglistdetails.ShoppingListDetailsFragment;

public class ProductDetailsActivity extends AppCompatActivity {
    private final static ActivitiesEnum CURRENT = ActivitiesEnum.NONE;
    private DrawerLayout drawerLayout;
    private ProductDetailsPresenter presenter;
    ProductDetailsFragment productDetailsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_menues_activity);

        drawerLayout = Menues.createDrawerMenu(this, CURRENT);
        Menues.setupActionBar(this);

        switch (getExtraCommand()) {
            case EDIT_PRODUCT: {
                presenter = new ProductDetailsPresenter(
                        setupProductDetailsFragment(),
                        DataSourceDealer.getINSTANCE(getApplicationContext()),
                       getIntent().getIntExtra(ShoppingListDetailsFragment.CLICKED_PRODUCT,0)
                );
                break;
            }
            case NEW_PRODUCT: {
                presenter = new ProductDetailsPresenter(
                        setupProductDetailsFragment(),
                        DataSourceDealer.getINSTANCE(getApplicationContext()),
                        -1
                );
                break;
            }
        }
    }

    private ShoppingListDetailsFragment.Action getExtraCommand() {
        return (ShoppingListDetailsFragment.Action) getIntent().getSerializableExtra(getString(R.string.intent_action));
    }

    private ProductDetailsFragment setupProductDetailsFragment() {
        productDetailsFragment = (ProductDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (productDetailsFragment == null) {
            // Create the fragment
            productDetailsFragment = new ProductDetailsFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), productDetailsFragment, R.id.contentFrame);
        }
        return productDetailsFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
