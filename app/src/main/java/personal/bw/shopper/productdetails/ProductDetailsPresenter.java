package personal.bw.shopper.productdetails;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.builders.ProductBuilder;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.source.DataSourceDealer;

import static com.google.common.base.Preconditions.checkNotNull;


public class ProductDetailsPresenter implements ProductDetailsContract.Presenter {
    private final ProductDetailsContract.View productDetailsView;
    private final DataSourceDealer repository;
    private Product product;

    public ProductDetailsPresenter(@NonNull ProductDetailsContract.View productDetailsFragment, @NonNull DataSourceDealer repository, Product product) {
        this.productDetailsView = checkNotNull(productDetailsFragment, "productDetailsFragment cannot be null");
        this.repository = repository;
        this.productDetailsView.setPresenter(this);
        this.product = product;
    }

    @Override
    public void start() {
        productDetailsView.setupPrefilledInputs(product);
    }

    @Override
    public void saveProduct(String name, String brand, String description, String amount) {
        repository.addEditProductCache(
                new ProductBuilder(name)
                        .withBrand(brand)
                        .withDescription(description)
                        .withAmount(amount)
                        .build()
        );
    }
}
