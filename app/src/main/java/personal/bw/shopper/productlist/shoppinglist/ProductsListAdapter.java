package personal.bw.shopper.productlist.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;

import java.util.List;

import static butterknife.ButterKnife.bind;

public class ProductsListAdapter extends BaseAdapter
{
	private List<Product> products;
	private ProductListener itemListener;
	private Context context;

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

	@BindView(R.id.checkbox_products_list)
	CheckBox productCheckBox;

	public ProductsListAdapter(List<Product> products, ProductListener itemListener, Context context)
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View view = convertView;

		if (view == null)
		{
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			view = layoutInflater.inflate(R.layout.product_list_item, null);
			bind(this, view);
		}

		final Product product = getItem(position);

		if (product != null)
		{
			if (productName != null)
			{
				productName.setText(product.getName());
				productName.invalidate();
			}

			if (productBrand != null)
			{
				productBrand.setText(product.getBrand());
				productBrand.invalidate();
			}

			if (productAmount != null)
			{
				productAmount.setText(product.getAmount());
				productAmount.invalidate();
			}
		}

		productCheckBox.setChecked(product.getChecked());
		productViewText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onProductClick(position);
			}
		});
		productDeleteIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemListener.onDeleteIconClick(position);
			}
		});
		productCheckBox.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (((CheckBox) view).isChecked()) itemListener.onProductCheck(position);
				else itemListener.onProductUncheck(position);
			}
		});

		view.invalidate();
		notifyDataSetChanged();
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
}
