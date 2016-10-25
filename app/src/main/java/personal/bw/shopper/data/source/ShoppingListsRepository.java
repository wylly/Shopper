package personal.bw.shopper.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.data.source.local.ShopHelperDatabaseHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ShoppingListsRepository {
    private static ShoppingListsRepository INSTANCE = null;
    private final ShopHelperDatabaseHelper shopHelperDatabaseHelper;

    private ShoppingListsRepository(Context context) {
        shopHelperDatabaseHelper = new ShopHelperDatabaseHelper(context);
    }

    public static ShoppingListsRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingListsRepository(context);
        }
        return INSTANCE;
    }

    public void readAllOrderedShoppingLists(@NonNull DataSourceAPI.LoadShoppingListsCallback callback) {
        try {
            callback.onShoppingListsLoaded(lookupAllShoppingListsOrdered());
        } catch (SQLException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        }
    }

    private List<ShoppingList> lookupAllShoppingListsOrdered() throws SQLException {
        return shopHelperDatabaseHelper.getShoppingListDao().query(
                shopHelperDatabaseHelper
                        .getShoppingListDao()
                        .queryBuilder()
                        .groupBy(ShoppingList.DATE_FIELD_NAME)
                        .prepare()
        );
    }

    public void deleteShoppingList(ShoppingList shoppingListToDelete, DataSourceAPI.DeleteShoppingListCallback deleteShoppingListCallback) {
        try {
            int result = shopHelperDatabaseHelper.getShoppingListDao().delete(shoppingListToDelete);
            if (result > 1)
                deleteShoppingListCallback.onDeletionError("Deleted more than 1 shopping list");
            else if (result == 1)
                deleteShoppingListCallback.onShoppingListDeleted();
            else if (result == 0)
                deleteShoppingListCallback.onDeletionError("Couldn't delete shopping list - shopping list not found");
            else if (result < 0)
                deleteShoppingListCallback.onDeletionError("Couldn't delete shopping list - error unknown");
        } catch (SQLException e) {
            e.printStackTrace();
            deleteShoppingListCallback.onDeletionError("Exception occurred");
        }
    }

    private ShoppingList readShoppingList(Long shoppingListId) throws SQLException {
        return shopHelperDatabaseHelper.getShoppingListDao().queryForId(shoppingListId);
    }

    //TODO Move or delete
    private HashMap<Long, ShoppingList> mapShoppingListsToMap(List<ShoppingList> list) {
        HashMap<Long, ShoppingList> map = new HashMap<>();
        if (list != null) {
            for (ShoppingList shoppingList : list) {
                map.put(shoppingList.getId(), shoppingList);
            }
        }
        return map;
    }

    public void createOrUpdateShoppingList(ShoppingList shoppingListCache, DataSourceAPI.PutShooppingListCallback putShooppingListCallback) {
        try {
            shopHelperDatabaseHelper.getShoppingListDao().createOrUpdate(shoppingListCache);
            putShooppingListCallback.onShoppingListPut();
        } catch (SQLException e) {
            e.printStackTrace();
            putShooppingListCallback.onShoppingListPutFailure();
        }
    }
}
