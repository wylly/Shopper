package personal.bw.shopper.data.datasource;

import android.content.Context;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import personal.bw.shopper.data.datasource.DataSourceAPI.DeleteProductCallback;
import personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper;
import personal.bw.shopper.data.datasource.queries.DeleteAllProductsForShoppingListQueryBuilder;
import personal.bw.shopper.data.datasource.queries.ProductsForShoppingListQueryBuilder;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;
import personal.bw.shopper.untils.ListFilter;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static personal.bw.shopper.data.models.ShoppingListProduct.PRODUCT_ID_FIELD_NAME;

public class ProductsForShoppingListRepository
{
	private static ProductsForShoppingListRepository INSTANCE = null;

	private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;

	private ProductsForShoppingListQueryBuilder productsForShoppingListQueryBuilder;
	private DeleteAllProductsForShoppingListQueryBuilder deleteAllProductsForShoppingListQueryBuilder;
	private ListFilter<Product> filter = new ListFilter<>();

	private ProductsForShoppingListRepository(Context context)
	{
		shopHelperDatabaseHelper = new ShopHelperDatabaseHelper(context);
		productsForShoppingListQueryBuilder =
				new ProductsForShoppingListQueryBuilder(shopHelperDatabaseHelper);
		deleteAllProductsForShoppingListQueryBuilder =
				new DeleteAllProductsForShoppingListQueryBuilder(shopHelperDatabaseHelper);
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
				PreparedQuery<Product> query = productsForShoppingListQueryBuilder.getQuery(shoppingList);
				List<Product> productList = shopHelperDatabaseHelper.getProductDao().query(query);
				callback.onProductsForShoppingListLoaded(productList);
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


	public void readShoppingListProductsWithBarcode(final String barcode, ShoppingList shoppingList, DataSourceAPI.LoadProductsForShoppingListCallback callback)
	{
		try
		{
			PreparedQuery<Product> query = productsForShoppingListQueryBuilder.getQuery(shoppingList);
			List<Product> productList = shopHelperDatabaseHelper.getProductDao().query(query);
			LinkedList<Product> filteredProducts = filter.filterResults(productList, new Predicate<Product>()
			{
				@Override
				public boolean apply(Product input)
				{
					return barcode.equals(input.getBarCode());
				}
			});
			callback.onProductsForShoppingListLoaded(filteredProducts);
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onDataNotAvailable();
		}
	}

	public void deleteProductsForShoppingList(ShoppingList shoppingList, DataSourceAPI.DeleteProductsForShoppingListCallback callback)
	{
		try
		{
			PreparedDelete<ShoppingListProduct> shoppingListProductPreparedDelete =
					deleteAllProductsForShoppingListQueryBuilder.removeProductsForShoppingListFromDB(shoppingList);
			shopHelperDatabaseHelper.getShoppingListProductDao().delete(shoppingListProductPreparedDelete);
			callback.onProductsDelete();
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onProductsDeleteFailure();
		}
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

	public void deleteProductFromAllShoppingLists(Product product, DeleteProductCallback callback)
	{
		try
		{
			deleteProduct(product);
			deleteShoppingListProducts(product);
			callback.onProductDeleted();
		} catch (SQLException e)
		{
			e.printStackTrace();
			callback.onDeletionError("Failed to delete product from all shoppinglists");
		}
	}

	private void deleteProduct(Product product) throws SQLException
	{
		shopHelperDatabaseHelper.getProductDao().delete(product);
	}

	private void deleteShoppingListProducts(Product product) throws SQLException
	{
		PreparedDelete<ShoppingListProduct> deleteQuery = makeShoppingListsForProductQuery(product);
		shopHelperDatabaseHelper.getShoppingListProductDao().delete(deleteQuery);
	}

	private PreparedDelete<ShoppingListProduct> makeShoppingListsForProductQuery(Product product) throws SQLException
	{
		PreparedDelete<ShoppingListProduct> shoppingListProductQB = getProductForShoppingListQueryBuilder();
		shoppingListProductQB.setArgumentHolderValue(0, product.getId());
		return shoppingListProductQB;
	}

	private PreparedDelete<ShoppingListProduct> getProductForShoppingListQueryBuilder() throws SQLException
	{
		DeleteBuilder<ShoppingListProduct, Long> shoppingListProductQB =
				shopHelperDatabaseHelper.getShoppingListProductDao().deleteBuilder();
		SelectArg productIdArg = new SelectArg();
		shoppingListProductQB.where().eq(PRODUCT_ID_FIELD_NAME, productIdArg);
		return shoppingListProductQB.prepare();
	}
}
