
package boundaries;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 *
 * CatalogItemRow: Manage catalog table rows.
 *
 */
public class CatalogItemRow
{

	private Integer m_id;

	private String m_name;

	private String m_type;

	private Integer m_amount;

	private Double m_price;

	private String m_domainColor;

	private Image m_image;

	/**
	 * Constructor.
	 *
	 * @param m_id
	 *            Unique item ID.
	 * @param m_name
	 *            Item name.
	 * @param m_type
	 *            Item type.
	 * @param m_price
	 *            Item price.
	 * @param domainColor
	 *            Item domain color.
	 * @param m_image
	 *            Item image.
	 */
	public CatalogItemRow(Integer m_id, String m_name, String m_type, Double m_price, String domainColor, Image m_image)
	{
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_domainColor = domainColor;
		this.m_image = ResizeImage(m_image);
	}

	/**
	 * Constructor with default image.
	 *
	 * @param m_id
	 *            Unique item ID.
	 * @param m_name
	 *            Item name.
	 * @param m_type
	 *            Item type.
	 * @param m_price
	 *            Item price.
	 * @param domainColor
	 *            Item domain color.
	 */
	public CatalogItemRow(int m_id, String m_name, String m_type, double m_price, String domainColor)
	{
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_domainColor = domainColor;
		this.m_image = ResizeImage(new Image(getClass().getResourceAsStream("/boundaries/images/Zerli_Headline.jpg")));
	}

	/**
	 * Constructor for payment table.
	 *
	 * @param id
	 *            Unique item ID.
	 * @param name
	 *            Item name.
	 * @param amount
	 *            Item amount.
	 * @param price
	 *            Summered price.
	 * @param m_image
	 *            Image of the item.
	 * @param domainColor
	 *            Domain color of the item
	 * @param type
	 *            Type of the item.
	 */
	public CatalogItemRow(int id, String name, int amount, double price, Image m_image, String domainColor, String type)
	{
		super();
		this.m_id = id;
		this.m_name = name;
		this.m_type = type;
		this.m_price = price;
		this.m_domainColor = domainColor;
		this.m_amount = amount;
		this.m_image = ResizeImage(m_image);
	}

	/**
	 * Scale image to defined size.
	 *
	 * @param imageToResize
	 *            Image to resize.
	 * @return Resized image.
	 */
	private Image ResizeImage(Image imageToResize)
	{
		BufferedImage imageReader = SwingFXUtils.fromFXImage(imageToResize, null);
		BufferedImage newImage = ImageNewScale(imageReader, 80, 80);
		Image finalImage = SwingFXUtils.toFXImage(newImage, null);
		return finalImage;
	}

	/**
	 * Scale image with new Height and Width.
	 *
	 * @param imageToScale
	 *            The image.
	 * @param newScaleWidth
	 *            New width.
	 * @param newScaleHeight
	 *            New Height.
	 * @return Scale image.
	 */
	private BufferedImage ImageNewScale(BufferedImage imageToScale, int newScaleWidth, int newScaleHeight)
	{
		BufferedImage scaledImage = null;
		if (imageToScale != null) {
			scaledImage = new BufferedImage(newScaleWidth, newScaleHeight, imageToScale.getType());
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(imageToScale, 0, 0, newScaleWidth, newScaleHeight, null);
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.dispose();
		}
		return scaledImage;
	}

	/**
	 * @return Item id.
	 */
	public Integer getM_id()
	{
		return m_id;
	}

	/**
	 * @return String item id.
	 */
	public String getId()
	{
		return m_id.toString();
	}

	/**
	 * @param m_id
	 *            Item id to set.
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
	 * @return Item name.
	 */
	public String getName()
	{
		return m_name.toString();
	}

	/**
	 * @param m_name
	 *            Item name to set.
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
	 * @return Item type.
	 */
	public String getType()
	{
		return m_type.toString();
	}

	/**
	 * @param m_type
	 *            Item type to set.
	 */
	public void setM_type(String m_type)
	{
		this.m_type = m_type;
	}

	/**
	 * @return Item amount.
	 */
	public Integer getM_amount()
	{
		return m_amount;
	}

	/**
	 * @return String item amount.
	 */
	public String getAmount()
	{
		return m_amount.toString();
	}

	/**
	 * @param m_amount
	 *            Item amount to set.
	 */
	public void setM_amount(Integer m_amount)
	{
		this.m_amount = m_amount;
	}

	/**
	 * @return Item price.
	 */
	public Double getM_price()
	{
		return m_price;
	}

	/**
	 * @return Item string price.
	 */
	public String getPrice()
	{
		return m_price.toString();
	}

	/**
	 * @param m_price
	 *            Item price to set.
	 */
	public void setM_price(Double m_price)
	{
		this.m_price = m_price;
	}

	/**
	 * @return Item domain color.
	 */
	public String getM_domainColor()
	{
		return m_domainColor;
	}

	/**
	 * @param m_domainColor
	 *            Item domain color to set.
	 */
	public void setM_domainColor(String m_domainColor)
	{
		this.m_domainColor = m_domainColor;
	}

	/**
	 * @return Item image.
	 */
	public Image getM_image()
	{
		return m_image;
	}

	/**
	 * @return Item image ImageView.
	 */
	public ImageView getImage()
	{
		return new ImageView(m_image);
	}

	/**
	 * @param m_image
	 *            Item image to set.
	 */
	public void setM_image(Image m_image)
	{
		this.m_image = ResizeImage(m_image);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "CatalogItemRaw [m_id=" + m_id + ", m_name=" + m_name + ", m_type=" + m_type + ", m_price=" + m_price
				+ ", m_image=" + m_image + "]";
	}

}
