package personal.bw.shopper.data.datasource;

import android.content.Context;
import android.support.annotation.NonNull;
import personal.bw.shopper.ShopperLog;
import personal.bw.shopper.data.datasource.DataSourceAPI.*;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

import static personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper.HOUSEHOLD_LIST_ID;
import static personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper.TRASH_LIST;

public class DataSourceDealer
{

	private static DataSourceDealer INSTANCE;
	private static Cache cache;
	private static DataBase dataBase;

	private boolean isProductClearRequired;

	public static DataSourceDealer getINSTANCE(Context context)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DataSourceDealer(context);
		}
		return INSTANCE;
	}

	private DataSourceDealer(Context context)
	{
		cache = new Cache();
		dataBase = DataBase.getINSTANCE(context);
	}

	public void saveShoppingList(final SaveShoppingListCallback callback)
	{
		dataBase.createOrUpdateShoppingList(cache.readShoppingList(), new DataSourceAPI.PutShooppingListCallback()
		{
			@Override
			public void onShoppingListPut()
			{
				ShopperLog.i("Shopping list saving: created shoppingList");
			}

			@Override
			public void onShoppingListPutFailure()
			{
				ShopperLog.e("Shopping list saving failed while creating shopping list");
			}
		});

		if (isProductClearRequired)
		{
			dataBase.deleteAllProductsForShoppingList(cache.readShoppingList(), new DeleteProductsForShoppingListCallback()
			{
				@Override
				public void onProductsDeleteFailure()
				{
					ShopperLog.e("Saving shoppingList " + cache.readShoppingList().toString() + " failed while deleting products");
					callback.onShoppingListSaveFailure();
				}

				@Override
				public void onProductsDelete()
				{
					ShopperLog.i("Save shopping list: deletion of products completed");
				}
			});
		}
		dataBase.createOrUpdateAllProducts(cache.readProducts(), cache.readShoppingList(), new CreateOrUpdateAllProductsCallback()
		{
			@Override
			public void onCreateSuccess()
			{
				ShopperLog.i("Create or update all products success");
			}

			@Override
			public void onCreateFailure()
			{
				ShopperLog.e("Create or update all products success");
				callback.onShoppingListSaveFailure();
			}
		});
		callback.onShoppingListSave();
	}

	public void saveHousestockProduct(@NonNull Product product, @NonNull final PutProductCallback callback)
	{
		dataBase.createOrUpdateProduct(product, callback);
		ShoppingList housestock = getHousestockList();
		dataBase.createShoppingListProduct(housestock, product, new CreateShoppingListProductCallback()
		{
			@Override
			public void onCreateSuccess()
			{
				callback.onProductPut(false);
			}

			@Override
			public void onCreateFailure()
			{
				callback.onPuttingError();
			}
		});
	}

	public void getOrderedShoppingLists(@NonNull LoadShoppingListsCallback callback)
	{
		dataBase.getOrderedShoppingLists(callback);
	}

	public void deleteShoppingListFromDB(final ShoppingList shoppingListToDelete, DeleteShoppingListCallback deleteShoppingListCallback)
	{
		dataBase.deleteAllProductsForShoppingList(shoppingListToDelete, new DeleteProductsForShoppingListCallback()
		{
			@Override
			public void onProductsDeleteFailure()
			{
				ShopperLog.e("Delete shopping list" + shoppingListToDelete + " failed while deleting products");
			}

			@Override
			public void onProductsDelete()
			{
				ShopperLog.i("Delete shopping list: products deletion completed.");
			}
		});
		dataBase.deleteShoppingList(shoppingListToDelete, deleteShoppingListCallback);
	}

	public void getProductsForShoppingList(ShoppingList shoppingList, final LoadProductsForShoppingListCallback callback)
	{
		if (cache.readProducts() == null || cache.readShoppingList() != shoppingList)
		{
			dataBase.readProductsForShoppingList(shoppingList, new LoadProductsForShoppingListCallback()
			{
				@Override
				public void onProductsForShoppingListLoaded(List<Product> products)
				{
					cache.updateProducts(products);
					ShopperLog.i("Get products for shopping list - get products from DB success");
				}

				@Override
				public void onDataNotAvailable()
				{
					ShopperLog.e("Get products for shopping list failed while reading products from DB");
					callback.onDataNotAvailable();
				}
			});
			cache.updateShoppingList(shoppingList);
		}
		callback.onProductsForShoppingListLoaded(cache.readProducts());
	}


	public void addProductCache(Product product)
	{
		cache.createProduct(product);
	}

	public void deleteProductFromCache(int product)
	{
		cache.deleteProduct(product);
		isProductClearRequired = true;
	}

	public Product getProductFromCache(int product)
	{
		return cache.readProduct(product);
	}

	public void updateProductInCache(int productId, Product product)
	{
		cache.updateProduct(productId, product);
	}

	public ShoppingList getHousestockList()
	{
		return getShoppingList(HOUSEHOLD_LIST_ID);
	}

	public ShoppingList getTrashList()
	{
		return getShoppingList(TRASH_LIST);
	}

	private ShoppingList getShoppingList(long shoppingListId)
	{
		return dataBase.readShoppingList(shoppingListId);
	}

	public void moveToTrash(int clickedProduct, @NonNull final PutProductCallback callback)
	{
		Product product = getProductFromCache(clickedProduct);
		deleteProductFromCache(clickedProduct);
		product.setId(0L);
		moveProductToTrash(product, callback);
	}

	public void moveProductToTrash(final Product product,@NonNull final PutProductCallback callback)
	{
		dataBase.deleteProduct(product, new DeleteProductCallback()
		{
			@Override
			public void onProductDeleted()
			{
				dataBase.createOrUpdateProduct(product, callback);
				ShoppingList trashList = getTrashList();
				dataBase.createShoppingListProduct(trashList, product, new CreateShoppingListProductCallback()
				{
					@Override
					public void onCreateSuccess()
					{
						callback.onProductPut(false);
					}

					@Override
					public void onCreateFailure()
					{
						callback.onPuttingError();
					}
				});
			}

			@Override
			public void onDeletionError(String s)
			{
				callback.onPuttingError();
			}
		});
	}

	public void getProductsWithBarcode(String barcode, LoadProductsForShoppingListCallback callback)
	{
		dataBase.getProductsWithBarcodeForShoppingList(barcode, HOUSEHOLD_LIST_ID, callback);
	}
}
