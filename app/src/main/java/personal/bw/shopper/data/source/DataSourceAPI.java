package personal.bw.shopper.data.source;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

public interface DataSourceAPI {

    void getProductsForShoppingList(ShoppingList shoppingList, LoadProductsForShoppingListCallback callback);

    void putProduct(Product product, PutProductCallback callback);

    void deleteShoppingListFromDB(ShoppingList shoppingListToDelete, DeleteShoppingListCallback deleteShoppingListCallback);

    void getOrderedShoppingLists(@NonNull LoadShoppingListsCallback callback);

    void addProductToCache(Product product, PutProductCallback callback);

    void addProductToCache(Product product);

    void deleteProductFromCache(Product product);

    void putProductToDB(Product product, PutProductCallback callback);

    void saveShoppingList(SaveShoppingListCallback callback);

    interface PutShooppingListCallback{
        void onShoppingListPut();

        void onShoppingListPutFailure();
    }

    interface LoadShoppingListsCallback {

        void onShoppingListsLoaded(List<ShoppingList> lists);

        void onDataNotAvailable();
    }

    interface LoadProductsForShoppingListCallback {

        void onProductsForShoppingListLoaded(List<Product> products);

        void onDataNotAvailable();
    }

    interface GetShoppingListCallback {

        void onShoppingListLoaded(ShoppingList list);

        void onDataNotAvailable();
    }

    interface DeleteShoppingListCallback {
        void onShoppingListDeleted();

        void onDeletionError(String s);
    }

    interface DeleteProductCallback {
        void onProductDeleted();

        void onDeletionError(String s);
    }

    interface PutProductCallback {
        void onProductPut();

        void onPuttingError();
    }

    interface SaveShoppingListCallback{
        void onShoppingListSave();

        void onShoppingListSaveFailure();
    }

    public interface DeleteProductsForShoppingListCallback {
        void onProductsDeleteFailure();

        void onProductsDelete();
    }
}
