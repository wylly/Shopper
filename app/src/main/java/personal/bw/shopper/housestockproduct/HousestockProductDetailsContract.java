package personal.bw.shopper.housestockproduct;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;
import personal.bw.shopper.upcdatabaseintegration.ProductInfo;

public interface HousestockProductDetailsContract
{
	interface View extends BaseView<Presenter>
	{

		void goToProductsList();

		void showProductSavedMessage();

		void showProductSaveErrorMessage(String error);

		void setName(String name);

		void setDescription(String description);

		void setBrand(String brand);

		void setDueDate(String dueDate);

		void setBarcode(String barcode);

		boolean isActive();

		void setAmount(String amount);

		void showProductDataDialog(ProductInfo productInfo);

		void showInvalid(ProductInfo productInfo);
	}

	interface Presenter extends BasePresenter
	{
		void saveProduct(String name, String brand, String description, String amount, String dueDate, String barcode);

		void handleResponse(ProductInfo productInfo, String barcodeNumber);

		void replaceData(ProductInfo productInfo);

		void replaceBarcode(String number);
	}
}
