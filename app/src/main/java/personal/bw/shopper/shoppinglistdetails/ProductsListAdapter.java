package personal.bw.shopper.shoppinglistdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import personal.bw.shopper.R;
import personal.bw.shopper.data.models.Product;

import java.util.List;

public class ProductsListAdapter extends BaseAdapter {
    private List<Product> products;
    private ProductListener itemListener;
    private Context context;

    public ProductsListAdapter(List<Product> products, ProductListener itemListener, Context context) {
        this.products = products;
        this.itemListener = itemListener;
        this.context = context;
    }

    public void replaceData(List<Product> products) {
        this.setList(products);
        this.notifyDataSetChanged();
    }

    private void setList(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.product_list_item, null);
        }

        final Product product = getItem(position);

        if (product != null) {
            TextView productName = (TextView) view.findViewById(R.id.label_product_name);
            TextView productBrand = (TextView) view.findViewById(R.id.label_product_brand);
            TextView productAmount = (TextView) view.findViewById(R.id.label_product_amount);

            if (productName != null) {
                productName.setText(product.getName());
            }

            if (productBrand != null) {
                productBrand.setText(product.getBrand());
            }

            if (productAmount != null) {
                productBrand.setText(product.getAmount());
            }
        }
        View productViewText = view.findViewById(R.id.products_list_item);
        View productDeleteIcon = view.findViewById(R.id.product_delete_icon);
        CheckBox productCheckBox = (CheckBox) view.findViewById(R.id.checkbox_products_list);
        productCheckBox.setChecked(product.getChecked());
        productViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onProductClick(position);
            }
        });

        productDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onDeleteIconClick(position);
            }
        });

        productCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) itemListener.onProductCheck(position);
                else itemListener.onProductUncheck(position);
            }
        });

        return view;
    }

    public interface ProductListener {
        void onProductClick(int clickedProduct);

        void onDeleteIconClick(int clickedProduct);

        void onProductCheck(int clickedProduct);

        void onProductUncheck(int clickedProduct);
    }
}
