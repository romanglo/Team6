
package entities;

import java.io.ByteArrayInputStream;
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

	private byte[] m_imageInBytes;

	// end region -> Fields

	// region Getters

	/**
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
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
	 * 
	 * @return <code>true</code> if an image contained, and <code>false</code> if
	 *         does not.
	 */
	public boolean isEmpty()
	{
		return m_image == null && m_imageInBytes == null;
	}
	// end region -> Getters

	// region Setters

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
	 * 
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
		m_imageInBytes = null;
	}

	/**
	 * 
	 * Create instance with contained {@link Image}.
	 *
	 * @param image
	 *            An {@link Image} to contain in class.
	 */
	SerializableImage(@NotNull Image image)
	{
		m_image = image;
		m_imageInBytes = ImageUtilities.ImageToByteArray(image, "jpeg", null);
	}

	/**
	 * 
	 * Create instance with contained {@link Image}.
	 *
	 * @param image
	 *            An {@link InputStream} the describes {@link Image} to contain in
	 *            class.
	 */
	SerializableImage(@NotNull InputStream inputStream)
	{
		m_image = ImageUtilities.InputStreamToImage(inputStream, null);
		m_imageInBytes = ImageUtilities.ImageToByteArray(m_image, "jpeg", null);
	}

	/**
	 * 
	 * Create instance with contained {@link Image}.
	 *
	 * @param image
	 *            A byte array the describes {@link Image} to contain in class.
	 */
	SerializableImage(@NotNull byte[] bytes)
	{
		m_image = ImageUtilities.ByteArrayToImage(bytes, null);
		m_imageInBytes = bytes;
	}

	// end region -> Constructors
}
