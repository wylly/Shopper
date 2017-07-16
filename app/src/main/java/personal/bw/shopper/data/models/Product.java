package personal.bw.shopper.data.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import static java.text.DateFormat.MEDIUM;

@DatabaseTable(tableName = "product")
public class Product implements Serializable
{
	public final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(MEDIUM);
	public final static String ID_FIELD_NAME = "id";

	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Long id;

	@DatabaseField
	private String name;

	@DatabaseField
	private String description;

	@DatabaseField
	private String brand;

	@DatabaseField
	private String amount;

	@DatabaseField
	private Date bestBefore;

	@DatabaseField
	private String barCode;

	@DatabaseField
	private Boolean checked;

	Product()
	{
	}


	public Product(String name)
	{
		this.name = name;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getBrand()
	{
		return brand;
	}

	public void setBrand(String brand)
	{
		this.brand = brand;
	}

	public String getAmount()
	{
		return amount;
	}

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	public Date getBestBefore()
	{
		return bestBefore;
	}

	public void setBestBefore(Date bestBefore)
	{
		this.bestBefore = bestBefore;
	}

	public String getBarCode()
	{
		return barCode;
	}

	public void setBarCode(String barCode)
	{
		this.barCode = barCode;
	}

	public String getFormattedDate()
	{
		return DATE_FORMAT.format(bestBefore);
	}

	public Boolean getChecked()
	{
		return checked != null ? checked : false;
	}

	public void setChecked(Boolean checked)
	{
		this.checked = checked;
	}

	public void copyFrom(Product product)
	{
		this.name = product.name;
		this.description = product.description;
		this.brand = product.brand;
		this.amount = product.amount;
		this.bestBefore = product.bestBefore;
		this.barCode = product.barCode;
		this.checked = product.checked;
	}

	public static Product getCopyOf(Product product)
	{
		Product p = new Product();
		p.id = product.id;
		p.name = product.name;
		p.description = product.description;
		p.brand = product.brand;
		p.amount = product.amount;
		p.bestBefore = product.bestBefore;
		p.barCode = product.barCode;
		p.checked = product.checked;
		return p;
	}

	@Override
	public String toString()
	{
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", brand='" + brand + '\'' +
				", amount='" + amount + '\'' +
				", bestBefore=" + bestBefore +
				", barCode='" + barCode + '\'' +
				", checked=" + checked +
				'}';
	}
}
