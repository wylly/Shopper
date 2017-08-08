package personal.bw.shopper.productlist.trashlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.BaseShoppingListDetailsPresenter;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;

public class TrashListPresenter extends BaseShoppingListDetailsPresenter
{
	public TrashListPresenter(
			@NonNull ShoppingListDetailsContract.View shoppingListsDetailsView,
			@NonNull DataSourceDealer shoppingListsRepository,
			@NonNull StringResourcesRepository stringResourcesRepository,
			Intent intent)
	{
		super(shoppingListsDetailsView, shoppingListsRepository, stringResourcesRepository, intent);
	}

	@Override
	public ShoppingList getShoppingList(Intent intent)
	{
		return getRepository().getTrashList();
	}
	@Override
	public void deleteProduct(int clickedProduct)
	{
		super.deleteProduct(clickedProduct);
		getRepository().saveShoppingList(new DataSourceAPI.SaveShoppingListCallback()
		{
			@Override
			public void onShoppingListSave()
			{
				getShoppingListsDetailsView().showShoppingListSavedMessage();
			}

			@Override
			public void onShoppingListSaveFailure()
			{
				getShoppingListsDetailsView().showUnableToSaveShoppingList();
			}
		});
	}
}
