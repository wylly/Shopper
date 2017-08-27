package personal.bw.shopper.productlist.scanbarcode;

import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.productlist.ProductListContract;

public interface ScanBarcodeContract
{
	interface View extends ProductListContract.View
	{
		void setPresenter(Presenter presenter);

		void startScanningBarcode();

		void showMoveToTrashDialog(final Product clickedShoppingList);

		void showMovedToTrash();
	}

	interface Presenter extends ProductListContract.Presenter
	{
		void handleScannedBarcode(String barcode);

		void handleProductClick(Product clickedProduct);

		void moveToTrash(Product product);
	}
}
