package personal.bw.shopper.productlist.shoppinglist.existingshoppinglist;

import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.BaseShoppingListDetailsPresenter;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;
import personal.bw.shopper.shoppinglists.ShoppingListsFragment;

public class ExisitngShoppingListDetailsPresenter extends BaseShoppingListDetailsPresenter
{
	public ExisitngShoppingListDetailsPresenter(
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
		return (ShoppingList) intent.getSerializableExtra(ShoppingListsFragment.CLICKED_SHOPPING_LIST);
	}
}
