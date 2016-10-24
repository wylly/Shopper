package personal.bw.shopper.productdetails;

import personal.bw.shopper.BasePresenter;
import personal.bw.shopper.BaseView;

public interface ProductDetailsContract {
     interface View extends BaseView<Presenter>{

         void showProductSavedMessage();

         void showProductSaveErrorMessage(String error);
     }

     interface Presenter extends BasePresenter{

         void saveProduct(String name, String brand, String description, String amount);
     }
}
