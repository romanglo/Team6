
package boundaries;

import java.util.Date;

/**
 *
 * ShopCostumerRow: This class is for shop costumer subscription table. For Shop
 * Manager Use Only!
 * 
 */
public class ShopCostumerRow
{

	// region Fields
	private Integer m_shopCostumerID;

	private String m_shopCostumerSubscription;

	private Date m_subscriptionStartDate;

	private String m_creditCard;

	private Float m_cumulativePrice;
	// end region -> Fields

	// region Getters

	/**
	 * Get shop costumer ID
	 *
	 * @return shop costumer ID
	 */
	public Integer getShopCostumerID()
	{
		return m_shopCostumerID;
	}

	/**
	 * Get shop costumer Shop Costumer Subscription
	 *
	 * @return shop costumer Shop Costumer Subscription
	 */
	public String getShopCostumerSubscription()
	{
		return m_shopCostumerSubscription;
	}

	/**
	 * Get shop costumer Subscription Start Date
	 *
	 * @return shop costumer Subscription Start Date
	 */
	public Date getSubscriptionStartDate()
	{
		return m_subscriptionStartDate;
	}

	/**
	 * Get shop costumer Credit Card
	 *
	 * @return shop costumer Credit Card
	 */
	public String getCreditCard()
	{
		return m_creditCard;
	}

	/**
	 * Get shop costumer Cumulative Price
	 *
	 * @return shop costumer Cumulative Price
	 */
	public Float getCumulativePrice()
	{
		return m_cumulativePrice;
	}
	// end region -> Getters

	// region Setters
	/**
	 * Set shop costumer Shop Costumer Subscription
	 *
	 * @param shopCostumerSubscription
	 *            The new Shop Costumer Subscription
	 */
	public void setShopCostumerSubscription(String shopCostumerSubscription)
	{
		m_shopCostumerSubscription = shopCostumerSubscription;
	}

	/**
	 * Set shop costumer Subscription Start Date
	 *
	 * @param subscriptionStartDate
	 *            The new Start Date
	 */
	public void setSubscriptionStartDate(Date subscriptionStartDate)
	{
		m_subscriptionStartDate = subscriptionStartDate;
	}

	/**
	 * Set shop costumer Credit Card
	 *
	 * @param creditCard
	 *            The new Credit Card
	 */
	public void setCreditCard(String creditCard)
	{
		m_creditCard = creditCard;
	}

	/**
	 * Set shop costumer Cumulative Price
	 *
	 * @param cumulativePrice
	 *            The new Cumulative Price
	 */
	public void setCumulativePrice(float cumulativePrice)
	{
		m_cumulativePrice = cumulativePrice;
	}
	// end region -> Setters

	// region Constructors
	/**
	 * Full constructor.
	 *
	 * @param shopCostumerID
	 *            Shop Costumer Id
	 * @param shopCostumerSubscription
	 *            Shop Costumer Subscription
	 * @param subscriptionStartDate
	 *            Subscription Start Date
	 * @param creditCard
	 *            Shop Costumer Credit Card
	 * @param cumulativePrice
	 *            Shop Costumer Cumulative Price
	 */
	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Date subscriptionStartDate,
			String creditCard, float cumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_subscriptionStartDate = subscriptionStartDate;
		m_creditCard = creditCard;
		m_cumulativePrice = cumulativePrice;
	}

	/**
	 * Constructor for shop costumer without credit card and subscription.
	 *
	 * @param shopCostumerID
	 *            Shop Costumer Id
	 * @param shopCostumerSubscription
	 *            Shop Costumer Subscription
	 * @param cumulativePrice
	 *            Shop Costumer Cumulative Price
	 */
	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Float cumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_subscriptionStartDate = new Date();
		m_creditCard = "None";
		m_cumulativePrice = cumulativePrice;
	}

	/**
	 * Constructor for shop costumer without subscription.
	 *
	 * @param shopCostumerID
	 *            Shop Costumer Id
	 * @param shopCostumerSubscription
	 *            Shop Costumer Subscription
	 * @param creditCard
	 *            Shop Costumer Credit Card
	 * @param cumulativePrice
	 *            Shop Costumer Cumulative Price
	 *
	 */
	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, String creditCard,
			Float cumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_subscriptionStartDate = new Date();
		m_creditCard = creditCard;
		m_cumulativePrice = cumulativePrice;
	}

	/**
	 * Constructor for shop costumer without credit card.
	 *
	 * @param shopCostumerID
	 *            Shop Costumer Id
	 * @param shopCostumerSubscription
	 *            Shop Costumer Subscription
	 * @param subscriptionStartDate
	 *            Shop Costumer Subscription Start Date
	 * @param cumulativePrice
	 *            Shop Costumer Cumulative Price
	 */
	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Date subscriptionStartDate,
			Float cumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_subscriptionStartDate = subscriptionStartDate;
		m_cumulativePrice = cumulativePrice;
	}
	// end region -> Constructors
}
