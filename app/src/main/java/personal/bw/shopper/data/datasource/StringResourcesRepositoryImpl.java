package personal.bw.shopper.data.datasource;

import android.content.Context;

public class StringResourcesRepositoryImpl implements StringResourcesRepository
{
	private Context context;

	public StringResourcesRepositoryImpl(Context context)
	{
		this.context = context;
	}

	@Override
	public String getString(int id)
	{
		return context.getString(id);
	}
}
