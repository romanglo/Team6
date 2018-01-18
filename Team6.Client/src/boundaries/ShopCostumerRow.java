package boundaries;

import java.util.Date;

public class ShopCostumerRow
{		
	// region Fields
	private Integer m_shopCostumerID;
	
	private String m_shopCostumerSubscription;
	
	private Date m_shopCostumerStartDate;
	
	private String m_shopCostumerCreditCard;
	
	public Float m_shopCostumerCumulativePrice;
	// end region -> Fields

	
	

	// region Getters
	public Integer getShopCostumerID()
	{
		return m_shopCostumerID;
	}

	
	public String getShopCostumerSubscription()
	{
		return m_shopCostumerSubscription;
	}

	
	public Date getShopCostumerStartDate()
	{
		return m_shopCostumerStartDate;
	}

	
	public String getShopCostumerCreditCard()
	{
		return m_shopCostumerCreditCard;
	}

	
	public Float getShopCostumerCumulativePrice()
	{
		return m_shopCostumerCumulativePrice;
	}
	// end region -> Getters

	// region Setters
	
	public void setShopCostumerID(Integer shopCostumerID)
	{
		m_shopCostumerID = shopCostumerID;
	}


	
	public void setShopCostumerSubscription(String shopCostumerSubscription)
	{
		m_shopCostumerSubscription = shopCostumerSubscription;
	}


	
	public void setShopCostumerStartDate(Date shopCostumerStartDate)
	{
		m_shopCostumerStartDate = shopCostumerStartDate;
	}


	
	public void setShopCostumerCreditCard(String shopCostumerCreditCard)
	{
		m_shopCostumerCreditCard = shopCostumerCreditCard;
	}


	
	public void setShopCostumerCumulativePrice(Float shopCostumerCumulativePrice)
	{
		m_shopCostumerCumulativePrice = shopCostumerCumulativePrice;
	}
	// end region -> Setters

	// region Constructors
	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Date shopCostumerStartDate,
			String shopCostumerCreditCard, Float shopCostumerCumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_shopCostumerStartDate = shopCostumerStartDate;
		m_shopCostumerCreditCard = shopCostumerCreditCard;
		m_shopCostumerCumulativePrice = shopCostumerCumulativePrice;
	}


	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Float shopCostumerCumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_shopCostumerStartDate = new Date();
		m_shopCostumerCreditCard = "None";
		m_shopCostumerCumulativePrice = shopCostumerCumulativePrice;
	}


	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, Date shopCostumerStartDate,
			Float shopCostumerCumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_shopCostumerStartDate = shopCostumerStartDate;
		m_shopCostumerCreditCard = "None";
		m_shopCostumerCumulativePrice = shopCostumerCumulativePrice;
	}


	public ShopCostumerRow(Integer shopCostumerID, String shopCostumerSubscription, String shopCostumerCreditCard,
			Float shopCostumerCumulativePrice)
	{
		super();
		m_shopCostumerID = shopCostumerID;
		m_shopCostumerSubscription = shopCostumerSubscription;
		m_shopCostumerStartDate = new Date();
		m_shopCostumerCreditCard = shopCostumerCreditCard;
		m_shopCostumerCumulativePrice = shopCostumerCumulativePrice;
	}


	
	
	
	// end region -> Constructors

	// region Public Methods
	
	
	
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
