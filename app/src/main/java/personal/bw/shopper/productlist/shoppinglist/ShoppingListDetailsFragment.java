package personal.bw.shopper.productlist.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.EditText;
import butterknife.OnClick;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.housestockproduct.HousestockProductDetailsActivity;
import personal.bw.shopper.productdetails.ProductDetailsActivity;
import personal.bw.shopper.productlist.BaseProductListFragment;

import java.util.LinkedList;
import java.util.List;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;
import static java.util.Collections.sort;
import static personal.bw.shopper.productlist.BaseProductListFragment.Action.NEW_PRODUCT;

public class ShoppingListDetailsFragment extends BaseProductListFragment
{
	private EditText shoppingListName;

	private ProductsListAdapter.ProductListener productListListener = makeProductListener();

	@Override
	protected View bindViews(LayoutInflater inflater, ViewGroup container)
	{
		View root = inflater.inflate(R.layout.shopping_list_details, container, false);
		bind(this, root);
		productsView = findById(root, R.id.productsLL);
		noProductsView = findById(root, R.id.noProducts);
		noProductsAddView = findById(root, R.id.noProductsAdd);
		shoppingListName = findById(root, R.id.input_shopping_list_name);
		listView = findById(root, R.id.productsList);
		scrollAndRefreshLayout = findById(root, R.id.products_refresh_layout);
		shoppingListName.setText(getPresenter().getShoppingListName());
		return root;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listAdapter = new ProductsListAdapter(new LinkedList<Product>(), productListListener, getContext());

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.products_fragment_menu, menu);
	}

	@Override
	public void refreshProductList(List<Product> products)
	{
		sort(products, new CheckedAndNamedProductsComparator());
		listAdapter = new ProductsListAdapter(products, productListListener, getContext());
		listView.setAdapter(listAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.add_new_product:
			{
				startAddProductActivity();
				break;
			}
			case R.id.save_shopping_list:
			{
				getPresenter().setShoppingListName(shoppingListName.getText().toString());
				getPresenter().saveShoppingList();
				getActivity().finish();
				break;
			}
		}
		return true;
	}

	@OnClick(R.id.noProducts)
	public void startAddProductActivity()
	{
		Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), NEW_PRODUCT);
		startActivity(intent);
	}

	private ProductsListAdapter.ProductListener makeProductListener()
	{
		return new ProductsListAdapter.ProductListener()
		{
			@Override
			public void onProductClick(int clickedProductId)
			{
				startProductDetailsActivity(clickedProductId);
			}

			@Override
			public void onDeleteIconClick(final int clickedProductId)
			{
				deleteProduct(clickedProductId);
			}

			@Override
			public void onMoveToHousestock(int clickedProduct)
			{
				startMoveToHousestockActivity(clickedProduct);
			}

			@Override
			public void onProductCheck(int clickedProductId)
			{
				getPresenter().markProductChecked(clickedProductId);
				getPresenter().loadProducts();
			}

			@Override
			public void onProductUncheck(int clickedProductId)
			{
				getPresenter().markProductUnchecked(clickedProductId);
				getPresenter().loadProducts();
			}
		};
	}

	private void startMoveToHousestockActivity(int clickedProduct)
	{
		Intent intent = new Intent(getContext(), HousestockProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), Action.CONVERT_PRODUCT);
		intent.putExtra(CLICKED_PRODUCT, clickedProduct);
		startActivity(intent);
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
