package personal.bw.shopper.shoppinglistdetails;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.source.DataSourceAPI;
import personal.bw.shopper.data.source.DataSourceDealer;
import personal.bw.shopper.data.source.StringResourcesRepository;
import personal.bw.shopper.shoppinglists.ShoppingListsFragment;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListDetailsPresenter implements ShoppingListDetailsContract.Presenter {

    private final DataSourceDealer repository;
    private final ShoppingListDetailsContract.View shoppingListsDetailsView;
    private final ShoppingList shoppingList;
    private final StringResourcesRepository stringResourcesRepository;

    public ShoppingListDetailsPresenter(@NonNull ShoppingListDetailsContract.View shoppingListsDetailsView, @NonNull DataSourceDealer shoppingListsRepository, @NonNull StringResourcesRepository stringResourcesRepository,  Intent intent) {
        this.repository = checkNotNull(shoppingListsRepository, "shoppingListDetailsFragment cannot be null");
        this.shoppingListsDetailsView = shoppingListsDetailsView;
        this.shoppingListsDetailsView.setPresenter(this);
        this.stringResourcesRepository = stringResourcesRepository;
        this.shoppingList = getShoppingList(getExtraCommand(intent), intent);
    }

    private ShoppingList getShoppingList(ShoppingListsFragment.Command command, Intent intent) {
        switch (command) {
            case EDIT: {
                return (ShoppingList) intent.getSerializableExtra(ShoppingListsFragment.CLICKED_SHOPPING_LIST);
            }
            case NEW: {
                return new ShoppingList(stringResourcesRepository.getString(R.string.new_shopping_list));
            }
            case HOUSEHOLD_LIST: {
                repository.getHouseRepository();
            }
        }
        return new ShoppingList(stringResourcesRepository.getString(R.string.new_shopping_list));
    }

    private ShoppingListsFragment.Command getExtraCommand(Intent intent) {
        return (ShoppingListsFragment.Command) intent.getSerializableExtra(ShoppingListsFragment.COMMAND);
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
    public void setShoppingListName(String name) {
        this.shoppingList.setName(name);
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
