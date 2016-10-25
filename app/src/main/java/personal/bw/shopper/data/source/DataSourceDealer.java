package personal.bw.shopper.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import personal.bw.shopper.ShopperLog;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.source.DataSourceAPI.*;

import java.util.List;

public class DataSourceDealer {

    private static DataSourceDealer INSTANCE;
    private static Cache cache;
    private static DataBase dataBase;

    private boolean isProductClearRequired;

    public static DataSourceDealer getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataSourceDealer(context);
        }
        return INSTANCE;
    }

    private DataSourceDealer(Context context) {
        cache = new Cache();
        dataBase = DataBase.getINSTANCE(context);
    }

    public void saveShoppingList(final SaveShoppingListCallback callback) {
        dataBase.createShoppingList(cache.readShoppingList(), new DataSourceAPI.PutShooppingListCallback() {
            @Override
            public void onShoppingListPut() {
                ShopperLog.i("Shopping list saving: created shoppingList");

            }

            @Override
            public void onShoppingListPutFailure() {
                ShopperLog.e("Shopping list saving failed while creating shopping list");
            }
        });

        if (isProductClearRequired) {
            dataBase.deleteAllProductsForShoppingList(cache.readShoppingList(), new DeleteProductsForShoppingListCallback() {
                @Override
                public void onProductsDeleteFailure() {
                    ShopperLog.e("Saving shoppingList " + cache.readShoppingList().toString() + " failed while deleting products");
                    callback.onShoppingListSaveFailure();
                }

                @Override
                public void onProductsDelete() {
                    ShopperLog.i("Save shopping list: deletion of products completed");
                }
            });
        }
        dataBase.createOrUpdateAllProducts(cache.readProducts());
    }

    public void getOrderedShoppingLists(@NonNull LoadShoppingListsCallback callback) {
        dataBase.getOrderedShoppingLists(callback);
    }

    public void deleteShoppingListFromDB(final ShoppingList shoppingListToDelete, DeleteShoppingListCallback deleteShoppingListCallback) {
        dataBase.deleteAllProductsForShoppingList(shoppingListToDelete, new DeleteProductsForShoppingListCallback() {
            @Override
            public void onProductsDeleteFailure() {
                ShopperLog.e("Delete shopping list" + shoppingListToDelete + " failed while deleting products");
            }

            @Override
            public void onProductsDelete() {
                ShopperLog.i("Delete shopping list: products deletion completed.");
            }
        });
        dataBase.deleteShoppingList(shoppingListToDelete, deleteShoppingListCallback);
    }

    public void getProductsForShoppingList(ShoppingList shoppingList, final LoadProductsForShoppingListCallback callback) {
        if (cache.readProducts() == null || cache.readShoppingList() != shoppingList) {
            dataBase.readProductsForShoppingList(shoppingList, new LoadProductsForShoppingListCallback() {
                @Override
                public void onProductsForShoppingListLoaded(List<Product> products) {
                    cache.updateProducts(products);
                    ShopperLog.i("Get products for shopping list - get products from DB success");
                }

                @Override
                public void onDataNotAvailable() {
                    ShopperLog.e("Get products for shopping list failed while reading products from DB");
                    callback.onDataNotAvailable();
                }
            });
            cache.updateShoppingList(shoppingList);
        }
        callback.onProductsForShoppingListLoaded(cache.readProducts());
    }


    public void addEditProductCache(Product product) {
        cache.createOrUpdateProduct(product);
    }

    public void deleteProductFromCache(Product product) {
        cache.deleteProduct(product);
        isProductClearRequired = true;
    }
}
