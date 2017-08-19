package personal.bw.shopper.housestockproduct;

import android.support.annotation.NonNull;
import personal.bw.shopper.CalendarConverter;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;

import java.text.ParseException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewHousestockProductDetailsPresenter implements HousestockProductDetailsContract.Presenter
{
	private final HousestockProductDetailsContract.View productDetailsView;
	private final DataSourceDealer repository;
	private CalendarConverter calendarConverter = new CalendarConverter();

	public NewHousestockProductDetailsPresenter(
			@NonNull HousestockProductDetailsContract.View productDetailsFragment,
			@NonNull DataSourceDealer repository)
	{
		this.productDetailsView = checkNotNull(productDetailsFragment, "NewHousestockProductDetailsPresenter cannot be null");
		this.repository = repository;
		this.productDetailsView.setPresenter(this);
	}

	@Override
	public void start()
	{
		productDetailsView.setDueDate(calendarConverter.toString(new Date()));
	}

	@Override
	public void saveProduct(String name, String brand, String description, String amount, String dueDate)
	{
		try
		{
			Product product = makeProduct(name, brand, description, amount, dueDate);
			repository.addProductCache(product);
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
			productDetailsView.showProductSaveErrorMessage("Could not save product, date parsing error");
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
