package personal.bw.shopper.productlist.housestock;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.productdetails.ProductDetailsActivity;
import personal.bw.shopper.productlist.BaseProductListFragment;
import personal.bw.shopper.productlist.shoppinglist.CheckedAndNamedProductsComparator;

import java.util.List;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;
import static java.util.Collections.sort;

public class HousestockFragment extends BaseProductListFragment
{
	private HousestockProductsListAdapter.ProductListener productListListener = makeProductListener();

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

	private void moveToTrash(int clickedProduct)
	{

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
		sort(products, new CheckedAndNamedProductsComparator());
		listAdapter = new HousestockProductsListAdapter(products, productListListener, getContext());
		listView.setAdapter(listAdapter);
	}

	@Override
	public void startProductDetailsActivity(int clickedProduct)
	{
		Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), Action.EDIT_PRODUCT);
		intent.putExtra(CLICKED_PRODUCT, clickedProduct);
		startActivity(intent);
	}
}
