package personal.bw.shopper.shoppinglists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.ShoppingList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListAdapter extends BaseAdapter
{
	private List<ShoppingList> shoppingLists;
	private ShoppingListListener itemListener;
	private Context context;

	public ShoppingListAdapter(List<ShoppingList> shoppingLists, ShoppingListListener itemListener, Context context)
	{
		setList(shoppingLists);
		this.itemListener = itemListener;
		this.context = context;
	}

	public void replaceData(List<ShoppingList> shoppingLists)
	{
		setList(shoppingLists);
		notifyDataSetChanged();
	}

	private void setList(List<ShoppingList> shoppingLists)
	{
		this.shoppingLists = checkNotNull(shoppingLists);
	}

	@Override
	public int getCount()
	{
		return shoppingLists.size();
	}

	@Override
	public ShoppingList getItem(int i)
	{
		return shoppingLists.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		view = initViewIfNull(view);

		final ShoppingList shoppingList = getItem(position);
		populateList(view, shoppingList);

		View shoppingListViewText = view.findViewById(R.id.shopping_lists_list_item);
		shoppingListViewText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onShoppingListClick(shoppingList);
			}
		});

		View shoppingListDeleteIcon = view.findViewById(R.id.shopping_lists_delete_icon);
		shoppingListDeleteIcon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onDeleteIconClick(shoppingList);
			}
		});

		return view;
	}

	private void populateList(View view, ShoppingList shoppingList)
	{
		if (shoppingList != null)
		{
			initListNameIfNull(view, shoppingList);
			initListDateIfNull(view, R.id.label_lists_date, shoppingList.getFormattedDate());
		}
	}

	private void initListDateIfNull(View view, int label_lists_date, String formattedDate)
	{
		TextView listDate = (TextView) view.findViewById(label_lists_date);
		if (listDate != null)
		{
			listDate.setText(formattedDate);
		}
	}

	private void initListNameIfNull(View view, ShoppingList shoppingList)
	{
		TextView listName = (TextView) view.findViewById(R.id.label_lists_name);
		if (listName != null)
		{
			listName.setText(shoppingList.getName());
		}
	}

	private View initViewIfNull(View view)
	{
		if (view == null)
		{
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			view = layoutInflater.inflate(R.layout.shopping_lists_list_item, null);
		}
		return view;
	}

	public interface ShoppingListListener
	{
		void onShoppingListClick(ShoppingList clickedShoppingList);

		void onDeleteIconClick(ShoppingList clickedShoppingList);
	}
}
