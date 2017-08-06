package personal.bw.shopper.productlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import personal.bw.shopper.R;
import personal.bw.shopper.ScrollAndRefreshLayout;
import personal.bw.shopper.data.models.Product;

import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.v4.content.ContextCompat.getColor;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseProductListFragment extends Fragment implements ShoppingListDetailsContract.View
{
	public static final String CLICKED_PRODUCT = "CLICKED PRODUCT";

	private ShoppingListDetailsContract.Presenter presenter;

	protected BaseAdapter listAdapter;
	protected LinearLayout productsView;

	protected View noProductsView;
	protected TextView noProductsAddView;
	protected ListView listView;
	protected ScrollAndRefreshLayout scrollAndRefreshLayout;

	public BaseProductListFragment()
	{
	}

	protected abstract View bindViews(LayoutInflater inflater, ViewGroup container);

	@Override
	public void onResume()
	{
		super.onResume();
		presenter.start();
		listView.setAdapter(listAdapter);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = bindViews(inflater, container);
		listView.setAdapter(listAdapter);
		createScrollAndRefreshLayout();
		setHasOptionsMenu(true);

		return root;
	}

	private void createScrollAndRefreshLayout()
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
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		presenter.result(requestCode, resultCode);
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
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

	protected abstract void refreshProductList(List<Product> products);

	protected void deleteProduct(final int clickedProductId)
	{
		new AlertDialog.Builder(getActivity())
				.setTitle("Deleting product")
				.setMessage("This will completely remove product from this Shopper application")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						getPresenter().deleteProduct(clickedProductId);
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
	public void showNoProducts()
	{
		productsView.setVisibility(GONE);
		noProductsView.setVisibility(VISIBLE);
		noProductsAddView.setVisibility(VISIBLE);
	}

	private void showMessage(String message)
	{
		Snackbar.make(getView(), message, LENGTH_LONG).show();
	}

	@Override
	public void showSuccessfullySavedMessage()
	{
		showMessage(getString(R.string.successfully_saved_shoppinglist_message));
	}

	@Override
	public void showLoadingProductsError()
	{
		showMessage(getString(R.string.loading_shoppinglists_error));
	}

	@Override
	public void showProductCheckFailed(String error)
	{
		showMessage("Product check failed because of: " + error);
	}

	@Override
	public void showProductUncheckFailed(String error)
	{
		showMessage("Product uncheck failed because of: " + error);
	}

	@Override
	public void showUnableToSaveShoppingList()
	{
		showMessage("Unable to save shopping list.");
	}

	private void showMessageShort(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void showProductChecked()
	{
		showMessageShort("Product checked");
	}

	@Override
	public void showProductUnchecked()
	{
		showMessageShort("Product unchecked");
	}

	@Override
	public void showShoppingListSavedMessage()
	{
		showMessageShort("Shopping list saved.");
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

	public enum Action
	{
		NEW_PRODUCT,
		EDIT_PRODUCT,
		CONVERT_PRODUCT
	}

	@Override
	public void setPresenter(@NonNull ShoppingListDetailsContract.Presenter presenter)
	{
		this.presenter = checkNotNull(presenter);
	}

	public ShoppingListDetailsContract.Presenter getPresenter()
	{
		return presenter;
	}
}
