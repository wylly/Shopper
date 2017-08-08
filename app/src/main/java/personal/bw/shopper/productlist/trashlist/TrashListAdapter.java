package personal.bw.shopper.productlist.trashlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.bw.shopper.CalendarConverter;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public class TrashListAdapter extends BaseAdapter
{
	private CalendarConverter calendarConverter = new CalendarConverter();
	private List<Product> products;
	private ProductListener itemListener;
	private Context context;

	public TrashListAdapter(List<Product> products, ProductListener itemListener, Context context)
	{
		this.products = products;
		this.itemListener = itemListener;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return products.size();
	}

	@Override
	public Product getItem(int i)
	{
		return products.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent)
	{
		ViewHolder holder;

		if (view == null)
		{
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			view = layoutInflater.inflate(R.layout.trash_list_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}

		final Product product = getItem(position);
		holder.productName.setText(product.getName());
		holder.productBrand.setText(product.getBrand());
		holder.productDate.setText(calendarConverter.toString(product.getBestBefore()));
		holder.productDeleteIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onDeleteIconClick(position);
			}
		});
		holder.productViewText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onProductClick(position);
			}
		});
		return view;
	}

	public interface ProductListener
	{
		void onProductClick(int clickedProduct);

		void onDeleteIconClick(int clickedProduct);
	}

	static class ViewHolder
	{
		@BindView(R.id.label_product_name)
		TextView productName;

		@BindView(R.id.label_product_brand)
		TextView productBrand;

		@BindView(R.id.label_product_date)
		TextView productDate;

		@BindView(R.id.products_list_item)
		View productViewText;

		@BindView(R.id.product_delete_icon)
		View productDeleteIcon;

		public ViewHolder(View view)
		{
			ButterKnife.bind(this, view);
		}
	}
}
