package personal.bw.shopper.productlist;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public interface ProductListContract
{
	interface View extends BaseView<Presenter>
	{
		void setLoadingIndicator(boolean b);

		void showProducts(List<Product> products);

		void showNoProducts();

		void showSuccessfullySavedMessage();

		void showProductDeletedToast();

		void showProductDeletionError(String info);

		void startProductDetailsActivity(int clickedProduct);

		void showLoadingProductsError();

		boolean isActive();

		void showProductChecked();

		void showProductCheckFailed(String s);

		void showProductUnchecked();

		void showProductUncheckFailed(String s);

		void showShoppingListSavedMessage();

		void showUnableToSaveShoppingList();
	}

	interface Presenter extends BasePresenter
	{

		void loadProducts();

		void deleteProduct(int clickedProduct);

		void moveToTrash(int clickedProduct);

		void result(int requestCode, int resultCode);

		void markProductChecked(int product);

		void markProductUnchecked(int product);

		CharSequence getShoppingListName();

		void saveShoppingList();

		void setShoppingListName(String s);
	}
}
