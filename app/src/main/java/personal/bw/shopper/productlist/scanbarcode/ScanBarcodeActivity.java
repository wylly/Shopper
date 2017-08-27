package personal.bw.shopper.productlist.scanbarcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import personal.bw.shopper.ActivitiesEnum;
import personal.bw.shopper.BaseActivity;
import personal.bw.shopper.data.datasource.DataSourceDealer;
import personal.bw.shopper.data.datasource.StringResourcesRepositoryImpl;

import static personal.bw.shopper.ActivitiesEnum.BARCODE;

public class ScanBarcodeActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new ScanBarcodePresenter(
				(ScanBarcodeContract.View) setupViewFragment(),
				DataSourceDealer.getINSTANCE(getApplicationContext()),
				new StringResourcesRepositoryImpl(getBaseContext()),
				getIntent());
	}

	@Override
	public Fragment createFragment()
	{
		return new ScanBarcodeFragment();
	}

	@Override
	public ActivitiesEnum getCurrentActivity()
	{
		return BARCODE;
	}
}
