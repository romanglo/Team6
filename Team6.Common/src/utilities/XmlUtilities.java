
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
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
	 * @return An instance of <code>TData</code> if the parsing succeed, and null if did not.
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

	/**
	 * The method parsing a object to file.
	 *
	 * @param <TData>
	 *            The type that XML file describes.
	 * @param file
	 *            The XML file which the object will be saved to it.
	 * @param object
	 *            The object that will be saved to XML file.
	 * 
	 * @throws Exception
	 *             If the parsing process failed.
	 * @throws NullPointerException
	 *             if one of the parameters is null.
	 */
	public static <TData> void parseObjectToXml(File file, TData object) throws Exception
	{
		if (file == null) {
			throw new NullPointerException("File parameter is null!");
		}

		if (object == null) {
			throw new NullPointerException("TData object parameter is null!");
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(object, file);
	}

}
