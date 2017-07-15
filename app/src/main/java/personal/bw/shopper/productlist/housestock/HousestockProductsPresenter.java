package personal.bw.shopper.productlist.housestock;

import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.BaseShoppingListDetailsPresenter;
import personal.bw.shopper.productlist.ShoppingListDetailsContract;

public class HousestockProductsPresenter extends BaseShoppingListDetailsPresenter
{
	public HousestockProductsPresenter(
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
		return getRepository().getHouseRepository();
	}
}
