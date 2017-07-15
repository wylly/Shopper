package personal.bw.shopper.housestockproduct;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.OnClick;
import personal.bw.shopper.R;
import personal.bw.shopper.ScrollAndRefreshLayout;
import personal.bw.shopper.data.models.Product;
import personal.bw.shopper.productdetails.ProductDetailsActivity;
import personal.bw.shopper.productlist.housestock.DateProductsComparator;
import personal.bw.shopper.productlist.housestock.HousestockProductsListAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.v4.content.ContextCompat.getColor;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static butterknife.ButterKnife.bind;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.sort;
import static personal.bw.shopper.productlist.BaseProductListFragment.Action.NEW_PRODUCT;

public class HousestockProductFragment extends Fragment implements HousestockProductContract.View
{
	public static final String CLICKED_PRODUCT = "CLICKED PRODUCT";
	private HousestockProductContract.Presenter presenter;
	private HousestockProductsListAdapter listAdapter;

	@BindView(R.id.productsLL)
	LinearLayout productsView;

	@BindView(R.id.noProducts)
	View noProductsView;

	@BindView(R.id.noProductsAdd)
	TextView noProductsAddView;

	@BindView(R.id.input_shopping_list_name)
	EditText shoppingListName;

	@BindView(R.id.productsList)
	ListView listView;

	@BindView(R.id.products_refresh_layout)
	ScrollAndRefreshLayout scrollAndRefreshLayout;

	public HousestockProductFragment()
	{
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listAdapter = new HousestockProductsListAdapter(new ArrayList<Product>(), mItemListener, getContext());
	}

	@Override
	public void onResume()
	{
		super.onResume();
		presenter.start();
		listView.setAdapter(listAdapter);
	}

	@Override
	public void setPresenter(@NonNull HousestockProductContract.Presenter presenter)
	{
		this.presenter = checkNotNull(presenter);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.shopping_list_details, container, false);
		bind(this, root);
		listView.setAdapter(listAdapter);
		shoppingListName.setText(presenter.getShoppingListName());

		createScrollAndRefreshLayout(root);

		setHasOptionsMenu(true);

		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		presenter.result(requestCode, resultCode);
	}

	private void createScrollAndRefreshLayout(View root)
	{
		scrollAndRefreshLayout.setColorSchemeColors(
				getColor(getActivity(), R.color.colorPrimary),
				getColor(getActivity(), R.color.colorAccent),
				getColor(getActivity(), R.color.colorPrimaryDark)
		);
		scrollAndRefreshLayout.setScrollUpChild(listView);
		scrollAndRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				presenter.loadProducts();
			}
		});
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
				presenter.setShoppingListName(shoppingListName.getText().toString());
				presenter.saveShoppingList();
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.products_fragment_menu, menu);
	}

	@Override
	public void setLoadingIndicator(final boolean active)
	{
		if (getView() == null)
		{
			return;
		}
		// Make sure setRefreshing() is called after the layout is done with everything else.
		scrollAndRefreshLayout.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrollAndRefreshLayout.setRefreshing(active);
			}
		});
	}

	@Override
	public void showProducts(List<Product> products)
	{
		refreshProductList(products);
		productsView.setVisibility(VISIBLE);
		noProductsView.setVisibility(GONE);
	}

	@Override
	public void showNoProducts()
	{
		productsView.setVisibility(GONE);
		noProductsView.setVisibility(VISIBLE);
		noProductsAddView.setVisibility(VISIBLE);
	}

	@Override
	public void showSuccessfullySavedMessage()
	{
		showMessage(getString(R.string.successfully_saved_shoppinglist_message));
	}

	@Override
	public void showProductDeletedToast()
	{
		Toast.makeText(getContext(), R.string.shopping_list_deletion_successfull, LENGTH_SHORT).show();
	}

	@Override
	public void showProductDeletionError(String info)
	{
		Toast.makeText(getContext(), info, LENGTH_SHORT).show();
	}

	@Override
	public void startProductDetailsActivity(int clickedProduct)
	{
		Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
		intent.putExtra(getString(R.string.intent_action), Action.EDIT_PRODUCT);
		intent.putExtra(CLICKED_PRODUCT, clickedProduct);
		startActivity(intent);
	}

	@Override
	public void showLoadingProductsError()
	{
		showMessage(getString(R.string.loading_shoppinglists_error));
	}

	private void showMessage(String message)
	{
		Snackbar.make(getView(), message, LENGTH_LONG).show();
	}

	private void showMessageShort(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	@Override
	public void showProductChecked()
	{
		showMessageShort("Product checked");
	}

	@Override
	public void showProductCheckFailed(String error)
	{
		showMessage("Product check failed because of: " + error);
	}

	@Override
	public void showProductUnchecked()
	{
		showMessageShort("Product unchecked");
	}

	@Override
	public void showProductUncheckFailed(String error)
	{
		showMessage("Product uncheck failed because of: " + error);
	}

	@Override
	public void showShoppingListSavedMessage()
	{
		showMessageShort("Shopping list saved.");
	}

	@Override
	public void showUnableToSaveShoppingList()
	{
		showMessage("Unable to save shopping list.");
	}

	public enum Action
	{
		NEW_PRODUCT,
		EDIT_PRODUCT
	}

	private void refreshProductList(List<Product> products)
	{
		sort(products, new DateProductsComparator());
		listAdapter = new HousestockProductsListAdapter(products, mItemListener, getContext());
		listView.setAdapter(listAdapter);
	}

	private HousestockProductsListAdapter.ProductListener mItemListener = new HousestockProductsListAdapter.ProductListener()
	{
		@Override
		public void onProductClick(int clickedProductId)
		{
			startProductDetailsActivity(clickedProductId);
		}

		@Override
		public void onDeleteIconClick(final int clickedProductId)
		{
			new AlertDialog.Builder(getActivity())
					.setTitle("Deleting product")
					.setMessage("This will completely remove product from this Shopper application")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							presenter.deleteProduct(clickedProductId);
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
		public void onTrashClick(final int clickedProduct)
		{
			new AlertDialog.Builder(getActivity())
					.setTitle("Moving product to trash list")
					.setMessage("This will move product to trash list")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							presenter.moveToTrash(clickedProduct);
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
	};
}
