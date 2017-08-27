package personal.bw.shopper.productlist.scanbarcode;

import android.content.Intent;
import android.support.annotation.NonNull;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepository;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.BaseProductListPresenter;

import java.util.List;

import static personal.bw.shopper.data.datasource.DataSourceAPI.LoadProductsForShoppingListCallback;
import static personal.bw.shopper.data.datasource.DataSourceAPI.PutProductCallback;

public class ScanBarcodePresenter extends BaseProductListPresenter implements ScanBarcodeContract.Presenter
{
	private ScanBarcodeContract.View scanBarcodeFragment;
	private String barcode = "";

	public ScanBarcodePresenter(
			@NonNull ScanBarcodeContract.View shoppingListsDetailsView,
			@NonNull DataSourceDealer shoppingListsRepository,
			@NonNull StringResourcesRepository stringResourcesRepository,
			Intent intent)
	{
		super(shoppingListsDetailsView, shoppingListsRepository, stringResourcesRepository, intent);
		scanBarcodeFragment = shoppingListsDetailsView;
		scanBarcodeFragment.setPresenter(this);
	}

	@Override
	public ShoppingList getShoppingList(Intent intent)
	{
		return new ShoppingList("Null object");
	}

	@Override
	public void start()
	{
		if (barcode.equals(""))
		{
			getShoppingListsDetailsView().setLoadingIndicator(true);
			scanBarcodeFragment.startScanningBarcode();
		}
	}

	@Override
	public void handleScannedBarcode(String barcode)
	{
		this.barcode = barcode;
		getRepository().getProductsWithBarcode(barcode, new LoadProductsForShoppingListCallback()
		{
			@Override
			public void onProductsForShoppingListLoaded(List<Product> products)
			{
				if (!getShoppingListsDetailsView().isActive())
				{
					return;
				}
				getShoppingListsDetailsView().setLoadingIndicator(false);
				processProducts(products);
			}

			@Override
			public void onDataNotAvailable()
			{
				getShoppingListsDetailsView().showLoadingProductsError();
			}
		});
	}

	@Override
	public void handleProductClick(Product clickedProduct)
	{
		scanBarcodeFragment.showMoveToTrashDialog(clickedProduct);
	}

	@Override
	public void moveToTrash(Product product)
	{
		getRepository().moveProductToTrash(product, new PutProductCallback()
		{
			@Override
			public void onProductPut(boolean isCreated)
			{
				scanBarcodeFragment.showMovedToTrash();
			}

			@Override
			public void onPuttingError()
			{
				scanBarcodeFragment.showProductDeletionError("Unable to move to trash");
			}
		});
	}
}
