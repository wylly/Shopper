package personal.bw.shopper.productdetails;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;
import personal.bw.shopper.data.models.Product;

public interface ProductDetailsContract {
     interface View extends BaseView<Presenter>{

         void setupPrefilledInputs(Product product);

         void showProductSavedMessage();

         void showProductSaveErrorMessage(String error);
     }

     interface Presenter extends BasePresenter{

         void saveProduct(String name, String brand, String description, String amount);
     }
}
