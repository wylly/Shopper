package personal.bw.shopper.productdetails;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;

import static com.google.common.base.Preconditions.checkNotNull;


public class ProductDetailsPresenter implements ProductDetailsContract.Presenter
{
	private final ProductDetailsContract.View productDetailsView;
	private final DataSourceDealer repository;
	private int productId;
	private boolean isChecked;

	public ProductDetailsPresenter(
			@NonNull ProductDetailsContract.View productDetailsFragment,
			@NonNull DataSourceDealer repository,
			int productId)
	{
		this.productDetailsView = checkNotNull(productDetailsFragment, "productDetailsFragment cannot be null");
		this.repository = repository;
		this.productDetailsView.setPresenter(this);
		this.productId = productId;
	}

	@Override
	public void start()
	{
		populateProduct();
	}

	private void populateProduct()
	{
		Product product = repository.getProductFromCache(productId);
		if (productDetailsView.isActive())
		{
			productDetailsView.setName(product.getName());
			productDetailsView.setBrand(product.getBrand());
			productDetailsView.setDescription(product.getDescription());
			productDetailsView.setAmount(product.getAmount());
			isChecked = product.getChecked();
		}
	}

	@Override
	public void saveProduct(String name, String brand, String description, String amount)
	{
		Product product = new ProductBuilder(name)
				.withBrand(brand)
				.withDescription(description)
				.withAmount(amount)
				.withChecked(isChecked)
				.build();
		repository.updateProductInCache(productId, product);
		productDetailsView.goToProductsList();
	}
}
