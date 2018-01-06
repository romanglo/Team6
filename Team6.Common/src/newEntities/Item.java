
package newEntities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javafx.scene.image.Image;
import utilities.ImageUtilities;

/**
 *
 * Item: A POJO to database 'items' table.
 * 
 */
public class Item implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -7703076691895192808L;

	private int m_id;

	private String m_name;

	private EntitiesEnums.ProductType m_type;

	private float m_price;

	private transient Image m_image;

	private byte[] m_imageInBytes;

	private String m_domainColor;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		m_id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * @return the type
	 */
	public EntitiesEnums.ProductType getType()
	{
		return m_type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(EntitiesEnums.ProductType type)
	{
		m_type = type;
	}

	/**
	 * @return the price
	 */
	public float getPrice()
	{
		return m_price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(float price)
	{
		m_price = price;
	}

	/**
	 * @return the domainColor
	 */
	public String getDomainColor()
	{
		return m_domainColor;
	}

	/**
	 * @param domainColor
	 *            the domainColor to set
	 */
	public void setDomainColor(String domainColor)
	{
		m_domainColor = domainColor;
	}

	/**
	 * @param image
	 *            the new {@link Image} to set.
	 */
	public void setImage(Image image)
	{
		if (image == null) {
			m_image = null;
			m_imageInBytes = null;
		}
		m_image = image;
		m_imageInBytes = ImageUtilities.ImageToByteArray(image, "jpeg", null);
	}

	/**
	 * 
	 * @param inputStream
	 *            the {@link Image} in {@link InputStream} format to set.
	 */
	public void setImage(InputStream inputStream)
	{
		if (inputStream == null) {
			m_image = null;
			m_imageInBytes = null;
		}
		m_image = ImageUtilities.InputStreamToImage(inputStream, null);
		m_imageInBytes = ImageUtilities.ImageToByteArray(m_image, "jpeg", null);
	}

	/**
	 * @param bytes
	 *            the {@link Image} in byte array format to set
	 */
	public void setImage(byte[] bytes)
	{
		if (bytes == null) {
			m_image = null;
			m_imageInBytes = null;
		}
		m_image = ImageUtilities.ByteArrayToImage(bytes, null);
		m_imageInBytes = bytes;
	}

	/**
	 * @return the m_inputStream
	 */
	public InputStream getInputStream()
	{
		if (m_imageInBytes == null) {
			return null;
		}
		return new ByteArrayInputStream(m_imageInBytes);
	}

	/**
	 * @return The contained {@link Image} in the class.
	 */
	public Image getImage()
	{
		if (m_image == null && m_imageInBytes != null) {
			m_image = ImageUtilities.ByteArrayToImage(m_imageInBytes, null);
		}
		return m_image;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Item [id=" + m_id + ", name=" + m_name + ", type=" + m_type + ", price=" + m_price
				+ ", existImage=" + (m_image != null || (m_imageInBytes != null && m_imageInBytes.length != 0))
				+ ", domainColor=" + m_domainColor + "]";
	}

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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Item)) {
			return false;
		}
		Item other = (Item) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
