package personal.bw.shopper.productlist.shoppinglist.newshoppinglist;

import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.R;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.BaseShoppingListDetailsPresenter;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;

public class NewShoppingListDetailsPresenter extends BaseShoppingListDetailsPresenter
{
	public NewShoppingListDetailsPresenter(
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
		return new ShoppingList(getStringResourcesRepository().getString(R.string.new_shopping_list));
	}
}
