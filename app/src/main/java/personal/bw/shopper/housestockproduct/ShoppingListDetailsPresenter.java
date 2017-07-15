package personal.bw.shopper.housestockproduct;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.R;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.shoppinglists.ShoppingListsFragment;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListDetailsPresenter implements HousestockProductContract.Presenter
{
	private final DataSourceDealer repository;
	private final HousestockProductContract.View houseStockView;
	private final ShoppingList shoppingList;
	private final StringResourcesRepository stringResourcesRepository;

	public ShoppingListDetailsPresenter(@NonNull HousestockProductContract.View houseStockView,
										@NonNull DataSourceDealer shoppingListsRepository,
										@NonNull StringResourcesRepository stringResourcesRepository,
										Intent intent)
	{
		this.repository = checkNotNull(shoppingListsRepository, "shoppingListDetailsFragment cannot be null");
		this.houseStockView = houseStockView;
		this.houseStockView.setPresenter(this);
		this.stringResourcesRepository = stringResourcesRepository;
		this.shoppingList = getShoppingList(getExtraCommand(intent), intent);
	}

	private ShoppingList getShoppingList(ShoppingListsFragment.Command command, Intent intent)
	{
		switch (command)
		{
			case EDIT:
			{
				return (ShoppingList) intent.getSerializableExtra(ShoppingListsFragment.CLICKED_SHOPPING_LIST);
			}
			case NEW:
			{
				return new ShoppingList(stringResourcesRepository.getString(R.string.new_shopping_list));
			}


		}
		return new ShoppingList(stringResourcesRepository.getString(R.string.new_shopping_list));
	}

	private ShoppingListsFragment.Command getExtraCommand(Intent intent)
	{
		return (ShoppingListsFragment.Command) intent.getSerializableExtra(ShoppingListsFragment.COMMAND);
	}

	@Override
	public void start()
	{
		loadProducts();
	}

	@Override
	public void loadProducts()
	{
		houseStockView.setLoadingIndicator(true);


	}

	private void processProducts(List<Product> products)
	{
		if (products.isEmpty())
		{
			houseStockView.showNoProducts();
		}
		else
		{
			houseStockView.showProducts(products);
		}
	}

	@Override
	public void deleteProduct(int clickedProduct)
	{
		repository.deleteProductFromCache(clickedProduct);
		loadProducts();
	}

	@Override
	public void result(int requestCode, int resultCode)
	{
		if (Activity.RESULT_OK == resultCode)
		{
			houseStockView.showSuccessfullySavedMessage();
		}
	}

	@Override
	public void setShoppingListName(String name)
	{
		this.shoppingList.setName(name);
	}

	@Override
	public void moveToTrash(int id)
	{

	}

	@Override
	public void saveShoppingList()
	{
		repository.saveShoppingList(new DataSourceAPI.SaveShoppingListCallback()
		{
			@Override
			public void onShoppingListSave()
			{
				houseStockView.showShoppingListSavedMessage();
			}

			@Override
			public void onShoppingListSaveFailure()
			{
				houseStockView.showUnableToSaveShoppingList();
			}
		});
	}

	@Override
	public void markProductChecked(int product)
	{
		repository.getProductFromCache(product).setChecked(true);
	}

	@Override
	public void markProductUnchecked(int product)
	{
		repository.getProductFromCache(product).setChecked(false);
	}

	@Override
	public CharSequence getShoppingListName()
	{
		return shoppingList.getName();
	}
}
