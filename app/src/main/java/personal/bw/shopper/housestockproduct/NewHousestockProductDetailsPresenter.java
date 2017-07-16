package personal.bw.shopper.housestockproduct;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;

import java.text.ParseException;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewHousestockProductDetailsPresenter implements ProductDetailsContract.Presenter
{
	private final ProductDetailsContract.View productDetailsView;
	private final DataSourceDealer repository;

	public NewHousestockProductDetailsPresenter(
			@NonNull ProductDetailsContract.View productDetailsFragment,
			@NonNull DataSourceDealer repository)
	{
		this.productDetailsView = checkNotNull(productDetailsFragment, "NewHousestockProductDetailsPresenter cannot be null");
		this.repository = repository;
		this.productDetailsView.setPresenter(this);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void saveProduct(String name, String brand, String description,String amount, String dueDate)
	{
		try
		{
			Product product = makeProduct(name, brand, description,amount, dueDate);
			saveProduct(product);
		} catch (ParseException e)
		{
			productDetailsView.showProductSaveErrorMessage("Could not save product, date parsing error");
		}
	}

	private void saveProduct(Product product)
	{
		repository.saveHousestockProduct(product, new DataSourceAPI.PutProductCallback()
		{
			@Override
			public void onProductPut(boolean isCreated)
			{
				productDetailsView.showProductSavedMessage();
				productDetailsView.goToProductsList();
			}

			@Override
			public void onPuttingError()
			{
				productDetailsView.showProductSaveErrorMessage("Could nor save products, problem with writing to database");
			}
		});
	}

	private Product makeProduct(String name, String brand, String description, String amount, String dueDate) throws ParseException
	{
		return new ProductBuilder(name)
				.withBrand(brand)
				.withDescription(description)
				.withAmount(amount)
				.withBestBefore(dueDate)
				.build();
	}
}
