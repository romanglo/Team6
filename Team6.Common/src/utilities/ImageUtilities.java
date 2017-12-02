
package utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import logs.LogManager;

/**
 *
 * ImageUtilities: An utilities to work with different kinds of image classes.
 * 
 */
public class ImageUtilities
{

	private static Logger s_logger = null;

	/**
	 * 
	 * A method which generate a byte array from BufferedImage instance.
	 *
	 * @param image
	 *            BufferedImage instance that will be generated to byte array.
	 * @param imageType
	 *            The image file extension, for example: "jpg", "jpeg", "png", etc.
	 * @return A byte array if the generation succeed, null if did not.
	 * @throws NullPointerException
	 *             if one of the parameters is null (or the String is empty).
	 */
	public static byte[] ImageToByteArray(BufferedImage image, String imageType) throws NullPointerException
	{
		loggerLazyLoad();

		if (image == null) {
			String errMsg = "BufferedImage parameter is null!";
			s_logger.log(Level.SEVERE, errMsg);
			throw new NullPointerException(errMsg);
		}

		if (imageType == null || imageType.isEmpty()) {
			String errMsg = "String parameter which describes image type is null or empty!";
			s_logger.log(Level.SEVERE, errMsg);
			throw new NullPointerException(errMsg);
		}

		byte[] imageInByte = null;
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, imageType, byteArrayOutputStream);
			byteArrayOutputStream.flush();
			imageInByte = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		}
		catch (Exception e) {
			s_logger.log(Level.WARNING, "Generation of byte array from BufferedImage failed!", e);
			imageInByte = null;
		}
		return imageInByte;
	}

	/**
	 * 
	 * A method which generate a BufferedImage instance from byte array.
	 *
	 * @param imageInByteArray
	 *            byte array that will be generated to BufferedImage instance.
	 * @return A BufferedImage instance if the generation succeed, null if did not.
	 * @throws NullPointerException
	 *             if one of the parameter is null.
	 */
	public static BufferedImage ByteArrayToBufferedImage(byte[] imageInByteArray) throws NullPointerException
	{
		loggerLazyLoad();

		if (imageInByteArray == null) {
			String errMsg = "Byte[] parameter is null!";
			s_logger.log(Level.SEVERE, errMsg);
			throw new NullPointerException(errMsg);
		}

		BufferedImage bufferedImage = null;
		try {
			InputStream in = new ByteArrayInputStream(imageInByteArray);
			bufferedImage = ImageIO.read(in);
		}
		catch (Exception e) {
			s_logger.log(Level.WARNING, "Generation of BufferedImage instance from byte array failed!", e);
		}
		return bufferedImage;
	}

	private static void loggerLazyLoad()
	{
		// lazy loading to logger
		if (s_logger == null) {
			s_logger = LogManager.getLogger();
		}
	}
}
