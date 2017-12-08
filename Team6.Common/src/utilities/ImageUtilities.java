
package utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 *
 * ImageUtilities: An utilities to work with different kinds of image classes.
 * 
 */
public class ImageUtilities
{

	/**
	 * 
	 * A method which generate a byte array from BufferedImage instance.
	 *
	 * @param image
	 *            BufferedImage instance that will be generated to byte array.
	 * @param imageType
	 *            The image file extension, for example: "jpg", "jpeg", "png", etc.
	 * @return A byte array if the generation succeed, null if did not.
	 * @throws Exception
	 *             if the parsing process failed!
	 * @throws NullPointerException
	 *             if one of the parameters is null (or the String is empty).
	 */
	public static byte[] ImageToByteArray(BufferedImage image, String imageType) throws Exception
	{
		if (image == null) {
			throw new NullPointerException("BufferedImage parameter is null!");
		}

		if (imageType == null || imageType.isEmpty()) {
			throw new NullPointerException("String parameter which describes image type is null or empty!");
		}

		byte[] imageInByte = null;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, imageType, byteArrayOutputStream);
		byteArrayOutputStream.flush();
		imageInByte = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();

		return imageInByte;
	}

	/**
	 * 
	 * A method which generate a BufferedImage instance from byte array.
	 *
	 * @param imageInByteArray
	 *            byte array that will be generated to BufferedImage instance.
	 * @return A BufferedImage instance if the generation succeed, null if did not.
	 * @throws Exception
	 *             if the parsing process failed!
	 * @throws NullPointerException
	 *             if one of the parameter is null.
	 */
	public static BufferedImage ByteArrayToBufferedImage(byte[] imageInByteArray) throws Exception
	{

		if (imageInByteArray == null) {
			throw new NullPointerException("Byte[] parameter is null!");
		}

		BufferedImage bufferedImage = null;

		InputStream in = new ByteArrayInputStream(imageInByteArray);
		bufferedImage = ImageIO.read(in);

		return bufferedImage;
	}
}
