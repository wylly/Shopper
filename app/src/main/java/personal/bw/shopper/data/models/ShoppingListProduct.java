package personal.bw.shopper.data.models;

import com.j256.ormlite.field.DatabaseField;

public class ShoppingListProduct {
    public final static String PRODUCT_ID_FIELD_NAME = "product_id";
    public final static String SHOPPINGLIST_ID_FIELD_NAME = "shoppinglist_id";

    @DatabaseField(generatedId = true)
    private int id;

    // This is a foreign object which just stores the id from the User object in this table.
    @DatabaseField(foreign = true, columnName = PRODUCT_ID_FIELD_NAME)
    private Product product;

    // This is a foreign object which just stores the id from the Post object in this table.
    @DatabaseField(foreign = true, columnName = SHOPPINGLIST_ID_FIELD_NAME)
    private ShoppingList shoppingList;

    ShoppingListProduct() {
    }

    public ShoppingListProduct(Product product, ShoppingList shoppingList) {
        this.product = product;
        this.shoppingList = shoppingList;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }
}
