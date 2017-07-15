package personal.bw.shopper.productlist;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseShoppingListDetailsPresenter implements ShoppingListDetailsContract.Presenter
{
	private final DataSourceDealer repository;
	private final ShoppingListDetailsContract.View shoppingListsDetailsView;
	private final ShoppingList shoppingList;
	private final StringResourcesRepository stringResourcesRepository;

	public BaseShoppingListDetailsPresenter(@NonNull ShoppingListDetailsContract.View shoppingListsDetailsView,
											@NonNull DataSourceDealer shoppingListsRepository,
											@NonNull StringResourcesRepository stringResourcesRepository,
											Intent intent)
	{
		this.repository = checkNotNull(shoppingListsRepository, "shoppingListDetailsFragment cannot be null");
		this.shoppingListsDetailsView = shoppingListsDetailsView;
		this.shoppingListsDetailsView.setPresenter(this);
		this.stringResourcesRepository = stringResourcesRepository;
		this.shoppingList = getShoppingList(intent);
	}

	public abstract ShoppingList getShoppingList(Intent intent);


	@Override
	public void start()
	{
		loadProducts();
	}

	@Override
	public void loadProducts()
	{
		shoppingListsDetailsView.setLoadingIndicator(true);
		repository.getProductsForShoppingList(shoppingList, new DataSourceAPI.LoadProductsForShoppingListCallback()
		{
			@Override
			public void onProductsForShoppingListLoaded(List<Product> products)
			{
				if (!shoppingListsDetailsView.isActive())
				{
					return;
				}
				shoppingListsDetailsView.setLoadingIndicator(false);
				processProducts(products);
			}

			@Override
			public void onDataNotAvailable()
			{
				if (!shoppingListsDetailsView.isActive())
				{
					return;
				}
				shoppingListsDetailsView.showLoadingProductsError();
			}
		});
	}

	private void processProducts(List<Product> products)
	{
		if (products.isEmpty())
		{
			shoppingListsDetailsView.showNoProducts();
		}
		else
		{
			shoppingListsDetailsView.showProducts(products);
		}
	}

	@Override
	public void deleteProduct(int clickedProduct)
	{
		repository.deleteProductFromCache(clickedProduct);
		loadProducts();
	}

	@Override
	public void moveToTrash(int clickedProduct){
		repository.moveToTrash(clickedProduct);
	}

	@Override
	public void result(int requestCode, int resultCode)
	{
		if (Activity.RESULT_OK == resultCode)
		{
			shoppingListsDetailsView.showSuccessfullySavedMessage();
		}
	}

	@Override
	public void setShoppingListName(String name)
	{
		this.shoppingList.setName(name);
	}

	@Override
	public void saveShoppingList()
	{
		repository.saveShoppingList(new DataSourceAPI.SaveShoppingListCallback()
		{
			@Override
			public void onShoppingListSave()
			{
				shoppingListsDetailsView.showShoppingListSavedMessage();
			}

			@Override
			public void onShoppingListSaveFailure()
			{
				shoppingListsDetailsView.showUnableToSaveShoppingList();
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

	public DataSourceDealer getRepository()
	{
		return repository;
	}

	public StringResourcesRepository getStringResourcesRepository()
	{
		return stringResourcesRepository;
	}
}
