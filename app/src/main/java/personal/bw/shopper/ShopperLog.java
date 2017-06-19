package personal.bw.shopper;

import android.util.Log;

public class ShopperLog
{
	private final static String TAG = "SHOPPER: ";

	private ShopperLog()
	{
	}

	public static int i(String msg)
	{
		return Log.i(TAG, msg);
	}

	public static int e(String msg)
	{
		return Log.e(TAG, msg);
	}
}
