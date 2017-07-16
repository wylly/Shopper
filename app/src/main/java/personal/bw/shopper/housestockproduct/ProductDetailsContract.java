package personal.bw.shopper.housestockproduct;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;

public interface ProductDetailsContract
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

		boolean isActive();

		void setAmount(String amount);
	}

	interface Presenter extends BasePresenter
	{
		void saveProduct(String name, String brand, String description, String amount, String dueDate);
	}
}
