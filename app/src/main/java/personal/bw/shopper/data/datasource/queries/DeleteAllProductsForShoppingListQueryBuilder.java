package personal.bw.shopper.data.datasource.queries;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.SelectArg;
import personal.bw.shopper.data.datasource.local.ShopHelperDatabaseHelper;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.models.ShoppingListProduct;

import java.sql.SQLException;

import static personal.bw.shopper.data.models.ShoppingListProduct.SHOPPINGLIST_ID_FIELD_NAME;

public class DeleteAllProductsForShoppingListQueryBuilder
{
	private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;
	private PreparedDelete<ShoppingListProduct> deleteProductsForShoppingListsQuery = null;

	public DeleteAllProductsForShoppingListQueryBuilder(ShopHelperDatabaseHelper shopHelperDatabaseHelper)
	{
		this.shopHelperDatabaseHelper = shopHelperDatabaseHelper;
	}

	public PreparedDelete<ShoppingListProduct> removeProductsForShoppingListFromDB(ShoppingList shoppingList) throws SQLException
	{
		if (deleteProductsForShoppingListsQuery == null)
		{
			deleteProductsForShoppingListsQuery = prepareDeleteProductsFroShoppingListQuery();
		}
		deleteProductsForShoppingListsQuery.setArgumentHolderValue(0, shoppingList.getId());
		return deleteProductsForShoppingListsQuery;
	}

	private PreparedDelete<ShoppingListProduct> prepareDeleteProductsFroShoppingListQuery() throws SQLException
	{
		DeleteBuilder<ShoppingListProduct, Long> deleteBuilder = shopHelperDatabaseHelper.getShoppingListProductDao().deleteBuilder();
		deleteBuilder.where().eq(SHOPPINGLIST_ID_FIELD_NAME, new SelectArg());
		return deleteBuilder.prepare();
	}

}
