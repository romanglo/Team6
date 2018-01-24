
package common;

import java.io.InputStream;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 *
 * AlertBuilder: A builder of {@link Alert} window.
 *
 */
public class AlertBuilder
{

	private static boolean s_imagesInitialized = false;

	private static Image s_infoImage = null;

	private static Image s_warningImage = null;

	private static Image s_errorImage = null;

	private static Image s_confirmationImage = null;

	private Alert m_alert;

	private boolean m_defaultTitle;

	/**
	 * 
	 * Initialize an empty builder, the default {@link Alert} window is a
	 * {@link AlertType#INFORMATION} with empty content and header.
	 *
	 */
	public AlertBuilder()
	{
		m_alert = new Alert(AlertType.INFORMATION);
		m_alert.setTitle("Information Message");
		m_alert.setContentText(null);
		m_alert.setHeaderText(null);
		m_defaultTitle = true;
		initializeImages();
	}

	private void initializeImages()
	{
		if (s_imagesInitialized) {
			return;
		}

		InputStream infoResource = getClass().getResourceAsStream("/common/images/info.png");
		if (infoResource != null) {
			s_infoImage = new Image(infoResource);
		}

		InputStream warningResource = getClass().getResourceAsStream("/common/images/warning.png");
		if (warningResource != null) {
			s_warningImage = new Image(warningResource);
		}

		InputStream errorResource = getClass().getResourceAsStream("/common/images/error.png");
		if (errorResource != null) {
			s_errorImage = new Image(errorResource);
		}

		InputStream confirmationResource = getClass().getResourceAsStream("/common/images/confirmation.png");
		if (confirmationResource != null) {
			s_confirmationImage = new Image(confirmationResource);
		}

		s_imagesInitialized = true;
	}

	/**
	 * 
	 * The method build the alert message.
	 *
	 * @return an {@link Alert}.
	 */
	public Alert build()
	{
		if (!s_imagesInitialized) {
			return m_alert;
		}

		ImageView imageView = null;
		switch (m_alert.getAlertType()) {
			case INFORMATION:
				if (s_infoImage != null) {
					imageView = new ImageView(s_infoImage);
				}
			break;
			case CONFIRMATION:
				if (s_confirmationImage != null) {
					imageView = new ImageView(s_confirmationImage);
				}
			break;
			case ERROR:
				if (s_errorImage != null) {
					imageView = new ImageView(s_errorImage);
				}
			break;
			case WARNING:
				if (s_warningImage != null) {
					imageView = new ImageView(s_warningImage);
				}
			break;

			default:
			break;
		}

		if (imageView != null) {
			imageView.setFitHeight(40);
			imageView.setFitWidth(40);
			m_alert.setGraphic(imageView);
		}

		return m_alert;
	}

	/**
	 * @param title
	 *            the title of the {@link Alert}.
	 * @return an {@link AlertBuilder}.
	 */
	public AlertBuilder setTitle(String title)
	{
		m_alert.setTitle(title);
		m_defaultTitle = false;
		return this;
	}

	/**
	 * @param headerText
	 *            the header text of the {@link Alert}.
	 * @return an {@link AlertBuilder}.
	 */
	public AlertBuilder setHeaderText(String headerText)
	{
		if (headerText != null && !headerText.isEmpty()) {
			m_alert.setHeaderText(headerText);
		} else {
			m_alert.setHeaderText(null);
		}
		return this;
	}

	/**
	 * @param contentText
	 *            the content text of the {@link Alert}.
	 * @return an {@link AlertBuilder}.
	 */
	public AlertBuilder setContentText(String contentText)
	{
		if (contentText != null && !contentText.isEmpty()) {
			m_alert.setContentText(contentText);
		} else {
			m_alert.setContentText(null);
		}
		return this;
	}

	/**
	 * @param alertType
	 *            the type of the {@link Alert}.
	 * @return an {@link AlertBuilder}.
	 */
	public AlertBuilder setAlertType(AlertType alertType)
	{
		m_alert.setAlertType(alertType);

		if (m_defaultTitle) {
			switch (alertType) {
				case CONFIRMATION:
					m_alert.setTitle("Confirmation Message");
				break;

				case ERROR:
					m_alert.setTitle("Error Message");
				break;

				case INFORMATION:
					m_alert.setTitle("Information Message");
				break;

				case WARNING:
					m_alert.setTitle("Warning Message");
				break;

				default:
					m_alert.setTitle("Message");
				break;
			}
		}
		return this;
	}
}
