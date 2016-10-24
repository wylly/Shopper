package personal.bw.shopper.shoppinglists;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.ActivityUtils;
import personal.bw.shopper.Menues;
import personal.bw.shopper.R;
import personal.bw.shopper.data.source.DataSourceDealer;

public class ShoppingListsActivity extends AppCompatActivity {
    private final static ActivitiesEnum CURRENT = ActivitiesEnum.LISTS;

    private DrawerLayout drawerLayout;

    private ShoppingListsPresenter shoppingListsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_menues_activity);

        Menues.setupActionBar(this);
        drawerLayout = Menues.setupDrawerMenu(this, CURRENT);

        // Create the presenter
        shoppingListsPresenter = new ShoppingListsPresenter(
                setupShoppingListsFragment(),
                DataSourceDealer.getINSTANCE(getApplicationContext())
        );
    }

    private ShoppingListsFragment setupShoppingListsFragment() {
        ShoppingListsFragment shoppingListsFragment = (ShoppingListsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (shoppingListsFragment == null) {
            // Create the fragment
            shoppingListsFragment = ShoppingListsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), shoppingListsFragment, R.id.contentFrame);
        }
        return shoppingListsFragment;
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
