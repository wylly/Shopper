package personal.bw.shopper.productdetails;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.EditText;
import personal.bw.shopper.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProductDetailsFragment extends Fragment implements ProductDetailsContract.View {
    private ProductDetailsContract.Presenter presenter;
    private View root;

    public ProductDetailsFragment() {
    }

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.product_details, container, false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(@NonNull ProductDetailsContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_product: {
                presenter.saveProduct(
                        getValue(Inputs.NAME),
                        getValue(Inputs.BRAND),
                        getValue(Inputs.DESCRIPTION),
                        getValue(Inputs.AMOUNT)
                );
                break;
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.product_details_fragment_menu, menu);
    }

    private String getValue(Inputs input) {
        return ((EditText) root.findViewById(input.getId())).getText().toString();
    }

    @Override
    public void showProductSavedMessage() {
        showMessageShort("Product saved!");
    }

    @Override
    public void showProductSaveErrorMessage(String error) {
        showMessage("Product save error: " + error);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showMessageShort(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private enum Inputs {
        NAME {
            int getId() {
                return R.id.input_product_name;
            }
        },

        BRAND {
            int getId() {
                return R.id.input_product_brand;
            }
        },

        DESCRIPTION {
            int getId() {
                return R.id.input_product_description;
            }
        },

        AMOUNT {
            int getId() {
                return R.id.input_product_amount;
            }
        };

        abstract int getId();
    }
}
