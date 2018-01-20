
package controllers;

import java.awt.Checkbox;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import client.ApplicationEntryPoint;
import client.Client;
import client.ClientConfiguration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logger.LogManager;
import newEntities.Complaint;
import newEntities.Costumer;
import newEntities.IEntity;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.IMessageData;
import newMessages.Message;
import newMessages.MessagesFactory;
import newMessages.RespondMessageData;

/**
 *
 * CompanyEmployeeController : TODO Shimon: Create class description
 * 
 */
public class CostumerSeviceEmployee_TreatmentAnOpenComplaint
		implements Initializable, Client.ClientStatusHandler, Client.MessageReceiveHandler
{
	/* UI Binding Fields region */

	// Title images :
	@FXML private ImageView imageview_gif;

	@FXML private ImageView imageview_title;

	@FXML private ImageView imageview_subtitle;

	@FXML private TextField financial_compensation;

	@FXML private ComboBox<String> combobox_id;

	@FXML private TextArea textarea_complaint;

	@FXML private TextArea textarea_summary;

	@FXML private AnchorPane ancorepane_root;

	@FXML private CheckBox checkbox_financial;

	/* End of --> UI Binding Fields region */

	/* Fields */
	private Logger m_logger;

	private Client m_client;

	private ClientConfiguration m_configuration;

	private List<IEntity> m_complaint_array;

	private ArrayList<String> m_id_array = new ArrayList<>();

	private ObservableList<String> list;

	private Complaint complaint_fun;

	private float refund;
	/* End of --> Fields region */

	/* UI events region */

	/* Initializing methods region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		initializeFields();
		initializeImages();
		initializeClientHandler();
		treatComplaint_initializeComplaint();
	}

	private void initializeFields()
	{
		m_logger = LogManager.getLogger();
		m_configuration = ApplicationEntryPoint.ClientConfiguration;
		m_client = ApplicationEntryPoint.Client;
	}

	private void initializeImages()
	{
		InputStream serverGif = getClass().getResourceAsStream("/boundaries/images/Flower.gif");
		if (serverGif != null) {
			Image image = new Image(serverGif);
			imageview_gif.setImage(image);
		}
		InputStream title = getClass().getResourceAsStream("/boundaries/images/Zerli_Headline.jpg");
		if (title != null) {
			Image image = new Image(title);
			imageview_title.setImage(image);
		}
		InputStream costumerSeviceHeadLine = getClass()
				.getResourceAsStream("/boundaries/images/TreatmentAnOpenComplain.png");
		if (serverGif != null) {
			Image image = new Image(costumerSeviceHeadLine);
			imageview_subtitle.setImage(image);
		}
	}

	private void initializeClientHandler()
	{
		m_client.setMessagesHandler(this);
		m_client.setClientStatusHandler(this);
	}

	private void treatComplaint_initializeComplaint()
	{
		Complaint entity = new Complaint();
		Message msg = MessagesFactory.createGetAllEntityMessage(entity);
		m_client.sendMessageToServer(msg);
	}

	/* End of --> Initializing methods region */

	/* UI events region */

	/**
	 * The function back to costumer service employee main window.
	 *
	 * @param event
	 *            Back button clicked
	 */
	@FXML
	public void backButtonClick(ActionEvent event)
	{
		try {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundaries/CostumerServiceEmployee.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/boundaries/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			String msg = "Failed on try to load the next window";
			m_logger.severe(msg + ", excepion: " + e.getMessage());
			showInformationMessage(msg);
		}
	}

	/**
	 * Turn On the financial compensation text field
	 *
	 * @param event
	 *            click on check box.
	 */
	@FXML
	public void treatComplaint_financialCompensation(ActionEvent event)
	{
		if (financial_compensation.isDisable()) financial_compensation.setDisable(false);
		else financial_compensation.setDisable(true);
	}

	/**
	 * TODO Send message to server with the update complain.
	 *
	 * @param event
	 *            Update button clicked
	 */
	@FXML
	public void treatComplaint_updateClick(ActionEvent event)
	{
		if ((combobox_id.getValue().equals(""))) {
			showInformationMessage("complaint id field is empty");
			return;
		}
		int com_id = Integer.parseInt(combobox_id.getValue());
		int i = 0;
		while (((Complaint) m_complaint_array.get(i)).getId() != com_id) {
			i++;
		}
		((Complaint) m_complaint_array.get(i)).setSummary(textarea_summary.getText());
		if (!(financial_compensation.isDisable())) {
			if (financial_compensation.getText().equals("")) {
				showInformationMessage("refund field is empty please enter refund or disable it.");
				return;
			}
			complaint_fun = (Complaint) m_complaint_array.get(i);
			complaint_fun.setSummary(textarea_summary.getText());
			try {
			refund = Float.parseFloat(financial_compensation.getText());
			}catch(NumberFormatException  e)
			{
				showInformationMessage("Cant Convert to float please enter number");
				return;
			}
			Costumer cos_entity = new Costumer();
			cos_entity.setId(complaint_fun.getCostumerId());
			Message msg = MessagesFactory.createGetEntityMessage(cos_entity);
			m_client.sendMessageToServer(msg);
			tri();
		} else {
			((Complaint) m_complaint_array.get(i)).setOpened(false);
			Message msgg = MessagesFactory.createUpdateEntityMessage(m_complaint_array.get(i));
			m_client.sendMessageToServer(msgg);
			ancorepane_root.setDisable(true);
			tri();
		}
	}

	@FXML
	public void treatComplaint_setIdInCombobox()
	{
		list = FXCollections.observableArrayList(m_id_array);
		combobox_id.setItems(list);
	}

	@FXML
	public void treatComplaint_chooseComplaint(ActionEvent event)
	{
		if ((combobox_id.getValue() == null) || (combobox_id.getValue().equals(""))) return;
		int id = Integer.parseInt(combobox_id.getValue());
		Complaint comp = null;
		for (int i = 0; i < m_complaint_array.size(); i++) {
			Complaint temp = (Complaint) m_complaint_array.get(i);
			if (temp.getId() == id) comp = (Complaint) m_complaint_array.get(i);
		}
		textarea_complaint.setText(comp.getComplaint());
	}

	@FXML
	private void tri()
	{
		combobox_id.setDisable(true);
		combobox_id.getItems().clear();
		m_complaint_array.clear();
		m_id_array.clear();
		treatComplaint_initializeComplaint();
		combobox_id.setDisable(false);
		textarea_complaint.clear();
		textarea_summary.clear();
		financial_compensation.clear();
		financial_compensation.setDisable(true);
		checkbox_financial.setSelected(false);
	}
	/* End of --> UI events region */

	// region Private Methods

	private void showInformationMessage(String message)
	{
		if (message == null || message.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		ancorepane_root.setDisable(false);
	}

	/* End of --> Private Methods region */

	/* Client handlers implementation region */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onMessageReceived(Message msg)
	{
		boolean flag = true;
		IMessageData messageData = msg.getMessageData();
		if (messageData instanceof EntitiesListData) // Happens when initializeComplaint works fill m_complaint_array.
		{
			EntitiesListData entitiesListData = (EntitiesListData) msg.getMessageData();
			m_complaint_array = entitiesListData.getEntities();
			for (int i = 0; i < m_complaint_array.size(); i++) {
				Complaint temp = (Complaint) (m_complaint_array.get(i));
				if (temp.isOpened()) {
					String s = Integer.toString(temp.getId());
					m_id_array.add(s);
				}
			}
			treatComplaint_setIdInCombobox();
		} else if (messageData instanceof EntityData) // Happens when it need to update refund.
		{
			Costumer cos_entity = (Costumer) ((EntityData) messageData).getEntity();
			cos_entity.setBalance(cos_entity.getBalance() + refund);
			msg = MessagesFactory.createUpdateEntityMessage(cos_entity);
			m_client.sendMessageToServer(msg);
		} else if (msg.getMessageData() instanceof RespondMessageData) // Respond from server
		{
			RespondMessageData res = (RespondMessageData) msg.getMessageData();
			if (((EntityData) (res.getMessageData())).getEntity() instanceof Costumer) // Respond about costumer refund
			{
				if (!(res.isSucceed())) flag = false;
				else {
					complaint_fun.setOpened(false);
					msg = MessagesFactory.createUpdateEntityMessage(complaint_fun);
					m_client.sendMessageToServer(msg);
				}
			} else {
				if (flag == false) {
					m_logger.severe("Can't Update complaint please try again");
					ancorepane_root.setDisable(false);
				} else {
					m_logger.severe("Update succssed");
					ancorepane_root.setDisable(false);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientConnected()
	{
		// TODO Shimon : Add event handling
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClientDisconnected()
	{
		// TODO Shimon : Add event handling
	}

	/* End of --> Client handlers implementation region */
}
