package personal.bw.shopper.shoppinglistdetails;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public interface ShoppingListDetailsContract {
    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean b);

        void showProducts(List<Product> products);

        void showNoProducts();

        void showSuccessfullySavedMessage();

        void showProductDeletedToast();

        void showProductDeletionError(String info);

        void showProductDetailsUi(Product clickedProduct);

        void showLoadingProductsError();

        boolean isActive();

        void showProductChecked();

        void showProductCheckFailed(String s);

        void showProductUnchecked();

        void showProductUncheckFailed(String s);

        void showShoppingListSavedMessage();

        void showUnableToSaveShoppingList();
    }

    interface Presenter extends BasePresenter{

        void loadProducts();

        void deleteProduct(Product clickedProduct);

        void result(int requestCode, int resultCode);

        void markProductChecked(Product product);

        void markProductUnchecked(Product product);

        CharSequence getShoppingListName();

        void saveShoppingList();
    }
}
