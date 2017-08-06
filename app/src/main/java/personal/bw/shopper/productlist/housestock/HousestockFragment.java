package personal.bw.shopper.productlist.housestock;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.*;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.housestockproduct.HousestockProductDetailsActivity;
import personal.bw.shopper.productlist.BaseProductListFragment;

import java.util.List;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;
import static java.util.Collections.sort;
import static personal.bw.shopper.productlist.BaseProductListFragment.Action.NEW_PRODUCT;

public class HousestockFragment extends BaseProductListFragment
{
	private HousestockProductsListAdapter.ProductListener productListListener = makeProductListener();

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.housestock_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.add_new_housestock_item:
			{
				startNewHousestockProductActivity();
				break;
			}
		}
		return true;
	}

	private void startNewHousestockProductActivity()
	{
		Intent intent = new Intent(getContext(), HousestockProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), NEW_PRODUCT);
		startActivity(intent);
	}

	private HousestockProductsListAdapter.ProductListener makeProductListener()
	{
		return new HousestockProductsListAdapter.ProductListener()
		{
			@Override
			public void onProductClick(int clickedProduct)
			{
				startProductDetailsActivity(clickedProduct);
			}

			@Override
			public void onDeleteIconClick(int clickedProduct)
			{
				deleteProduct(clickedProduct);
			}

			@Override
			public void onTrashClick(int clickedProduct)
			{
				moveToTrash(clickedProduct);
			}
		};
	}

	private void moveToTrash(final int clickedProduct)
	{
		new AlertDialog.Builder(getActivity())
				.setTitle("Moving product to trash list")
				.setMessage("This will move product to trash list")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						getPresenter().moveToTrash(clickedProduct);
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
	protected void refreshProductList(List<Product> products)
	{
		sort(products, new DateProductsComparator());
		listAdapter = new HousestockProductsListAdapter(products, productListListener, getContext());
		listView.setAdapter(listAdapter);
	}

	@Override
	public void startProductDetailsActivity(int clickedProduct)
	{
		Intent intent = new Intent(getContext(), HousestockProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), Action.EDIT_PRODUCT);
		intent.putExtra(CLICKED_PRODUCT, clickedProduct);
		startActivity(intent);
	}
}
