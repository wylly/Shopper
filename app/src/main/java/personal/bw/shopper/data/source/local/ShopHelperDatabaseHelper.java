package personal.bw.shopper.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;

import java.sql.SQLException;

public class ShopHelperDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "shopHelperDatabase";
    private static final int DATABASE_VERSION = 2;

    private Dao<Product, Long> productDao;
    private Dao<ShoppingList, Long> shoppingListDao;
    private Dao<ShoppingListProduct, Long> shoppingListProductDao;

    public ShopHelperDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                R.raw.ormlite_config);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, ShoppingList.class);
            TableUtils.createTable(connectionSource, ShoppingListProduct.class);
            putPredefinedShoppingList(createHouseList());
            putPredefinedShoppingList(createTrashList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Product.class, false);
            TableUtils.dropTable(connectionSource, ShoppingList.class, false);
            TableUtils.dropTable(connectionSource, ShoppingListProduct.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Product, Long> getProductDao() throws SQLException {
        if (productDao == null) {
            productDao = getDao(Product.class);
        }
        return productDao;
    }

    public Dao<ShoppingList, Long> getShoppingListDao() throws SQLException {
        if (shoppingListDao == null) {
            shoppingListDao = getDao(ShoppingList.class);
        }
        return shoppingListDao;
    }

    public Dao<ShoppingListProduct, Long> getShoppingListProductDao() throws SQLException {
        if (shoppingListProductDao == null) {
            shoppingListProductDao = getDao(ShoppingListProduct.class);
        }
        return shoppingListProductDao;
    }

    private ShoppingList createHouseList() {
        return new ShoppingList( 0L, "House List");
    }

    private ShoppingList createTrashList() {
        return new ShoppingList( 1L, "House List");
    }

    private void putPredefinedShoppingList(ShoppingList list) {
        try {
            getShoppingListDao().create(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
