
package controllers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * CatalogItemRow: Class handles the table row of the catalog item list.
 * 
 */
public class CatalogItemRow
{

	private Integer m_id;

	private String m_name;

	private String m_type;

	private Double m_price;

	private Image m_image;

	/**
	 * Constructor.
	 *
	 * @param m_id
	 *            Item ID.
	 * @param m_name
	 *            Item name.
	 * @param m_type
	 *            Item type.
	 * @param m_price
	 *            Item price.
	 * @param m_image
	 *            Item image.
	 */
	public CatalogItemRow(Integer m_id, String m_name, String m_type, Double m_price, Image m_image)
	{
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_image = ResizeImage(m_image);
		;
	}

	/**
	 * Constructor with default image.
	 *
	 * @param m_id
	 *            Item ID.
	 * @param m_name
	 *            Item name.
	 * @param m_type
	 *            Item type.
	 * @param m_price
	 *            Item price.
	 */
	public CatalogItemRow(Integer m_id, String m_name, String m_type, Double m_price)
	{
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_image = ResizeImage(new Image(getClass().getResourceAsStream("/boundaries/images/Zerli_Headline.jpg")));
	}

	private Image ResizeImage(Image imageToResize)
	{
		BufferedImage imageReader = SwingFXUtils.fromFXImage(imageToResize, null);
		BufferedImage newImage = ImageNewScale(imageReader, 80, 80);
		Image finalImage = SwingFXUtils.toFXImage(newImage, null);
		return finalImage;
	}

	private BufferedImage ImageNewScale(BufferedImage imageToScale, int newScaleWidth, int newScaleHeight)
	{
		BufferedImage scaledImage = null;
		if (imageToScale != null) {
			scaledImage = new BufferedImage(newScaleWidth, newScaleHeight, imageToScale.getType());
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(imageToScale, 0, 0, newScaleWidth, newScaleHeight, null);
			graphics2D.dispose();
		}
		return scaledImage;
	}

	/**
	 * @return Item ID.
	 */
	public Integer getM_id()
	{
		return m_id;
	}

	/**
	 * Getter for table view.
	 * @return Item ID.
	 */
	public String getId()
	{
		return m_id.toString();
	}

	/**
	 * @param m_id
	 *            Item ID.
	 */
	public void setM_id(Integer m_id)
	{
		this.m_id = m_id;
	}

	/**
	 * @return Item name.
	 */
	public String getM_name()
	{
		return m_name;
	}

	/**
	 * Getter for table view.
	 * @return Item name String.
	 */
	public String getName()
	{
		return m_name.toString();
	}

	/**
	 * @param m_name
	 *            Item name.
	 */
	public void setM_name(String m_name)
	{
		this.m_name = m_name;
	}

	/**
	 * @return Item type.
	 */
	public String getM_type()
	{
		return m_type;
	}

	/**
	 * Getter for table view.
	 * @return Item type.
	 */
	public String getType()
	{
		return m_type.toString();
	}

	/**
	 * @param m_type
	 *            Item type.
	 */
	public void setM_type(String m_type)
	{
		this.m_type = m_type;
	}

	/**
	 * @return Item price.
	 */
	public Double getM_price()
	{
		return m_price;
	}

	/**
	 * Getter for table view.
	 * @return Item price string.
	 */
	public String getPrice()
	{
		return m_price.toString();
	}

	/**
	 * @param m_price
	 *            Item price.
	 */
	public void setM_price(Double m_price)
	{
		this.m_price = m_price;
	}

	/**
	 * @return Item image.
	 */
	public Image getM_image()
	{
		return m_image;
	}

	/**
	 * Getter for table view.
	 * @return Item image in imageView.
	 */
	public ImageView getImage()
	{
		return new ImageView(m_image);
	}

	/**
	 * @param m_image
	 *            Item image.
	 */
	public void setM_image(Image m_image)
	{
		this.m_image = ResizeImage(m_image);
	}

	@Override
	public String toString()
	{
		return "CatalogItemRaw [m_id=" + m_id + ", m_name=" + m_name + ", m_type=" + m_type + ", m_price=" + m_price
				+ ", m_image=" + m_image + "]";
	}

}
