
package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import boundaries.SettingsRow;
import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import common.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import logger.LogManager;

/**
 *
 * LoginController: Create first connection with the server and login to the
 * system.
 * 
 */
public class LoginSettingsController implements Initializable
{
	// region UI Fields

	private @FXML TableView<SettingsRow> setting_table;

	private @FXML TableColumn<SettingsRow, String> tablecolumn_setting;

	private @FXML TableColumn<SettingsRow, String> tablecolumn_value;

	private @FXML Button btn_update_settings;

	// end region -> UI Fields

	// region Fields

	private Logger m_logger;

	private ClientConfiguration m_configuration;

	private Client m_client;

	// end region -> Fields

	// region UI events

	/**
	 * 
	 * The method update the {@link ClientConfiguration} resource file.
	 *
	 * @param event
	 *            the trigger event.
	 */
	@FXML
	private void updateSettingsFile(ActionEvent event)
	{
		m_configuration.updateResourceFile();
		btn_update_settings.setDisable(true);
	}

	// end region -> UI events

	// region Initializing methods region

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
		m_client = ApplicationEntryPoint.Client;
		initializeConfigurationTable();
	}

	/**
	 * The method initialize {@link LoginSettingsController#setting_table} and set
	 * necessary triggers.
	 */
	private void initializeConfigurationTable()
	{
		setting_table.setRowFactory(param -> {
			TableRow<SettingsRow> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					SettingsRow rowData = tableRow.getItem();

					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Update Settings Value");
					dialog.setHeaderText("Do you want to update the value of " + rowData.getSetting() + " ?");
					dialog.setContentText("Please enter the new value:");

					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()) {
						return;
					}
					String resultString = result.get();
					if (!(resultString != null && !resultString.equals(rowData.getValue()))) {
						return;
					}

					boolean wrongInput = false;
					switch (rowData.getSetting()) {
						case ClientConfiguration.PROPERTY_NAME_IP:
							long count = resultString.chars().filter(ch -> ch == '.').count();
							if (!resultString.equalsIgnoreCase("localhost") && count != 3) {
								wrongInput = true;
								showAlertMessage("Please enter valid IP address!", AlertType.INFORMATION);
								break;
							}

							resultString = resultString.toLowerCase();
							m_logger.info(String.format("The property:\"%s\" updated from %s to %s",
									ClientConfiguration.PROPERTY_NAME_IP, m_configuration.getIp(), resultString));

							m_client.setHost(resultString);
							m_configuration.setIp(resultString);

						break;

						case ClientConfiguration.PROPERTY_NAME_PORT:

							try {
								int port = Integer.parseInt(resultString);
								if (port >= 0 && port <= 65636) {
									m_logger.info(String.format("The property:\"%s\" updated from %d to %d",
											ClientConfiguration.PROPERTY_NAME_PORT, m_configuration.getPort(), port));
									m_client.setPort(port);
									m_configuration.setPort(port);
								} else {
									wrongInput = true;
								}

							}
							catch (NumberFormatException numberFormatException) {
								wrongInput = true;
							}
							if (wrongInput) {
								showAlertMessage("The port must be an number between 0 to 65535!", AlertType.WARNING);
							}
						break;

						default:
							wrongInput = false;
						break;
					}
					if (!wrongInput) {
						rowData.setValue(resultString);
						drawContantToTable();
						btn_update_settings.setDisable(false);
					}
				}
			});
			return tableRow;
		});
		tablecolumn_setting.setCellValueFactory(new PropertyValueFactory<>("setting"));
		tablecolumn_value.setCellValueFactory(new PropertyValueFactory<>("value"));

		drawContantToTable();

		btn_update_settings.setDisable(true);
	}

	// end region -> Initializing methods region

	// region Private methods

	/**
	 * 
	 * The method draw the {@link ClientConfiguration} to
	 * {@link LoginSettingsController#setting_table}.
	 *
	 */
	private void drawContantToTable()
	{
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();

		settings.add(new SettingsRow(ClientConfiguration.PROPERTY_NAME_IP, m_configuration.getIp()));
		settings.add(
				new SettingsRow(ClientConfiguration.PROPERTY_NAME_PORT, Integer.toString(m_configuration.getPort())));
		setting_table.setItems(settings);
	}

	/**
	 * The method show alert message from {@link Alert} type.
	 *
	 * @param message
	 *            the message to show.
	 * @param alertType
	 *            the type of the alert, selected type determinate ton the title and
	 *            the image.
	 */
	protected void showAlertMessage(String message, AlertType alertType)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		javafx.application.Platform.runLater(() -> {
			Alert alert = new AlertBuilder().setAlertType(alertType).setContentText(message).build();
			alert.showAndWait();
		});
	}

	// end region -> Private methods
}
