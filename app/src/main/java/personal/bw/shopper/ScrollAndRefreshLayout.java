package personal.bw.shopper;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

public class ScrollAndRefreshLayout extends SwipeRefreshLayout
{

	private View scrollUpChild;

	public ScrollAndRefreshLayout(Context context)
	{
		super(context);
	}

	public ScrollAndRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean canChildScrollUp()
	{
		if (scrollUpChild != null)
		{
			return ViewCompat.canScrollVertically(scrollUpChild, -1);
		}
		return super.canChildScrollUp();
	}

	public void setScrollUpChild(View view)
	{
		scrollUpChild = view;
	}
}
