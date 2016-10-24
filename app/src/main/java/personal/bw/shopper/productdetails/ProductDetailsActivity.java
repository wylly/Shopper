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
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.source.DataSourceDealer;
import personal.bw.shopper.shoppinglistdetails.ShoppingListDetailsFragment;

public class ProductDetailsActivity extends AppCompatActivity {
    private final static ActivitiesEnum CURRENT = ActivitiesEnum.NONE;
    private DrawerLayout drawerLayout;
    private ProductDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_menues_activity);

        drawerLayout = Menues.setupDrawerMenu(this, CURRENT);

        presenter = new ProductDetailsPresenter(
                setupProductDetailsFragment(),
                DataSourceDealer.getINSTANCE(getApplicationContext()),
                getProduct(getExtraCommand())
        );
    }

    private Product getProduct(ShoppingListDetailsFragment.Action action) {
        switch (action) {
            case EDIT_PRODUCT: {
                return (Product) getIntent().getSerializableExtra(ShoppingListDetailsFragment.CLICKED_PRODUCT);
            }
            case NEW_PRODUCT: {
                return new Product(getString(R.string.new_product));
            }
        }
        return new Product(getString(R.string.new_product));
    }

    private ShoppingListDetailsFragment.Action getExtraCommand() {
        return (ShoppingListDetailsFragment.Action) getIntent().getSerializableExtra(getString(R.string.intent_action));
    }

    private ProductDetailsFragment setupProductDetailsFragment() {
        ProductDetailsFragment productDetailsFragment = (ProductDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (productDetailsFragment == null) {
            // Create the fragment
            productDetailsFragment = ProductDetailsFragment.newInstance();
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
