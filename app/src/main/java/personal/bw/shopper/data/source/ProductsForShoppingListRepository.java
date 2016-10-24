package personal.bw.shopper.data.source;

import android.content.Context;
import com.j256.ormlite.stmt.*;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;
import personal.bw.shopper.data.source.local.ShopHelperDatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class ProductsForShoppingListRepository {
    private static ProductsForShoppingListRepository INSTANCE = null;

    private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;

    private PreparedQuery<Product> getProductsForShoppingListsQuery = null;
    private PreparedDelete<ShoppingListProduct> deleteProductsForShoppingListsQuery = null;


    private ProductsForShoppingListRepository(Context context) {
        shopHelperDatabaseHelper = new ShopHelperDatabaseHelper(context);
    }

    static ProductsForShoppingListRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsForShoppingListRepository(context);
        }
        return INSTANCE;
    }


    public void readProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.LoadProductsForShoppingListCallback callback) {
        try {
            callback.onProductsForShoppingListLoaded(lookupProductsForShoppingList(shoppingList));
        } catch (SQLException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        }
    }

    private List<Product> lookupProductsForShoppingList(ShoppingList shoppingList) throws SQLException {
        if (getProductsForShoppingListsQuery == null) {
            getProductsForShoppingListsQuery = makeProductsForShoppingListQuery();
        }
        getProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList);
        return shopHelperDatabaseHelper.getProductDao().query(getProductsForShoppingListsQuery);
    }

    private PreparedQuery<Product> makeProductsForShoppingListQuery() throws SQLException {
        QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB = shopHelperDatabaseHelper.getShoppingListProductDao().queryBuilder();
        shoppingListProductQB.selectColumns(ShoppingListProduct.PRODUCT_ID_FIELD_NAME);
        SelectArg shoppingListSelectArg = new SelectArg();
        shoppingListProductQB.where().eq(ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME, shoppingListSelectArg);
        QueryBuilder<Product, Long> productQB = shopHelperDatabaseHelper.getProductDao().queryBuilder();
        productQB.where().in(Product.ID_FIELD_NAME, shoppingListProductQB);
        return productQB.prepare();
    }

    public void deleteProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.DeleteProductsForShoppingListCallback callback) {
        try {
            removeProductsForShoppingListFromDB(shoppingList);
            callback.onProductsDelete();
        } catch (SQLException e) {
            e.printStackTrace();
            callback.onProductsDeleteFailure();
        }
    }

    private void removeProductsForShoppingListFromDB(ShoppingList shoppingList) throws SQLException {
        if (deleteProductsForShoppingListsQuery == null) {
            deleteProductsForShoppingListsQuery = prepareDeleteProductsFroShoppingListQuery();
        }
        deleteProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList.getId());
        shopHelperDatabaseHelper.getShoppingListProductDao().delete(deleteProductsForShoppingListsQuery);
    }

    private PreparedDelete<ShoppingListProduct> prepareDeleteProductsFroShoppingListQuery() throws SQLException {
        DeleteBuilder<ShoppingListProduct, Long> deleteBuilder = shopHelperDatabaseHelper.getShoppingListProductDao().deleteBuilder();
        deleteBuilder.where().eq(ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME, new SelectArg());
        return deleteBuilder.prepare();
    }
}
