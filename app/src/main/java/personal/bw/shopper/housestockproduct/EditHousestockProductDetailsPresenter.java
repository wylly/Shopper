package personal.bw.shopper.housestockproduct;

import android.support.annotation.NonNull;
import personal.bw.shopper.CalendarConverter;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;

import java.text.ParseException;

import static com.google.common.base.Preconditions.checkNotNull;


public class EditHousestockProductDetailsPresenter implements HousestockProductDetailsContract.Presenter
{
	private CalendarConverter calendarConverter = new CalendarConverter();
	private final HousestockProductDetailsContract.View productDetailsView;
	private final DataSourceDealer repository;
	private int productId;

	public EditHousestockProductDetailsPresenter(
			@NonNull HousestockProductDetailsContract.View productDetailsFragment,
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
			productDetailsView.setDueDate(calendarConverter.toString(product.getBestBefore()));
		}
	}



	@Override
	public void saveProduct(String name, String brand, String description, String amount, String dueDate)
	{
		try
		{
			Product product = makeProduct(name, brand, description, amount, dueDate);
			repository.updateProductInCache(productId, product);
			repository.saveShoppingList(new DataSourceAPI.SaveShoppingListCallback()
			{
				@Override
				public void onShoppingListSave()
				{
					productDetailsView.showProductSavedMessage();
					productDetailsView.goToProductsList();
				}

				@Override
				public void onShoppingListSaveFailure()
				{
					productDetailsView.showProductSaveErrorMessage("Unable to update house stock list");
				}
			});
		} catch (ParseException e)
		{
			e.printStackTrace();
			productDetailsView.showProductSaveErrorMessage("Could not save products, problem with date parsing");
		}
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
