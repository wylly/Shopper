package personal.bw.shopper.shoppinglistdetails;

import android.app.Activity;
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
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.source.DataSourceDealer;
import personal.bw.shopper.shoppinglists.ShoppingListsFragment;

public class ShoppingListDetailsActivity extends AppCompatActivity {
    private static final ActivitiesEnum CURRENT = ActivitiesEnum.NONE;
    private DrawerLayout drawerLayout;
    private ShoppingListDetailsPresenter shoppingListDetailsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_menues_activity);

        Menues.setupActionBar(this);
        drawerLayout = Menues.setupDrawerMenu(this,CURRENT);
        shoppingListDetailsPresenter = new ShoppingListDetailsPresenter(
                setupShoppigListDetailsFragment(),
                DataSourceDealer.getINSTANCE(getApplicationContext()),
                getShoppingList(getExtraCommand())
        );
    }

    private ShoppingList getShoppingList(ShoppingListsFragment.Command command) {
        switch (command){
            case EDIT:{
                return (ShoppingList) getIntent().getSerializableExtra(ShoppingListsFragment.CLICKED_SHOPPING_LIST);
            }
            case NEW:{
                return new ShoppingList(getString(R.string.new_shopping_list));
            }
            default: {
                setResult(Activity.RESULT_CANCELED);
            }
        }
        return new ShoppingList(getString(R.string.new_shopping_list));
    }

    private ShoppingListsFragment.Command getExtraCommand() {
        return (ShoppingListsFragment.Command) getIntent().getSerializableExtra(ShoppingListsFragment.COMMAND);
    }

    private ShoppingListDetailsFragment setupShoppigListDetailsFragment() {
        ShoppingListDetailsFragment fragment = (ShoppingListDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(fragment == null){
            fragment = fragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        return fragment;
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
