package personal.bw.shopper.productdetails;


import android.app.Activity;
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
    private EditText name;
    private EditText description;
    private EditText brand;
    private EditText amount;
    private View rootView;

    public ProductDetailsFragment() {
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = (EditText) this.getView().findViewById(R.id.input_product_name);
        description = (EditText) this.getView().findViewById(R.id.input_product_brand);
        brand = (EditText) this.getView().findViewById(R.id.input_product_description);
        amount = (EditText) this.getView().findViewById(R.id.input_product_amount);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_details, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return rootView;
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
                        name.getText().toString(),
                        description.getText().toString(),
                        brand.getText().toString(),
                        amount.getText().toString()
                );
                break;
            }
        }
        return true;
    }

    @Override
    public void goToProductsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.product_details_fragment_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
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

    @Override
    public void setName(String name) {
        this.name.setText(name);
    }

    @Override
    public void setDescription(String description) {
        this.description.setText(description);
    }

    @Override
    public void setBrand(String brand) {
        this.brand.setText(brand);
    }

    @Override
    public void setAmount(String amount) {
        this.amount.setText(amount);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
