
package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * XmlUtilities: An utilities to work with XML files.
 * 
 */
public class XmlUtilities
{

	/**
	 * The method parsing a XML file to specific object.
	 *
	 * @param <TData>
	 *            The type that XML file describes.
	 * @param bufferedReader
	 *            the buffer of the XML file.
	 * @param type
	 *            A class object of the type that XML file describes.
	 * @return An instance of <TData> if the parsing succeed, and null if did not.
	 * @throws Exception
	 *             If the parsing process failed.
	 * @throws NullPointerException
	 *             if one of the parameters is null.
	 */
	@SuppressWarnings("unchecked")
	public static <TData> TData parseXmlToObject(BufferedReader bufferedReader, Class<TData> type) throws Exception
	{
		if (bufferedReader == null) {
			throw new NullPointerException("BufferedReader parameter is null!");
		}

		if (type == null) {
			throw new NullPointerException("Class<TData> parameter is null!");
		}

		TData data = null;

		String xmlString = generateStringFromBufferedReader(bufferedReader);
		JAXBContext jaxbContext = JAXBContext.newInstance(type);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xmlString);
		data = (TData) unmarshaller.unmarshal(reader);

		return data;
	}

	/**
	 * 
	 * The method generate a string from XML file buffer.
	 *
	 * @param bufferedReader
	 *            the buffer that contain the XML content.
	 * @return The BufferedReader content as string.
	 * @throws IOException
	 *             if the file does not exist at the path or if reading from the
	 *             file failed.
	 */
	private static String generateStringFromBufferedReader(BufferedReader bufferedReader) throws IOException
	{
		String line;
		StringBuilder sb = new StringBuilder();

		try {
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line.trim());
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			try {
				bufferedReader.close();
			}
			catch (IOException e) {
				throw e;
			}
		}
		return sb.toString();
	}
}
