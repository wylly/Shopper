package personal.bw.shopper.data.datasource;

import android.content.Context;
import personal.bw.shopper.ShopperLog;
import personal.bw.shopper.data.datasource.DataSourceAPI.DeleteProductCallback;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

public class DataBase
{
	private static DataBase INSTANCE;
	private ShoppingListsRepository shoppingListsRepository;
	private ProductsRepository productsRepository;
	private ProductsForShoppingListRepository productsForShoppingListRepository;

	private DataBase()
	{
	}

	private DataBase(Context context)
	{
		shoppingListsRepository = ShoppingListsRepository.getINSTANCE(context);
		productsRepository = ProductsRepository.getINSTANCE(context);
		productsForShoppingListRepository = ProductsForShoppingListRepository.getINSTANCE(context);
	}

	public static DataBase getINSTANCE(Context context)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DataBase(context);
		}
		return INSTANCE;
	}


	public void deleteAllProductsForShoppingList(final ShoppingList shoppingList, final DataSourceAPI.DeleteProductsForShoppingListCallback callback)
	{
		readProductsForShoppingList(shoppingList, new DataSourceAPI.LoadProductsForShoppingListCallback()
		{
			@Override
			public void onProductsForShoppingListLoaded(List<Product> products)
			{
				productsRepository.deleteProducts(products, new DataSourceAPI.DeleteProductsForShoppingListCallback()
				{
					@Override
					public void onProductsDeleteFailure()
					{
						ShopperLog.e("Deleting all products failed while deleting products for shopping list from PRODUCTS table.");
						callback.onProductsDeleteFailure();
					}

					@Override
					public void onProductsDelete()
					{
						ShopperLog.i("Delete all products: read all products for shopping list success");
					}
				});
			}

			@Override
			public void onDataNotAvailable()
			{
				ShopperLog.e("Delete all products failed while reading products for shopping list");
				callback.onProductsDeleteFailure();
			}
		});
		productsForShoppingListRepository.deleteProductsForShoppingList(shoppingList, new DataSourceAPI.DeleteProductsForShoppingListCallback()
		{
			@Override
			public void onProductsDeleteFailure()
			{
				ShopperLog.e("Deleting all products failed while deleting products from PRODUCTSFORSHOPPINGLIST table");
				callback.onProductsDeleteFailure();
			}

			@Override
			public void onProductsDelete()
			{
				ShopperLog.i("Deleting all products success!");
				callback.onProductsDelete();
			}
		});
	}

	public void createOrUpdateAllProducts(List<Product> products, final ShoppingList shoppingList, final DataSourceAPI.CreateOrUpdateAllProductsCallback callback)
	{
		for (final Product product : products)
		{
			createOrUpdateProduct(product, new DataSourceAPI.PutProductCallback()
			{
				@Override
				public void onProductPut(boolean isCreated)
				{
					ShopperLog.i("Putting product success: " + product);
					if (isCreated)
					{
						createShoppingListProduct(shoppingList, product, new DataSourceAPI.CreateShoppingListProductCallback()
						{
							@Override
							public void onCreateSuccess()
							{
								ShopperLog.i("Creating ShoppingListProduct success");
							}

							@Override
							public void onCreateFailure()
							{
								ShopperLog.e("Unable to create ShoppingListProduct :  shopping list = " + shoppingList + " product = " + product);
								callback.onCreateFailure();
							}
						});
					}
				}

				@Override
				public void onPuttingError()
				{
					ShopperLog.e("Putting many products failed on: " + product);
					callback.onCreateFailure();
				}
			});

		}
	}

	public void createOrUpdateProduct(Product product, DataSourceAPI.PutProductCallback callback)
	{
		productsRepository.createOrUpdateProduct(product, callback);
	}

	public void createOrUpdateShoppingList(final ShoppingList shoppingList, final DataSourceAPI.PutShooppingListCallback callback)
	{
		shoppingListsRepository.createOrUpdateShoppingList(shoppingList, new DataSourceAPI.PutShooppingListCallback()
		{

			@Override
			public void onShoppingListPut()
			{
				ShopperLog.i("Creating shopping list success " + shoppingList.toString());
				callback.onShoppingListPut();
			}

			@Override
			public void onShoppingListPutFailure()
			{
				ShopperLog.e("Creating shopping list failed: " + shoppingList.toString());
			}
		});
	}

	public void getOrderedShoppingLists(DataSourceAPI.LoadShoppingListsCallback callback)
	{
		shoppingListsRepository.readAllOrderedShoppingLists(callback);
	}


	public void deleteShoppingList(ShoppingList shoppingListToDelete, DataSourceAPI.DeleteShoppingListCallback deleteShoppingListCallback)
	{
		shoppingListsRepository.deleteShoppingList(shoppingListToDelete, deleteShoppingListCallback);
	}

	public void readProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.LoadProductsForShoppingListCallback callback)
	{
		productsForShoppingListRepository.readProductsForShoppingList(shoppingList, callback);
	}

	public void createShoppingListProduct(ShoppingList shoppingList, Product product, DataSourceAPI.CreateShoppingListProductCallback callback)
	{
		productsForShoppingListRepository.createShoppingListProduct(shoppingList, product, callback);
	}

	public ShoppingList readShoppingList(long shoppingListId)
	{
		return shoppingListsRepository.readShoppingList(shoppingListId);
	}

	public void getProductsWithBarcodeForShoppingList(String barcode, Long shoppingListId,  DataSourceAPI.LoadProductsForShoppingListCallback callback)
	{
		ShoppingList shoppingList = shoppingListsRepository.readShoppingList(shoppingListId);
		productsForShoppingListRepository.readShoppingListProductsWithBarcode(barcode, shoppingList, callback);
	}

	public void deleteProduct(Product product, DeleteProductCallback callback)
	{
		productsForShoppingListRepository.deleteProductFromAllShoppingLists(product, callback);
	}
}
