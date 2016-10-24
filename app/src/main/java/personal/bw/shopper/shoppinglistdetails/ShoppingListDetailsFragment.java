package personal.bw.shopper.shoppinglistdetails;

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
import personal.bw.shopper.data.models.Product;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShoppingListDetailsFragment extends Fragment implements ShoppingListDetailsContract.View {

    public static final String CLICKED_PRODUCT = "CLICKED PRODUCT";
    private ShoppingListDetailsContract.Presenter presenter;
    private ProductsListAdapter listAdapter;
    private LinearLayout productsView;
    private View noProductsView;
    private ImageView noProductsIcon;
    private TextView noProductsMainView;
    private TextView noProductsAddView;

    public ShoppingListDetailsFragment() {
    }

    public static ShoppingListDetailsFragment newInstance() {
        return new ShoppingListDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull ShoppingListDetailsContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.shopping_list_details, container, false);

        ListView listView = (ListView) root.findViewById(R.id.productsList);
        listView.setAdapter(listAdapter);
        productsView = (LinearLayout) root.findViewById(R.id.productsLL);
        ((EditText) root.findViewById(R.id.input_shopping_list_name)).setText(presenter.getShoppingListName());
        setUpNoProductsViews(root);

        final ScrollAndRefreshLayout scrollAndRefreshLayout = setupProgressIndicator(root);

        scrollAndRefreshLayout.setScrollUpChild(listView);
        scrollAndRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadProducts();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private ScrollAndRefreshLayout setupProgressIndicator(View root) {
        final ScrollAndRefreshLayout scrollAndRefreshLayout =
                (ScrollAndRefreshLayout) root.findViewById(R.id.products_refresh_layout);
        scrollAndRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        return scrollAndRefreshLayout;
    }

    private void setUpNoProductsViews(View root) {
        noProductsView = root.findViewById(R.id.noProducts);
        noProductsIcon = (ImageView) root.findViewById(R.id.noProductsIcon);
        noProductsMainView = (TextView) root.findViewById(R.id.noProductsMain);
        noProductsAddView = (TextView) root.findViewById(R.id.noProductsAdd);
        noProductsAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProduct();
            }
        });
    }

    public void showAddProduct() {
        //TODO go to add new shopping list activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_product: {
                startAddProductActivity();
                break;
            }
            case R.id.save_shopping_list: {
                presenter.saveShoppingList();
                break;
            }
        }
        return true;
    }

    private void startAddProductActivity() {
        Intent intent = new Intent(getContext(), ShoppingListDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_action), Action.EDIT_PRODUCT);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.products_fragment_menu, menu);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.products_refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showProducts(List<Product> shoppingLists) {
        listAdapter.replaceData(shoppingLists);
        productsView.setVisibility(View.VISIBLE);
        noProductsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoProducts() {
        productsView.setVisibility(View.GONE);
        noProductsView.setVisibility(View.VISIBLE);
        noProductsAddView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_shoppinglist_message));
    }

    @Override
    public void showProductDeletedToast() {
        Toast.makeText(getContext(), R.string.shopping_list_deletion_successfull, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProductDeletionError(String info) {
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProductDetailsUi(Product clickedProduct) {
        Intent intent = new Intent(getContext(), ShoppingListDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_action), Action.EDIT_PRODUCT);
        intent.putExtra(CLICKED_PRODUCT, clickedProduct);
        startActivity(intent);
    }

    @Override
    public void showLoadingProductsError() {
        showMessage(getString(R.string.loading_shoppinglists_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showMessageShort(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    ProductsListAdapter.ProductListener mItemListener = new ProductsListAdapter.ProductListener() {
        @Override
        public void onProductClick(Product clickedProduct) {
            showProductDetailsUi(clickedProduct);
        }

        @Override
        public void onDeleteIconClick(final Product clickedProduct) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Deleting product")
                    .setMessage("This will completely remove product from this Shopper application")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.deleteProduct(clickedProduct);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Nothig happens
                }
            }).create()
                    .show();
        }

        @Override
        public void onProductCheck(Product clickedProduct) {
            presenter.markProductChecked(clickedProduct);
        }

        @Override
        public void onProductUncheck(Product product) {
            presenter.markProductUnchecked(product);
        }
    };

    @Override
    public void showProductChecked() {
        showMessageShort("Product checked");
    }

    @Override
    public void showProductCheckFailed(String error) {
        showMessage("Product check failed because of: " + error);
    }

    @Override
    public void showProductUnchecked() {
        showMessageShort("Product unchecked");
    }

    @Override
    public void showProductUncheckFailed(String error) {
        showMessage("Product uncheck failed because of: " + error);
    }

    @Override
    public void showShoppingListSavedMessage() {
        showMessageShort("Shopping list saved.");
    }

    @Override
    public void showUnableToSaveShoppingList() {
        showMessage("Unable to save shopping list.");
    }

    public enum Action{
        NEW_PRODUCT,
        EDIT_PRODUCT
    }
}
