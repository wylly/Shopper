package personal.bw.shopper.productdetails;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewProductDetailsPresenter implements ProductDetailsContract.Presenter
{
	private final ProductDetailsContract.View productDetailsView;
	private final DataSourceDealer repository;

	public NewProductDetailsPresenter(
			@NonNull ProductDetailsContract.View productDetailsFragment,
			@NonNull DataSourceDealer repository)
	{
		this.productDetailsView = checkNotNull(productDetailsFragment, "NewProductDetailsPresenter cannot be null");
		this.repository = repository;
		this.productDetailsView.setPresenter(this);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void saveProduct(String name, String brand, String description, String amount)
	{
		Product product = new ProductBuilder(name)
				.withBrand(brand)
				.withDescription(description)
				.withAmount(amount)
				.withChecked(false)
				.build();
		repository.addProductCache(product);
		productDetailsView.goToProductsList();
	}
}
