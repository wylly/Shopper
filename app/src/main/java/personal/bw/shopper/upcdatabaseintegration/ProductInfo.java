package personal.bw.shopper.upcdatabaseintegration;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "output")
public class ProductInfo
{
	public static final String SEPARATOR = ", ";

	@Element
	private boolean valid;

	@Element(required = false)
	private String number;

	@Element(required = false)
	private String itemname;

	@Element(required = false)
	private String alias;

	@Element(required = false)
	private String description;

	@Element(required = false)
	private String avgprice;

	@Element(required = false)
	private String rate_up;

	@Element(required = false)
	private String rate_down;

	@Element(required = false)
	private String error;

	@Element(required = false)
	private String reason;

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getItemname()
	{
		return itemname;
	}

	public void setItemname(String itemname)
	{
		this.itemname = itemname;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAvgprice()
	{
		return avgprice;
	}

	public void setAvgprice(String avgprice)
	{
		this.avgprice = avgprice;
	}

	public String getRate_up()
	{
		return rate_up;
	}

	public void setRate_up(String rate_up)
	{
		this.rate_up = rate_up;
	}

	public String getRate_down()
	{
		return rate_down;
	}

	public void setRate_down(String rate_down)
	{
		this.rate_down = rate_down;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getFullDescription(){
		return description +
				SEPARATOR +
				avgprice +
				SEPARATOR +
				alias;
	}

	@Override
	public String toString()
	{
		return "Barcode : " + number + "\n" +
				"Name : " + itemname + "\n" +
				"Description : " + description + "\n" +
				"Avg price : " + avgprice + "$";
	}
}
