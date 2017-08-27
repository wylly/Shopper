package personal.bw.shopper.data.datasource.queries;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;

import java.sql.SQLException;

import static personal.bw.shopper.data.models.Product.ID_FIELD_NAME;
import static personal.bw.shopper.data.models.ShoppingListProduct.PRODUCT_ID_FIELD_NAME;
import static personal.bw.shopper.data.models.ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME;

public class ProductsForShoppingListQueryBuilder
{
	private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;
	private PreparedQuery<Product> getProductsForShoppingListsQuery = null;

	public ProductsForShoppingListQueryBuilder(ShopHelperDatabaseHelper shopHelperDatabaseHelper)
	{
		this.shopHelperDatabaseHelper = shopHelperDatabaseHelper;
	}

	public PreparedQuery<Product> getQuery(ShoppingList shoppingList) throws SQLException
	{
		initQueryLazy();
		getProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList);
		return getProductsForShoppingListsQuery;
	}

	private void initQueryLazy() throws SQLException
	{
		if (getProductsForShoppingListsQuery == null)
		{
			getProductsForShoppingListsQuery = makeProductsForShoppingListQuery();
		}
	}

	private PreparedQuery<Product> makeProductsForShoppingListQuery() throws SQLException
	{
		QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB = getSelectProductIdOnArgShoppingListIdQuery();
		QueryBuilder<Product, Long> productQB = getProductQueryBuilder(shoppingListProductQB);
		return productQB.prepare();
	}

	private QueryBuilder<ShoppingListProduct, Long> getSelectProductIdOnArgShoppingListIdQuery() throws SQLException
	{
		QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB =
				shopHelperDatabaseHelper
						.getShoppingListProductDao()
						.queryBuilder();

		shoppingListProductQB.selectColumns(PRODUCT_ID_FIELD_NAME);

		SelectArg shoppingListIdArg = new SelectArg();
		shoppingListProductQB.where().eq(SHOPPINGLIST_ID_FIELD_NAME, shoppingListIdArg);

		return shoppingListProductQB;
	}

	private QueryBuilder<Product, Long> getProductQueryBuilder(QueryBuilder<ShoppingListProduct, Long> shoppingListProductQB) throws SQLException
	{
		QueryBuilder<Product, Long> productQB = shopHelperDatabaseHelper.getProductDao().queryBuilder();
		productQB.where().in(ID_FIELD_NAME, shoppingListProductQB);
		return productQB;
	}
}
