package personal.bw.shopper.shoppinglists;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

public interface ShoppingListsContract
{

	interface View extends BaseView<Presenter>
	{

		void startAddShoppingListActivity();

		void setLoadingIndicator(boolean active);

		void showShoppingLists(List<ShoppingList> shoppingLists);

		void showSuccessfullySavedMessage();

		void showLoadingShoppingListsError();

		boolean isActive();

		void showNoShoppingLists();

		void showShoppingListDeletedToast();

		void showShoppingListDeletionError(String info);
	}

	interface Presenter extends BasePresenter
	{

		void loadShoppingLists();

		void result(int requestCode, int resultCode);

		void deleteShoppingList(ShoppingList clickedShoppingList);
	}
}
