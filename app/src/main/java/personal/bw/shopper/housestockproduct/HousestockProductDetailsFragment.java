package personal.bw.shopper.housestockproduct;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import personal.bw.shopper.CalendarConverter;
import personal.bw.shopper.DatePickerDialogUpdater;
import personal.bw.shopper.DatePickerFragment;
import personal.bw.shopper.R;

import static android.app.Activity.RESULT_OK;
import static butterknife.ButterKnife.bind;
import static com.google.common.base.Preconditions.checkNotNull;

public class HousestockProductDetailsFragment extends Fragment implements HousestockProductDetailsContract.View, DatePickerDialogUpdater
{
	private HousestockProductDetailsContract.Presenter presenter;

	@BindView(R.id.input_product_name)
	EditText name;

	@BindView(R.id.input_product_brand)
	EditText brand;

	@BindView(R.id.input_product_description)
	EditText description;

	@BindView(R.id.input_product_amount)
	EditText amount;

	@BindView(R.id.input_product_due_date)
	TextView dueDate;

	private View rootView;
	private CalendarConverter calendarConverter = new CalendarConverter();

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.product_details_housestock, container, false);
		bind(this, rootView);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		presenter.start();

		return rootView;
	}

	@OnClick(R.id.input_product_due_date)
	public void showTimePickerDialog(View v)
	{
		DatePickerFragment newFragment = new DatePickerFragment();
		Bundle bundle = new Bundle();
		String date = dueDate.getText().toString();
		bundle.putInt("day", calendarConverter.toDay(date));
		bundle.putInt("month", calendarConverter.toMonth(date));
		bundle.putInt("year", calendarConverter.toYear(date));
		newFragment.setArguments(bundle);
		newFragment.setDatePickerDialogUpdater(this);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	@Override
	public void setPresenter(@NonNull HousestockProductDetailsContract.Presenter presenter)
	{
		this.presenter = checkNotNull(presenter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.save_product:
			{
				presenter.saveProduct(
						name.getText().toString(),
						brand.getText().toString(),
						description.getText().toString(),
						amount.getText().toString(),
						dueDate.getText().toString()
				);
				break;
			}
			case R.id.scan_barcode_product:
			{
				startScanBarCodeActivity();
			}
		}
		return true;
	}

	private void startScanBarCodeActivity()
	{

	}

	@Override
	public void goToProductsList()
	{
		getActivity().setResult(RESULT_OK);
		getActivity().finish();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.product_details_housestock_fragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void showProductSavedMessage()
	{
		showMessageShort("Product saved!");
	}

	@Override
	public void showProductSaveErrorMessage(String error)
	{
		showMessage("Product save error: " + error);
	}

	private void showMessage(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
	}

	private void showMessageShort(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void setName(String name)
	{
		this.name.setText(name);
	}

	@Override
	public void setDescription(String description)
	{
		this.description.setText(description);
	}

	@Override
	public void setBrand(String brand)
	{
		this.brand.setText(brand);
	}

	@Override
	public void setDueDate(String dueDate)
	{
		this.dueDate.setText(dueDate);
	}

	@Override
	public void setAmount(String amount)
	{
		this.amount.setText(amount);
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	@Override
	public void updateDate(int day, int month, int year)
	{
		this.dueDate.setText(calendarConverter.toString(day, month, year));
	}
}
