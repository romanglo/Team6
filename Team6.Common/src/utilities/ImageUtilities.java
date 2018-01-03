
package utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.istack.internal.Nullable;

import javafx.scene.image.Image;

/**
 *
 * ImageUtilities: An utilities to work with different kinds of image classes.
 * 
 */
public class ImageUtilities
{

	/**
	 * 
	 * A method which generate a {@link InputStream} from {@link Image} instance.
	 *
	 * @param image
	 *            Image instance that will be generated to {@link InputStream}.
	 * @param imageType
	 *            The image file extension, for example: "jpg", "jpeg", "png", etc.
	 * @param logger
	 *            An optional logger, will be used if the parsing process failed.
	 * @return A {@link InputStream} if the parsing succeed, <code>null</code> if
	 *         the parsing process failed.
	 * @throws NullPointerException
	 *             if one of the parameters is <code>null</code> (or the String is
	 *             empty).
	 */
	public static InputStream ImageToInputStream(Image image, String imageType, @Nullable Logger logger)
			throws NullPointerException
	{

		byte[] imageInByte = ImageToByteArray(image, imageType, logger);
		if (imageInByte == null) {
			return null;
		}
		InputStream returningInputStram = null;
		try {
			returningInputStram = new ByteArrayInputStream(imageInByte);
		}
		catch (Exception e) {
			if (logger != null && logger.isLoggable(Level.WARNING)) {
				logger.warning("Failed on try to create InputStream from javafx.scene.image.Image, exception: "
						+ e.getMessage());
			}
		}
		return returningInputStram;
	}

	/**
	 * 
	 * A method which generate a {@link InputStream} from {@link Image} instance.
	 *
	 * @param image
	 *            Image instance that will be generated to {@link InputStream}.
	 * @param imageType
	 *            The image file extension, for example: "jpg", "jpeg", "png", etc.
	 * @param logger
	 *            An optional logger, will be used if the parsing process failed.
	 * @return A byte array if the parsing succeed, <code>null</code> if the parsing
	 *         process failed.
	 * @throws NullPointerException
	 *             if one of the parameters is <code>null</code> (or the String is
	 *             empty).
	 */
	public static byte[] ImageToByteArray(Image image, String imageType, @Nullable Logger logger)
			throws NullPointerException
	{
		if (image == null) {
			throw new NullPointerException("Image parameter is null!");
		}

		if (imageType == null || imageType.isEmpty()) {
			throw new NullPointerException("String parameter which describes image type is null or empty!");
		}

		byte[] imageInByte = null;
		try {
			BufferedImage fromFXImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(fromFXImage, imageType, byteArrayOutputStream);
			byteArrayOutputStream.flush();
			imageInByte = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		}
		catch (Exception e) {
			if (logger != null && logger.isLoggable(Level.WARNING)) {
				logger.warning("Failed on try to create InputStream from javafx.scene.image.Image, exception: "
						+ e.getMessage());
			}
		}
		return imageInByte;
	}

	/**
	 * 
	 * A method which generate a {@link Image} instance from {@link InputStream}.
	 *
	 * @param inputStream
	 *            {@link InputStream} which will be parsed to {@link Image}
	 *            instance.
	 * @param logger
	 *            An optional logger, will be used if the parsing process failed.
	 * @return A {@link Image} if the parsing succeed, <code>null</code> if the
	 *         parsing process failed.
	 * @throws NullPointerException
	 *             if one of the received parameter is null.
	 */
	public static Image InputStreamToImage(InputStream inputStream, @Nullable Logger logger) throws NullPointerException
	{

		if (inputStream == null) {
			throw new NullPointerException("InputStream parameter is null!");
		}

		Image returnedImage = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(inputStream);

			returnedImage = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
		}
		catch (Exception e) {
			if (logger != null && logger.isLoggable(Level.WARNING)) {
				logger.warning("Failed on try to create javafx.scene.image.Image from InputStream , exception: "
						+ e.getMessage());
			}
		}

		return returnedImage;
	}

	/**
	 * 
	 * A method which generate a {@link Image} instance from byte array.
	 *
	 * @param bytes
	 *            byte array which will be parsed to {@link Image} instance.
	 * @param logger
	 *            An optional logger, will be used if the parsing process failed.
	 * @return A {@link Image} if the parsing succeed, <code>null</code> if the
	 *         parsing process failed.
	 * @throws NullPointerException
	 *             if one of the received parameter is null.
	 */
	public static Image ByteArrayToImage(byte[] bytes, @Nullable Logger logger) throws NullPointerException
	{

		if (bytes == null) {
			throw new NullPointerException("byte array parameter is null!");
		}

		Image returnedImage = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(bytes);
			returnedImage = InputStreamToImage(inputStream, logger);
		}
		catch (Exception e) {
			if (logger != null && logger.isLoggable(Level.WARNING)) {
				logger.warning("Failed on try to create javafx.scene.image.Image from InputStream , exception: "
						+ e.getMessage());
			}
		}

		return returnedImage;
	}
}
