package personal.bw.shopper.data.source;

import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.LinkedList;
import java.util.List;

public class Cache {
    private ShoppingList shoppingListCache;
    private List<Product> shoppingListToProductsCache;

    public Cache() {
    }

    private void createShoppingList() {

    }

    public ShoppingList readShoppingList() {
        return shoppingListCache;
    }

    public void updateShoppingList(ShoppingList shoppingList) {
        this.shoppingListCache = shoppingList;
    }

    private void deleteShoppingList() {

    }

    public void createProduct(Product product) {
        shoppingListToProductsCache.add(product);
    }

    public List<Product> readProducts() {
        return shoppingListToProductsCache;
    }

    public void updateProducts(List<Product> products) {
        this.shoppingListToProductsCache = products;
        if(products.equals(null)){
            this.shoppingListToProductsCache = new LinkedList<>();
        }
    }

    public void deleteProduct(int product) {
        shoppingListToProductsCache.remove(product);

    }

    public Product readProduct(int product) {
        return shoppingListToProductsCache.get(product);
    }

    public void updateProduct(int productId, Product product) {
        readProduct(productId).copyFrom(product);
    }
}
