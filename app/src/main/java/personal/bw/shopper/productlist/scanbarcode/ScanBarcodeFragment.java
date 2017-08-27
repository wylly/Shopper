package personal.bw.shopper.productlist.scanbarcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.productlist.BaseProductListFragment;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

public class ScanBarcodeFragment extends BaseProductListFragment implements ScanBarcodeContract.View
{
	private ScanBarcodeContract.Presenter presenter;
	private ScanBarcodeListAdapter.ProductListener productListListener = makeProductListener();

	private ScanBarcodeListAdapter.ProductListener makeProductListener()
	{
		return new ScanBarcodeListAdapter.ProductListener()
		{
			@Override
			public void onProductClick(Product clickedProduct)
			{
				presenter.handleProductClick(clickedProduct);
			}
		};
	}

	@Override
	public void setPresenter(ScanBarcodeContract.Presenter presenter)
	{
		this.presenter = presenter;
	}

	public void startScanningBarcode()
	{
		IntentIntegrator.forSupportFragment(this).initiateScan();
	}

	@Override
	public void showMoveToTrashDialog(final Product product)
	{
		new AlertDialog.Builder(getActivity())
				.setTitle("Move product to trash list")
				.setMessage("This will move product from housestock to trash list")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						presenter.moveToTrash(product);
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//Nothig happens
			}
		}).create()
				.show();
	}

	@Override
	public void showMovedToTrash()
	{
		Toast.makeText(getContext(), "Moved to trash", LENGTH_SHORT).show();
		getActivity().finish();
	}


	@Override
	protected View bindViews(LayoutInflater inflater, ViewGroup container)
	{
		View root = inflater.inflate(R.layout.housestock_list, container, false);
		bind(this, root);
		productsView = findById(root, R.id.housestock_productsLL);
		noProductsView = findById(root, R.id.housestock_noProducts);
		noProductsAddView = findById(root, R.id.housestock_noProductsAdd);
		listView = findById(root, R.id.housestock_productsList);
		scrollAndRefreshLayout = findById(root, R.id.housestock_products_refresh_layout);
		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null)
		{
			String barcode = result.getContents();
			if (barcode == null)
			{
				Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getContext(), "Scanned: " + barcode, Toast.LENGTH_SHORT).show();
				presenter.handleScannedBarcode(barcode);
			}
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void refreshProductList(List<Product> products)
	{
		listAdapter = new ScanBarcodeListAdapter(products, productListListener, getContext());
		listView.setAdapter(listAdapter);
	}

	@Override
	public void startProductDetailsActivity(int clickedProduct)
	{

	}
}
