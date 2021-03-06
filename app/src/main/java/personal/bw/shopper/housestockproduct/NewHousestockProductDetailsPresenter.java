package personal.bw.shopper.housestockproduct;

import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceAPI;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.builders.ProductBuilder;
import personal.bw.shopper.untils.CalendarConverter;
import personal.bw.shopper.upcdatabaseintegration.ProductInfo;

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
	public void saveProduct(String name, String brand, String description, String amount, String dueDate, String barcode)
	{
		try
		{
			Product product = makeProduct(name, brand, description, amount, dueDate, barcode);
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

	@Override
	public void handleResponse(ProductInfo productInfo, String barcodeNumber)
	{
		if (productInfo.isValid() && barcodeNumber.equals(productInfo.getNumber()))
		{
			productDetailsView.showProductDataDialog(productInfo);
		}else{
			productDetailsView.showInvalid(productInfo);
		}
	}

	@Override
	public void replaceData(ProductInfo productInfo)
	{
		if (productDetailsView.isActive())
		{
			productDetailsView.setName(productInfo.getItemname());
			productDetailsView.setDescription(productInfo.getFullDescription());
			productDetailsView.setBarcode(productInfo.getNumber());
		}
	}

	@Override
	public void replaceBarcode(String barcode)
	{
		if (productDetailsView.isActive())
		{
			productDetailsView.setBarcode(barcode);
		}
	}

	private Product makeProduct(String name, String brand, String description, String amount, String dueDate, String barcode) throws ParseException
	{
		return new ProductBuilder(name)
				.withBrand(brand)
				.withDescription(description)
				.withAmount(amount)
				.withBestBefore(dueDate)
				.withBarCode(barcode)
				.build();
	}
}
