package personal.bw.shopper.productlist.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public class ProductsListAdapter extends BaseAdapter
{
	private List<Product> products;
	private ProductListener itemListener;
	private Context context;

	public ProductsListAdapter(List<Product> products, ProductListener itemListener, Context context)
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
			view = layoutInflater.inflate(R.layout.product_list_item, null);
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
		holder.productAmount.setText(product.getAmount());
		holder.productCheckBox.setChecked(product.getChecked());
		holder.productViewText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onProductClick(position);
			}
		});
		holder.productDeleteIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onDeleteIconClick(position);
			}
		});
		holder.productMoveToHousestock.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				itemListener.onMoveToHousestock(position);
			}
		});
		holder.productCheckBox.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (((CheckBox) view).isChecked()) itemListener.onProductCheck(position);
				else itemListener.onProductUncheck(position);
			}
		});

		return view;
	}

	public interface ProductListener
	{
		public void onProductClick(int clickedProduct);

		public void onDeleteIconClick(int clickedProduct);

		public void onMoveToHousestock(int clickedProduct);

		public void onProductCheck(int clickedProduct);

		public void onProductUncheck(int clickedProduct);
	}

	static class ViewHolder
	{
		@BindView(R.id.label_product_name)
		TextView productName;

		@BindView(R.id.label_product_brand)
		TextView productBrand;

		@BindView(R.id.label_product_amount)
		TextView productAmount;

		@BindView(R.id.products_list_item)
		View productViewText;

		@BindView(R.id.product_delete_icon)
		View productDeleteIcon;

		@BindView(R.id.move_to_housestock_product)
		View productMoveToHousestock;

		@BindView(R.id.checkbox_products_list)
		CheckBox productCheckBox;

		public ViewHolder(View view)
		{
			ButterKnife.bind(this, view);
		}
	}
}
