package personal.bw.shopper;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class Menues {

    public static void setupActionBar(AppCompatActivity parent){
        Toolbar toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        parent.setSupportActionBar(toolbar);
        ActionBar ab = parent.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public static DrawerLayout setupDrawerMenu(AppCompatActivity parent, ActivitiesEnum current) {
        DrawerLayout drawerLayout = (DrawerLayout) parent.findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) drawerLayout.findViewById(R.id.drawer_menu);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(getListener(current, drawerLayout));
        }
        return drawerLayout;
    }


    private static NavigationView.OnNavigationItemSelectedListener getListener(final ActivitiesEnum current, final DrawerLayout mDrawerLayout) {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.shopping_lists_list_navigation_menu_item:
                        if (!current.equals(ActivitiesEnum.LISTS)) {

                        }
                        break;
                    case R.id.scan_barcode_navigation_menu_item:
                        if (!current.equals(ActivitiesEnum.BARCODE)) {
//                                    Intent intent =
//                                        new Intent(TasksActivity.this, StatisticsActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                        }

                        break;
                    case R.id.house_stock_list_navigation_menu_item:
                        if (!current.equals(ActivitiesEnum.HOUSESTOCK)) {
//                                    Intent intent =
//                                        new Intent(TasksActivity.this, StatisticsActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                        }
//
                        break;

                    case R.id.trash_list_navigation_menu_item:
                        if (!current.equals(ActivitiesEnum.TRASHBIN)) {
//                                    Intent intent =
//                                        new Intent(TasksActivity.this, StatisticsActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                // Close the navigation drawer when an item is selected.
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        };
    }
}
