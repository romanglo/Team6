

import java.io.IOException;
import java.net.URL;

import logs.LogManager;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * MainController:
 * Controls all the actions made in the UI of the client.
 * 
 */
public class MainController implements Initializable{

	@FXML
	Circle m_connectionLight = new Circle();
	@FXML
	Button m_connectToServer = new Button();
	@FXML
	Button m_disconnectFromServer = new Button();
	@FXML
	private TableView<ItemPropertyDisplay> m_productDisplayTable;
	@FXML
	private TableColumn<ItemPropertyDisplay, String> m_propertyColumn;
	@FXML
	private TableColumn<ItemPropertyDisplay, String> m_valueColumn;
	
	
	
	@SuppressWarnings("unused") 
	private static Logger s_logger = null;
	
	/**
	 * Controller constructor.
	 *
	 */
	public MainController() {
		s_logger = LogManager.getLogger();
	}

	/**
	 * Method is called after pressing the connect button and 
	 * creates new connection
	 * 
	 * @param connectionEvent  - Button event.
	 */
	public void ServerConnection(ActionEvent connectionEvent) {
//		try {
			if (connectionEvent.getSource().equals(m_connectToServer)) {
				//EntryPoint.initializeConnection();
				m_connectionLight.setFill(Color.GREEN);
			}
			else {
				//EntryPoint.disposeConnection();
				m_connectionLight.setFill(Color.RED);
			}
//		}
//		catch (IOException ioe) {
//			s_logger.warning("Connection fault. Exception: " + ioe.getMessage());
//		}
	}
	
	
	ObservableList<ItemPropertyDisplay> getProperties()
	{
		ObservableList<ItemPropertyDisplay> products = FXCollections.observableArrayList();
		products.add(new ItemPropertyDisplay("ID", "11") );
		products.add(new ItemPropertyDisplay("Name", "Narkis"));
		products.add(new ItemPropertyDisplay("Type", "Flower"));
		return products;
	}
	
	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
		m_propertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
		m_valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		m_productDisplayTable.setItems(getProperties());
		m_productDisplayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		System.out.println(m_productDisplayTable.getColumns().get(0).getCellData(0));
	}
}