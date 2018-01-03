
package entities;

import java.io.InputStream;
import java.io.Serializable;

import com.sun.istack.internal.NotNull;

import javafx.scene.image.Image;
import utilities.ImageUtilities;

/**
 *
 * SerializableImage: The Class deals with a problem that a {@link Image} can
 * not be sent, cause serialization error.
 * 
 * @see Serializable
 */
public class SerializableImage implements Serializable
{
	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -4878998813933824532L;

	private transient Image m_image;

	private InputStream m_inputStream;

	// end region -> Fields

	// region Getters

	/**
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @return the m_inputStream
	 */
	public InputStream getInputStream()
	{
		return m_inputStream;
	}

	/**
	 * @return The contained {@link Image} in the class.
	 */
	public Image getImage()
	{
		if (m_image == null && m_inputStream != null) {
			m_image = ImageUtilities.InputStreamToImage(m_inputStream, null);
		}
		return m_image;
	}

	/**
	 * 
	 * @return <code>true</code> if an image contained, and <code>false</code> if
	 *         does not.
	 */
	public boolean isEmpty()
	{
		return m_image == null && m_inputStream == null;
	}
	// end region -> Getters

	// region Setters

	/**
	 * @param image
	 *            the new {@link Image} to set.
	 */
	public void setImage(Image image)
	{
		m_image = image;
		m_inputStream = ImageUtilities.ImageToInputStream(image, "jpeg", null);
	}

	/**
	 * 
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream)
	{
		m_inputStream = inputStream;
		m_image = ImageUtilities.InputStreamToImage(inputStream, null);
	}
	// end region -> Setters

	// region Constructors

	/**
	 * 
	 * Create instance with null {@link Image}.
	 *
	 */
	public SerializableImage()
	{
		m_image = null;
		m_inputStream = null;
	}

	/**
	 * 
	 * Create instance with contained {@link Image}.
	 *
	 * @param image
	 *            An image to contain in class.
	 */
	SerializableImage(@NotNull Image image)
	{
		m_image = image;
		m_inputStream = ImageUtilities.ImageToInputStream(image, "jpeg", null);
	}

	/**
	 * 
	 * Create instance with contained {@link Image}.
	 *
	 * @param image
	 *            An image to contain in class.
	 */
	SerializableImage(@NotNull InputStream inputStream)
	{
		m_inputStream = inputStream;
		m_image = ImageUtilities.InputStreamToImage(inputStream, null);
	}

	// end region -> Constructors
}
