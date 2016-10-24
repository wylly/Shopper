package personal.bw.shopper.data.source;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import personal.bw.shopper.ShopperLog;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.source.local.ShopHelperDatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class ProductsRepository {
    private static ProductsRepository INSTANCE = null;

    private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;

    private PreparedQuery<Product> productsForShoppingListsQuery = null;

    private ProductsRepository(Context context) {
        shopHelperDatabaseHelper = new ShopHelperDatabaseHelper(context);
    }

    public static ProductsRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsRepository(context);
        }
        return INSTANCE;
    }

    public void deleteProduct(Product product, DataSourceAPI.DeleteProductCallback deleteProductCallback) {
        try {
            int result = shopHelperDatabaseHelper.getProductDao().delete(product);
            if (result > 1)
                deleteProductCallback.onDeletionError("Deleted more than 1 ");
            else if (result == 1)
                deleteProductCallback.onProductDeleted();
            else if (result == 0)
                deleteProductCallback.onDeletionError("Couldn't delete product - product not found");
            else if (result < 0)
                deleteProductCallback.onDeletionError("Couldn't delete product - error unknown");
        } catch (SQLException e) {
            e.printStackTrace();
            deleteProductCallback.onDeletionError("Exception occurred");
        }
    }

    public void createOrUpdateProduct(Product product, DataSourceAPI.PutProductCallback callback) {
        try {
            Dao.CreateOrUpdateStatus status = shopHelperDatabaseHelper.getProductDao().createOrUpdate(product);
            if (status.isCreated()) {
                ShopperLog.i("Created: " + product.toString());
            } else {
                ShopperLog.i("Updated: " + product.toString());
            }
            callback.onProductPut();
        } catch (SQLException e) {
            e.printStackTrace();
            ShopperLog.e("Exception while create or update: " + product.toString());
            callback.onPuttingError();
        }
    }

    public void deleteProducts(List<Product> products, final DataSourceAPI.DeleteProductsForShoppingListCallback callback) {
        for (final Product product : products) {
            deleteProduct(product, new DataSourceAPI.DeleteProductCallback() {
                @Override
                public void onProductDeleted() {
                    ShopperLog.i("Product deleted: " + product.toString());
                    callback.onProductsDelete();
                }

                @Override
                public void onDeletionError(String s) {
                    ShopperLog.e("Product deletion ERROR: " + product.toString());
                    callback.onProductsDeleteFailure();
                }
            });
        }
    }
}
