
package entities;

import java.io.Serializable;

import messages.IMessageData;
import messages.Message;

/**
 *
 * LoginData: Describes the required data to login message, the type used by
 * {@link Message}.
 * 
 */
public class ShopCatalogData implements IMessageData
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 5653924532728439335L;

	// region Fields

	private int m_shopManagerId;

	// end region -> Fields

	// region Getters

	/**
	 * @return the id of the requested shop catalog.
	 */
	public int getShopManagerId()
	{
		return m_shopManagerId;
	}

	// end region -> Getters

	// region Constructors

	/**
	 * 
	 * Create instance of request shop catalog that suitable to {@link Message}.
	 * 
	 * @param shopManagerId
	 *            the id of the requested shop catalog.
	 *
	 */
	public ShopCatalogData(int shopManagerId)
	{
		m_shopManagerId = shopManagerId;
	}

	// end region -> Constructors

	// region Object Methods Override

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopCatalogData [shopManagerId=" + m_shopManagerId + "]";
	}

	// end region ->
}
