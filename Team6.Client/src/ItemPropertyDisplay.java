
/**
 *
 * ItemPropertyDisplay:
 * TODO Shimon456: Auto-generated type stub - Change with type description
 * 
 */
public class ItemPropertyDisplay
{

	private String m_productProperty;
	private String m_value;
	
	
	/**
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 * @param m_productProperty -
	 * @param m_value -
	 */
	public ItemPropertyDisplay(String m_productProperty, String m_value)
	{
		super();
		this.m_productProperty = m_productProperty;
		this.m_value = m_value;
	}

	/**
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 * @return String
	 */
	public String getProduct_property()
	{
		return m_productProperty;
	}
	
	/**
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 * @param product_property -
	 */
	public void setProduct_property(String product_property)
	{
		this.m_productProperty = product_property;
	}
	
	/**
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 * @return -
	 */
	public String getValue()
	{
		return m_value;
	}
	
	/**
	 * TODO Shimon456: Auto-generated comment stub - Change it!
	 *
	 * @param value -
	 */
	public void setValue(String value)
	{
		this.m_value = value;
	}

	@Override
	public String toString()
	{
		return "ItemPropertyDisplay [m_productProperty=" + m_productProperty + ", m_value=" + m_value + "]";
	}
	
	
}
