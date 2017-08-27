package personal.bw.shopper.shoppinglists;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.*;
import personal.bw.shopper.R;
import personal.bw.shopper.ScrollAndRefreshLayout;
import personal.bw.shopper.data.models.ShoppingList;
import personal.bw.shopper.productlist.shoppinglist.existingshoppinglist.ExistingShoppingListDetailsActivity;
import personal.bw.shopper.productlist.shoppinglist.newshoppinglist.NewShoppingListDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class ShoppingListsFragment extends Fragment implements ShoppingListsContract.View
{
	public static final String CLICKED_SHOPPING_LIST = "CLICKED_SHOPPING_LIST";
	public static final String COMMAND = "COMMAND";
	private ShoppingListsContract.Presenter presenter;
	private ShoppingListAdapter listAdapter;
	private LinearLayout shoppingListsView;
	private View noShoppingListsView;
	private TextView noShoppingListsAddView;
	private TextView noShoppingListsMainView;
	private ImageView noShoppingListsIcon;

	public ShoppingListsFragment()
	{
		// Requires empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listAdapter = new ShoppingListAdapter(new ArrayList<ShoppingList>(), mItemListener, getContext());
	}

	@Override
	public void onResume()
	{
		super.onResume();
		presenter.start();
	}

	@Override
	public void setPresenter(@NonNull ShoppingListsContract.Presenter presenter)
	{
		this.presenter = checkNotNull(presenter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		presenter.result(requestCode, resultCode);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.shopping_lists, container, false);

		ListView listView = (ListView) root.findViewById(R.id.shoppingListsList);
		listView.setAdapter(listAdapter);
		shoppingListsView = (LinearLayout) root.findViewById(R.id.listsLL);

		setUpNoShoppingListsViews(root);

		final ScrollAndRefreshLayout scrollAndRefreshLayout = setupProgressIndicator(root);

		scrollAndRefreshLayout.setScrollUpChild(listView);
		scrollAndRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				presenter.loadShoppingLists();
			}
		});

		setHasOptionsMenu(true);

		return root;
	}

	private ScrollAndRefreshLayout setupProgressIndicator(View root)
	{
		final ScrollAndRefreshLayout scrollAndRefreshLayout =
				(ScrollAndRefreshLayout) root.findViewById(R.id.refresh_layout);
		scrollAndRefreshLayout.setColorSchemeColors(
				ContextCompat.getColor(getActivity(), R.color.colorPrimary),
				ContextCompat.getColor(getActivity(), R.color.colorAccent),
				ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
		);
		return scrollAndRefreshLayout;
	}

	private void setUpNoShoppingListsViews(View root)
	{
		noShoppingListsView = root.findViewById(R.id.noShoppingLists);
		noShoppingListsIcon = (ImageView) root.findViewById(R.id.noShoppingListsIcon);
		noShoppingListsMainView = (TextView) root.findViewById(R.id.noShoppingListsMain);
		noShoppingListsAddView = (TextView) root.findViewById(R.id.noShoppingListsAdd);
		noShoppingListsView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startAddShoppingListActivity();
			}
		});
	}

	@Override
	public void startAddShoppingListActivity()
	{
		Intent intent = new Intent(getContext(), NewShoppingListDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.add_new_shopping_list:
				startAddShoppingListActivity();
				break;
		}
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.shopping_lists_fragment_menu, menu);
	}

	@Override
	public void setLoadingIndicator(final boolean active)
	{

		if (getView() == null)
		{
			return;
		}
		final SwipeRefreshLayout srl =
				(SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

		// Make sure setRefreshing() is called after the layout is done with everything else.
		srl.post(new Runnable()
		{
			@Override
			public void run()
			{
				srl.setRefreshing(active);
			}
		});
	}

	@Override
	public void showShoppingLists(List<ShoppingList> shoppingLists)
	{
		listAdapter.replaceData(shoppingLists);
		shoppingListsView.setVisibility(View.VISIBLE);
		noShoppingListsView.setVisibility(View.GONE);
	}

	@Override
	public void showNoShoppingLists()
	{
		shoppingListsView.setVisibility(View.GONE);
		noShoppingListsView.setVisibility(View.VISIBLE);
		noShoppingListsAddView.setVisibility(View.VISIBLE);
	}

	@Override
	public void showSuccessfullySavedMessage()
	{
		showMessage(getString(R.string.successfully_saved_shoppinglist_message));
	}

	@Override
	public void showShoppingListDeletedToast()
	{
		Toast.makeText(getContext(), R.string.shopping_list_deletion_successfull, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showShoppingListDeletionError(String info)
	{
		Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showLoadingShoppingListsError()
	{
		showMessage(getString(R.string.loading_shoppinglists_error));
	}

	private void showMessage(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	ShoppingListAdapter.ShoppingListListener mItemListener = new ShoppingListAdapter.ShoppingListListener()
	{
		@Override
		public void onShoppingListClick(ShoppingList clickedShoppingList)
		{
			Intent intent = new Intent(getContext(), ExistingShoppingListDetailsActivity.class);
			intent.putExtra(CLICKED_SHOPPING_LIST, clickedShoppingList);
			startActivity(intent);
		}

		@Override
		public void onDeleteIconClick(final ShoppingList clickedShoppingList)
		{
			new AlertDialog.Builder(getActivity())
					.setTitle("Deleting shopping list")
					.setMessage("This will completely remove shopping list from this Shopper application")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							presenter.deleteShoppingList(clickedShoppingList);
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

	public enum Command
	{
		EDIT,
		HOUSEHOLD_LIST, NEW
	}
}
