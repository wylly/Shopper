package personal.bw.shopper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import static android.app.DatePickerDialog.OnDateSetListener;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener
{
	private DatePickerDialogUpdater datePickerDialogUpdater;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Bundle arguments = getArguments();
		int year = arguments.getInt("year");
		int month = arguments.getInt("month");
		int day = arguments.getInt("day");
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void setDatePickerDialogUpdater(DatePickerDialogUpdater datePickerDialogUpdater)
	{
		this.datePickerDialogUpdater = datePickerDialogUpdater;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day)
	{
		datePickerDialogUpdater.updateDate(day, month, year);
	}
}