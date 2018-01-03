
package entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import com.sun.istack.internal.NotNull;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

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

	// end region -> Fields

	// region Getters

	/**
	 * @return The contained {@link Image} in the class.
	 */
	public Image getImage()
	{
		return m_image;
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
	}

	// end region -> Constructors

	// region Private Methods

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
	{
		if (m_image == null) {
			return;
		}
		s.defaultReadObject();
		setImage(SwingFXUtils.toFXImage(ImageIO.read(s), null));
	}

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		if (m_image == null) {
			return;
		}
		s.defaultWriteObject();
		ImageIO.write(SwingFXUtils.fromFXImage(m_image, null), "jpeg", s);
	}

	// end region -> Private Methods
}
