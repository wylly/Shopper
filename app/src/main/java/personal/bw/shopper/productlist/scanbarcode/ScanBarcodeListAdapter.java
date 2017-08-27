package personal.bw.shopper.productlist.scanbarcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.bw.shopper.untils.CalendarConverter;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public class ScanBarcodeListAdapter extends BaseAdapter
{
	private CalendarConverter calendarConverter = new CalendarConverter();
	private List<Product> products;
	private ProductListener itemListener;
	private Context context;

	public ScanBarcodeListAdapter(List<Product> products, ProductListener itemListener, Context context)
	{
		this.products = products;
		this.itemListener = itemListener;
		this.context = context;
	}

	public void replaceData(List<Product> products)
	{
		this.products.clear();
		this.products.addAll(products);
		this.notifyDataSetChanged();
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
		ViewHolder viewHolder;

		if (view == null)
		{
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			view = layoutInflater.inflate(R.layout.scan_result_list_item, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = ((ViewHolder) view.getTag());
		}

		final Product product = getItem(position);
		viewHolder.productName.setText(product.getName());
		viewHolder.productBrand.setText(product.getBrand());
		viewHolder.productDate.setText(calendarConverter.toString(product.getBestBefore()));
		viewHolder.productViewText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onProductClick(products.get(position));
			}
		});
		return view;
	}

	public interface ProductListener
	{
		void onProductClick(Product product);
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

		public ViewHolder(View view)
		{
			ButterKnife.bind(this, view);
		}
	}
}
