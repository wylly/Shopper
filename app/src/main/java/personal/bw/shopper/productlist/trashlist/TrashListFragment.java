package personal.bw.shopper.productlist.trashlist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.housestockproduct.HousestockProductDetailsActivity;
import personal.bw.shopper.productlist.BaseProductListFragment;
import personal.bw.shopper.productlist.housestock.DateProductsComparator;

import java.util.List;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;
import static java.util.Collections.sort;

public class TrashListFragment extends BaseProductListFragment
{
	private TrashListAdapter.ProductListener productListListener = makeProductListener();

	private TrashListAdapter.ProductListener makeProductListener()
	{
		return new TrashListAdapter.ProductListener()
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
		};
	}

	@Override
	protected View bindViews(LayoutInflater inflater, ViewGroup container)
	{
		View root = inflater.inflate(R.layout.trash_list, container, false);
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
		listAdapter = new TrashListAdapter(products, productListListener, getContext());
		listView.setAdapter(listAdapter);
	}

	@Override
	public void startProductDetailsActivity(int clickedProduct)
	{
		Intent intent = new Intent(getContext(), HousestockProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), BaseProductListFragment.Action.EDIT_PRODUCT);
		intent.putExtra(CLICKED_PRODUCT, clickedProduct);
		startActivity(intent);
	}
}
