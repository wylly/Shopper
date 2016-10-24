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

import static com.google.common.base.Preconditions.checkNotNull;

public class ProductsListAdapter extends BaseAdapter {
    private List<Product> products;
    private ProductListener itemListener;
    private Context context;

    public ProductsListAdapter(List<Product> products, ProductListener itemListener, Context context) {
        setList(products);
        this.itemListener = itemListener;
        this.context = context;
    }

    public void replaceData(List<Product> products) {
        setList(products);
        notifyDataSetChanged();
    }

    private void setList(List<Product> products) {
        this.products = checkNotNull(products);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.product_list_item, null);
        }

        final Product product = getItem(position);

        if (product != null) {
            TextView productName = (TextView)view.findViewById(R.id.label_product_name);
            TextView productBrand = (TextView)view.findViewById(R.id.label_product_brand);
            TextView productAmount = (TextView)view.findViewById(R.id.label_product_amount);

            if (productName != null) {
                productName.setText(product.getName());
            }

            if (productBrand!= null) {
                productBrand.setText(product.getBrand());
            }

            if (productAmount!= null) {
                productBrand.setText(product.getAmount());
            }
        }
        View productViewText = view.findViewById(R.id.products_list_item);
        View productDeleteIcon = view.findViewById(R.id.product_delete_icon);
        CheckBox productCheckBox = (CheckBox) view.findViewById(R.id.checkbox_products_list);
        productViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onProductClick(product);
            }
        });

        productDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onDeleteIconClick(product);
            }
        });

        productCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) itemListener.onProductCheck(product);
                else itemListener.onProductUncheck(product);
            }
        });

        return view;
    }
    public interface ProductListener {
        void onProductClick(Product clickedProduct);
        void onDeleteIconClick(Product clickedProduct);
        void onProductCheck(Product clickedProduct);
        void onProductUncheck(Product product);
    }
}
