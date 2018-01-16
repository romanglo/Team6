
package newEntities;

import java.io.Serializable;

/**
 *
 * ItemInShop: A POJO to database 'items_in_shops' table.
 * 
 */
public class ItemInShop implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -716921238153105773L;

	private int m_shopManagerId;

	private int m_itemId;

	private float m_discountedPrice;

	/**
	 * @return the shopManagerId
	 */
	public int getShopManagerId()
	{
		return m_shopManagerId;
	}

	/**
	 * @param shopManagerId
	 *            the shopManagerId to set
	 */
	public void setShopManagerId(int shopManagerId)
	{
		m_shopManagerId = shopManagerId;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId()
	{
		return m_itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(int itemId)
	{
		m_itemId = itemId;
	}

	/**
	 * @return the discountedPrice
	 */
	public float getDiscountedPrice()
	{
		return m_discountedPrice;
	}

	/**
	 * @param discountedPrice
	 *            the discountedPrice to set
	 */
	public void setDiscountedPrice(float discountedPrice)
	{
		m_discountedPrice = discountedPrice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ItemInShop [shopManagerId=" + m_shopManagerId + ", itemId=" + m_itemId + ", discountedPrice="
				+ m_discountedPrice + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_itemId;
		result = prime * result + m_shopManagerId;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ItemInShop)) {
			return false;
		}
		ItemInShop other = (ItemInShop) obj;
		if (m_itemId != other.m_itemId) {
			return false;
		}
		if (m_shopManagerId != other.m_shopManagerId) {
			return false;
		}
		return true;
	}

}
