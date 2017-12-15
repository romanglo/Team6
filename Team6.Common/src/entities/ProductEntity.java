
package entities;

import java.io.Serializable;

/**
 *
 * ProductEntity: Describes an entity of product.
 * 
 */
public class ProductEntity implements IEntity
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements {@link Serializable}
	 */
	private static final long serialVersionUID = -1575231046663959076L;

	private int m_id;

	private String m_name;

	private ProductType m_productType;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the product.
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @return The name of the product.
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * @return A enumerator which describes the product type.
	 */
	public ProductType getProductType()
	{
		return m_productType;
	}

	// end region -> Getters

	// region Setters

	/**
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public void setId(int m_id)
	{
		this.m_id = m_id;
	}

	/**
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @param m_name
	 *            the m_name to set
	 */
	public void setName(String m_name)
	{
		this.m_name = m_name;
	}

	/**
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @param m_productType
	 *            the m_productType to set
	 */
	public void setProductType(ProductType m_productType)
	{
		this.m_productType = m_productType;
	}

	// end region -> Setters

	// region Constructors

	/**
	 * Create instance of {@link ProductType}. Dedicated for add or update messages.
	 * 
	 * @param id
	 *            An unique ID of the product.
	 * @param name
	 *            The name of the product.
	 * @param productType
	 *            A enumerator which describes the product type.
	 */
	public ProductEntity(int id, String name, ProductType productType)
	{
		m_id = id;
		m_name = name;
		m_productType = productType;
	}

	/**
	 * Create instance of {@link ProductType}, with <code>null</code> name.
	 * Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the product.
	 * @param productType
	 *            A enumerator which describes the product type.
	 */
	public ProductEntity(int id, ProductType productType)
	{
		this(id, null, productType);
	}

	/**
	 * Create instance of {@link ProductType}, with <code>null</code> product type.
	 * Dedicated for update messages.
	 * 
	 * @param id
	 *            An unique ID of the product.
	 * @param name
	 *            The name of the product.
	 */
	public ProductEntity(int id, String name)
	{
		this(id, name, null);
	}

	/**
	 * Create instance of {@link ProductType} only with id. Dedicated for get or
	 * remove messages.
	 * 
	 * @param id
	 *            An unique ID of the product.
	 */
	public ProductEntity(int id)
	{
		this(id, null, null);
	}
	// end region -> Constructors

	// region Override Object Methods

	/*
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

	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ProductEntity other = (ProductEntity) obj;
		if (m_id != other.m_id) return false;
		return true;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ProductEntity [ID=" + m_id + ", Name=" + m_name + ", Product Type=" + m_productType + "]";
	}

	// end region -> Override Object Methods
}
