package personal.bw.shopper.data.source;

import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;

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

    public void createOrUpdateProduct(Product product) {
        for(Product cacheProduct : shoppingListToProductsCache){
            if(cacheProduct.getId().equals(product.getId())){
                updateProduct(product,cacheProduct);
                return;
            }
        }
        shoppingListToProductsCache.add(product);
    }

    public List<Product> readProducts() {
        return shoppingListToProductsCache;
    }

    public void updateProducts(List<Product> products) {
        this.shoppingListToProductsCache = products;
    }

    private void updateProduct(Product source, Product destination) {
        destination.copyFrom(source);
    }

    public void deleteProduct(Product product) {
        shoppingListToProductsCache.remove(product);

    }
}
