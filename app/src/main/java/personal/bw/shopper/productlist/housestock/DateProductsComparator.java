package personal.bw.shopper.productlist.housestock;

import personal.bw.shopper.data.models.Product;

import java.util.Comparator;

public class DateProductsComparator implements Comparator<Product>
{
	@Override
	public int compare(Product o1, Product o2)
	{
		return o1.getBestBefore().compareTo(o2.getBestBefore());
	}
}
