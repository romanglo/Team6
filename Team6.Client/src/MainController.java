

import java.io.IOException;
import logs.LogManager;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * MainController:
 * Controls all the actions made in the UI of the client.
 * 
 */
public class MainController {

	@FXML
	Circle m_connectionLight = new Circle();
	@FXML
	Button m_connectToServer = new Button();
	@FXML
	Button m_disconnectFromServer = new Button();
	
	
	
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
		try {
			if (connectionEvent.getSource().equals(m_connectToServer)) {
				EntryPoint.initializeConnection();
				m_connectionLight.setFill(Color.GREEN);
			}
			else {
				EntryPoint.disposeConnection();
				m_connectionLight.setFill(Color.RED);
			}
		}
		catch (IOException ioe) {
			s_logger.warning("Connection fault. Exception: " + ioe.getMessage());
		}
	}
	
}