package personal.bw.shopper.data.datasource;

import android.content.Context;
import com.google.common.collect.Lists;
import com.j256.ormlite.stmt.*;
import personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;

import java.sql.SQLException;
import java.util.List;

public class ProductsForShoppingListRepository
{
	private static ProductsForShoppingListRepository INSTANCE = null;

	private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;

	private PreparedQuery<Product> getProductsForShoppingListsQuery = null;
	private PreparedQuery<ShoppingListProduct> getShoppingListProductQuery = null;
	private PreparedDelete<ShoppingListProduct> deleteProductsForShoppingListsQuery = null;


	private ProductsForShoppingListRepository(Context context)
	{
		shopHelperDatabaseHelper = new ShopHelperDatabaseHelper(context);
	}

	static ProductsForShoppingListRepository getINSTANCE(Context context)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ProductsForShoppingListRepository(context);
		}
		return INSTANCE;
	}


	public void readProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.LoadProductsForShoppingListCallback callback)
	{
		try
		{
			if (shoppingList.getId() != null)
			{
				callback.onProductsForShoppingListLoaded(lookupProductsForShoppingList(shoppingList));
			}
			else
			{
				callback.onProductsForShoppingListLoaded(Lists.<Product>newLinkedList());
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onDataNotAvailable();
		}
	}

	private List<Product> lookupProductsForShoppingList(ShoppingList shoppingList) throws SQLException
	{
		if (getProductsForShoppingListsQuery == null)
		{
			getProductsForShoppingListsQuery = makeProductsForShoppingListQuery();
		}
		getProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList);
		return shopHelperDatabaseHelper.getProductDao().query(getProductsForShoppingListsQuery);
	}

	private PreparedQuery<Product> makeProductsForShoppingListQuery() throws SQLException
	{
		QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB = shopHelperDatabaseHelper.getShoppingListProductDao().queryBuilder();
		shoppingListProductQB.selectColumns(ShoppingListProduct.PRODUCT_ID_FIELD_NAME);
		SelectArg shoppingListSelectArg = new SelectArg();
		shoppingListProductQB.where().eq(ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME, shoppingListSelectArg);
		QueryBuilder<Product, Long> productQB = shopHelperDatabaseHelper.getProductDao().queryBuilder();
		productQB.where().in(Product.ID_FIELD_NAME, shoppingListProductQB);
		return productQB.prepare();
	}

	public void deleteProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.DeleteProductsForShoppingListCallback callback)
	{
		try
		{
			removeProductsForShoppingListFromDB(shoppingList);
			callback.onProductsDelete();
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onProductsDeleteFailure();
		}
	}

	private void removeProductsForShoppingListFromDB(ShoppingList shoppingList) throws SQLException
	{
		if (deleteProductsForShoppingListsQuery == null)
		{
			deleteProductsForShoppingListsQuery = prepareDeleteProductsFroShoppingListQuery();
		}
		deleteProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList.getId());
		shopHelperDatabaseHelper.getShoppingListProductDao().delete(deleteProductsForShoppingListsQuery);
	}

	private PreparedDelete<ShoppingListProduct> prepareDeleteProductsFroShoppingListQuery() throws SQLException
	{
		DeleteBuilder<ShoppingListProduct, Long> deleteBuilder = shopHelperDatabaseHelper.getShoppingListProductDao().deleteBuilder();
		deleteBuilder.where().eq(ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME, new SelectArg());
		return deleteBuilder.prepare();
	}

	private void readProductForShoppingList(Product product, ShoppingList shoppingList, DataSourceAPI.LoadProductForShoppingListCallback callback)
	{
		try
		{
			callback.onProductForShoppingListLoad(lookupShoppingListProduct(product, shoppingList));
		} catch (SQLException e)
		{
			callback.onProductForShoppingListLoadFailure();
		}
	}

	private ShoppingListProduct lookupShoppingListProduct(Product product, ShoppingList shoppingList) throws SQLException
	{
		if (getShoppingListProductQuery == null)
		{
			getShoppingListProductQuery = makeProductForShoppingListQuery();
		}
		getShoppingListProductQuery.setArgumentHolderValue(0, shoppingList);
		getShoppingListProductQuery.setArgumentHolderValue(1, product);
		return shopHelperDatabaseHelper.getShoppingListProductDao().query(getShoppingListProductQuery).get(0);
	}

	private PreparedQuery<ShoppingListProduct> makeProductForShoppingListQuery() throws SQLException
	{
		QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB = shopHelperDatabaseHelper.getShoppingListProductDao().queryBuilder();
		SelectArg shoppingListSelectArg = new SelectArg();
		SelectArg productSelectArg = new SelectArg();
		shoppingListProductQB.where()
				.eq(ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME, shoppingListSelectArg)
				.eq(ShoppingListProduct.PRODUCT_ID_FIELD_NAME, productSelectArg);
		return shoppingListProductQB.prepare();
	}

	public void createShoppingListProduct(ShoppingList shoppingList, Product product, DataSourceAPI.CreateShoppingListProductCallback callback)
	{
		try
		{
			shopHelperDatabaseHelper.getShoppingListProductDao().createOrUpdate(new ShoppingListProduct(product, shoppingList));
			callback.onCreateSuccess();
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onCreateFailure();
		}
	}
}
