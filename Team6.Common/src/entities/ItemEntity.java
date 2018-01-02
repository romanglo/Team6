
package entities;

import java.io.Serializable;

import javafx.scene.image.Image;

/**
 *
 * ItemEntity: Describes an entity of item.
 * 
 */
public class ItemEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -1575231046663959076L;

	private Integer m_id;

	private String m_name;

	private ProductType m_itemType;

	private Double m_itemPrice;

	private Image m_itemImage;

	private String m_domainColor;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the item.
	 */
	public Integer getId()
	{
		return m_id;
	}

	/**
	 * @return The name of the item.
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * @return A enumerator which describes the item type.
	 */
	public ProductType getItemType()
	{
		return m_itemType;
	}

	/**
	 * @return A number which describes the item price.
	 */
	public Double getItemPrice()
	{
		return m_itemPrice;
	}

	/**
	 * @return An image of the item.
	 */
	public Image getItemImage()
	{
		return m_itemImage;
	}

	/**
	 * @return The domain color of the item.
	 */
	public String getColor()
	{
		return m_domainColor;
	}

	// end region -> Getters

	// region Setters

	/**
	 * Upgrade the ID of the item.
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public void setId(Integer m_id)
	{
		this.m_id = m_id;
	}

	/**
	 * Upgrade the name of the item.
	 *
	 * @param m_name
	 *            the m_name to set
	 */
	public void setName(String m_name)
	{
		this.m_name = m_name;
	}

	/**
	 * Upgrade the {@link ProductType} of the item.
	 *
	 * @param m_itemType
	 *            the m_itemType to set
	 */
	public void setItemType(ProductType m_itemType)
	{
		this.m_itemType = m_itemType;
	}

	/**
	 * Upgrade the price of the item.
	 *
	 * @param m_itemPrice
	 *            the m_itemPrice to set
	 */
	public void setItemPrice(Double m_itemPrice)
	{
		this.m_itemPrice = m_itemPrice;
	}

	/**
	 * Upgrade the {@link Image} of the item.
	 *
	 * @param m_itemImage
	 *            the m_itemImage to set
	 */
	public void setItemImage(Image m_itemImage)
	{
		this.m_itemImage = m_itemImage;
	}

	/**
	 * Upgrade the domain color of the item.
	 *
	 * @param m_domainColor
	 *            the m_domainColor to set
	 */
	public void setColor(String m_domainColor)
	{
		this.m_domainColor = m_domainColor;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link ItemEntity}. Dedicated for add or update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param name
	 *            The name of the item.
	 * @param itemType
	 *            A enumerator which describes the item type.
	 * @param itemPrice
	 *            A number which describes the item price.
	 * @param color
	 *            The domain color of the item.
	 * @param itemImg
	 *            The image of the item.
	 */
	public ItemEntity(Integer id, String name, ProductType itemType, Double itemPrice, String color, Image itemImg)
	{
		m_id = id;
		m_name = name;
		m_itemType = itemType;
		m_itemPrice = itemPrice;
		m_domainColor = color;
		m_itemImage = itemImg;
	}

	/**
	 * Create instance of {@link ItemEntity}. Dedicated for add or update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param name
	 *            The name of the item.
	 * @param itemType
	 *            A enumerator which describes the item type.
	 * @param itemPrice
	 *            A number which describes the item price.
	 * @param itemImg
	 *            The image of the item.
	 */
	public ItemEntity(Integer id, String name, ProductType itemType, Double itemPrice, Image itemImg)
	{
		this(id, name, itemType, itemPrice, null, itemImg);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> name. Dedicated
	 * for update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param itemType
	 *            A enumerator which describes the item type.
	 */
	public ItemEntity(Integer id, ProductType itemType)
	{
		this(id, null, itemType, null, null, null);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> name. Dedicated
	 * for update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param itemType
	 *            A enumerator which describes the item type.
	 * @param color
	 *            The domain color of the item.
	 */
	public ItemEntity(Integer id, ProductType itemType, String color)
	{
		this(id, null, itemType, null, color, null);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> item type.
	 * Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param name
	 *            The name of the item.
	 */
	public ItemEntity(Integer id, String name)
	{
		this(id, name, null, null, null, null);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> item type.
	 * Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param price
	 *            The price of the item.
	 */
	public ItemEntity(Integer id, Double price)
	{
		this(id, null, null, price, null, null);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> item type.
	 * Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 * @param image
	 *            The image of the item.
	 */
	public ItemEntity(Integer id, Image image)
	{
		this(id, null, null, null, null, image);
	}

	/**
	 * Create instance of {@link ItemEntity} only with id. Dedicated for get or
	 * remove messages.
	 * 
	 * @param id
	 *            An unique ID of the item.
	 */
	public ItemEntity(Integer id)
	{
		this(id, null, null, null, null, null);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> type. Dedicated
	 * for update messages.
	 *
	 * @param id
	 *            An unique ID of the item.
	 * @param name
	 *            The name of the item.
	 * @param itemImg
	 *            The image of the item.
	 */
	public ItemEntity(Integer id, String name, Image itemImg)
	{
		this(id, name, null, null, null, itemImg);
	}

	/**
	 * Create instance of {@link ItemEntity}, with <code>null</code> type. Dedicated
	 * for update messages.
	 *
	 * @param id
	 *            An unique ID of the item.
	 * @param type
	 *            The type of the item.
	 * @param itemImg
	 *            The image of the item.
	 */
	public ItemEntity(Integer id, ProductType type, Image itemImg)
	{
		this(id, null, type, null, null, itemImg);
	}

	// end region -> Constructors

	// region Override Object Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_id;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ItemEntity other = (ItemEntity) obj;
		if (m_id != other.m_id) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ItemEntity [ID=" + m_id + ", Name=" + m_name + ", Item Type=" + m_itemType + ", Item Price="
				+ m_itemPrice + ", Domain Color=" + m_domainColor + ", Exist Image=" + (m_itemImage != null) + "]";
	}

	// end region -> Override Object Methods
}
