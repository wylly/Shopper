package personal.bw.shopper.shoppinglists;

import android.app.Activity;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListsPresenter implements ShoppingListsContract.Presenter
{

	private final ShoppingListsContract.View shoppingListsView;
	private final DataSourceDealer repository;

	public ShoppingListsPresenter(@NonNull ShoppingListsContract.View shoppingListsFragment, @NonNull DataSourceDealer repository)
	{
		this.shoppingListsView = checkNotNull(shoppingListsFragment, "shoppingListsFragment cannot be null");
		this.repository = repository;
		this.shoppingListsView.setPresenter(this);
	}

	@Override
	public void start()
	{
		loadShoppingLists();
	}

	@Override
	public void loadShoppingLists()
	{
		shoppingListsView.setLoadingIndicator(true);
		repository.getOrderedShoppingLists(new DataSourceAPI.LoadShoppingListsCallback()
		{
			@Override
			public void onShoppingListsLoaded(List<ShoppingList> shoppingLists)
			{
				if (!shoppingListsView.isActive())
				{
					return;
				}
				shoppingListsView.setLoadingIndicator(false);
				processShoppingLists(shoppingLists);
			}

			@Override
			public void onDataNotAvailable()
			{
				if (!shoppingListsView.isActive())
				{
					return;
				}
				shoppingListsView.showLoadingShoppingListsError();
			}
		});

	}

	@Override
	public void deleteShoppingList(ShoppingList clickedShoppingList)
	{
		repository.deleteShoppingListFromDB(clickedShoppingList, new DataSourceAPI.DeleteShoppingListCallback()
		{

			@Override
			public void onShoppingListDeleted()
			{
				shoppingListsView.showShoppingListDeletedToast();
			}

			@Override
			public void onDeletionError(String info)
			{
				shoppingListsView.showShoppingListDeletionError(info);
			}
		});
		loadShoppingLists();
	}

	private void processShoppingLists(List<ShoppingList> shoppingLists)
	{
		if (shoppingLists.isEmpty())
		{
			shoppingListsView.showNoShoppingLists();
		}
		else
		{
			shoppingListsView.showShoppingLists(shoppingLists);
		}
	}

	@Override
	public void result(int requestCode, int resultCode)
	{
		// If a task was successfully added, show snackbar
		if (/*AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && */Activity.RESULT_OK == resultCode)
		{
			shoppingListsView.showSuccessfullySavedMessage();
		}
	}
}
