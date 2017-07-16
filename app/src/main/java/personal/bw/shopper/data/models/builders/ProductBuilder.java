package personal.bw.shopper.data.models.builders;

import personal.bw.shopper.data.models.Product;

import java.text.ParseException;
import java.util.Date;

public class ProductBuilder
{
	private long id = -1;
	private String name = "";
	private String description = "";
	private String brand = "";
	private String amount = "";
	private Date bestBefore = new Date();
	private String barCode = "";
	private boolean isChecked;

	public ProductBuilder(String name)
	{
		this.name = name;
	}

	public ProductBuilder withId(int id){
		this.id = id;
		return this;
	}
	public ProductBuilder withDescription(String description)
	{
		this.description = description;
		return this;
	}

	public ProductBuilder withAmount(String amount)
	{
		this.amount = amount;
		return this;
	}

	public ProductBuilder withBrand(String brand)
	{
		this.brand = brand;
		return this;
	}

	public ProductBuilder withBestBefore(Date date)
	{
		this.bestBefore = date;
		return this;
	}

	public ProductBuilder withBestBefore(String dateString) throws ParseException
	{
		Date date = Product.DATE_FORMAT.parse(dateString);
		this.bestBefore = date;
		return this;
	}

	public ProductBuilder withBarCode(String barCode)
	{
		this.barCode = barCode;
		return this;
	}

	public ProductBuilder withChecked(boolean checked)
	{
		this.isChecked = checked;
		return this;
	}

	public Product build()
	{
		Product product = new Product(this.name);
		product.setId(this.id);
		product.setDescription(this.description);
		product.setAmount(this.amount);
		product.setBrand(this.brand);
		product.setBarCode(this.barCode);
		product.setBestBefore(this.bestBefore);
		product.setChecked(isChecked);
		return product;
	}
}

