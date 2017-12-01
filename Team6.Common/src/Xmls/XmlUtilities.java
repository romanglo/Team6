
package Xmls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import Logs.LogManager;

/**
 *
 * XmlUtilities: a utilities to work with XML files.
 * 
 */
public class XmlUtilities
{
	private static Logger s_logger=null; 


	/**
	 * The method parsing a XML file to specific object.
	 *
	 * @param <TData> The type that XML file describes.
	 * @param bufferedReader the buffer of the XML file.
	 * @param type A class object of the type that XML file describes.
	 * @return An instance of <TData> if the parsing succeed, and null if did not.
	 */
	@SuppressWarnings("unchecked")
	public static <TData> TData parseXmlToObject(BufferedReader bufferedReader, Class<TData> type)
	{
		//lazy loading to logger
		if(s_logger== null) {
			s_logger= LogManager.getLogger();
		}
		
		TData data = null;
		try {

			String xmlString = generateStringFromBufferedReader(bufferedReader);
			JAXBContext jaxbContext = JAXBContext.newInstance(type);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(xmlString);
			  data = (TData) unmarshaller.unmarshal(reader);
		}
		catch (Exception ex) {
			if(s_logger!=null) {
				s_logger.log(Level.WARNING,"Failed to parse XML file to object", ex);
			}
			data = null;
		}
		return data;
	}
	
	
	/**
	 * 
	 * The method generate a string from XML file buffer.
	 *
	 * @param bufferedReader the buffer that contain the XML content.
	 * @return The BufferedReader content as string.
	 * @throws IOException if the file does not exist at the path or if reading from the file failed.
	 */
	private static String generateStringFromBufferedReader(BufferedReader bufferedReader) throws IOException {
		String line;
		StringBuilder sb = new StringBuilder();

		try {
			while((line=bufferedReader.readLine())!= null){
			    sb.append(line.trim());
			}
		}
		catch (IOException e) {
			throw e;
		} finally {
			try {
				bufferedReader.close();
			}
			catch (IOException e) {
				if(s_logger!=null) {
					s_logger.log(Level.WARNING,"Failed on try to close the XML file.", e);
				}
			}
		}
		return sb.toString();
	}
}
