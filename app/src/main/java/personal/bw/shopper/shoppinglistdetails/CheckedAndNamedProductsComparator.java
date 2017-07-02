package personal.bw.shopper.shoppinglistdetails;

import personal.bw.shopper.data.models.Product;

import java.util.Comparator;

public class CheckedAndNamedProductsComparator implements Comparator<Product>
{
	@Override
	public int compare(Product o1, Product o2)
	{
		if (o1.getChecked())
		{
			if (o2.getChecked())
			{
				return o1.getName().compareTo(o2.getName());
			}
			else
			{
				return 1;
			}
		}
		else if (o2.getChecked())
		{
			return -1;
		}
		else
		{
			return o1.getName().compareTo(o2.getName());
		}
	}
}
