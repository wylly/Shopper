package personal.bw.shopper.shoppinglistdetails;

import android.app.Activity;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.source.DataSourceAPI;
import personal.bw.shopper.data.source.DataSourceDealer;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListDetailsPresenter implements ShoppingListDetailsContract.Presenter {

    private final DataSourceDealer repository;
    private final ShoppingListDetailsContract.View shoppingListsDetailsView;
    private final ShoppingList shoppingList;

    public ShoppingListDetailsPresenter(@NonNull ShoppingListDetailsContract.View shoppingListsDetailsView, @NonNull DataSourceDealer shoppingListsRepository, ShoppingList shoppingList) {
        this.repository = checkNotNull(shoppingListsRepository, "shoppingListDetailsFragment cannot be null");
        this.shoppingListsDetailsView = shoppingListsDetailsView;
        this.shoppingListsDetailsView.setPresenter(this);
        this.shoppingList = shoppingList;
    }

    @Override
    public void start() {
        loadProducts();
    }

    @Override
    public void loadProducts() {
        shoppingListsDetailsView.setLoadingIndicator(true);
        repository.getProductsForShoppingList(shoppingList, new DataSourceAPI.LoadProductsForShoppingListCallback() {
            @Override
            public void onProductsForShoppingListLoaded(List<Product> products) {
                if (!shoppingListsDetailsView.isActive()) {
                    return;
                }
                shoppingListsDetailsView.setLoadingIndicator(false);
                processProducts(products);
            }

            @Override
            public void onDataNotAvailable() {
                if (!shoppingListsDetailsView.isActive()) {
                    return;
                }
                shoppingListsDetailsView.showLoadingProductsError();
            }
        });
    }

    private void processProducts(List<Product> products) {
        if (products.isEmpty()) {
            shoppingListsDetailsView.showNoProducts();
        } else {
            shoppingListsDetailsView.showProducts(products);
        }
    }

    @Override
    public void deleteProduct(int clickedProduct) {
        repository.deleteProductFromCache(clickedProduct);
        loadProducts();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (/*AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && */Activity.RESULT_OK == resultCode) {
            shoppingListsDetailsView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void saveShoppingList() {
        repository.saveShoppingList(new DataSourceAPI.SaveShoppingListCallback() {
            @Override
            public void onShoppingListSave() {
                shoppingListsDetailsView.showShoppingListSavedMessage();
            }

            @Override
            public void onShoppingListSaveFailure() {
                shoppingListsDetailsView.showUnableToSaveShoppingList();
            }
        });
    }

    @Override
    public void markProductChecked(int product) {
        repository.getProductFromCache(product).setChecked(true);
    }

    @Override
    public void markProductUnchecked(int product) {
        repository.getProductFromCache(product).setChecked(false);
    }

    @Override
    public CharSequence getShoppingListName() {
        return shoppingList.getName();
    }
}
