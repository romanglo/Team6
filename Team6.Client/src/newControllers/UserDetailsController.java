
package newControllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import client.ApplicationEntryPoint;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import newEntities.User;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

/**
 *
 * UserDetailsController: TODO Roman: Auto-generated type stub - Change with
 * type description
 * 
 */
public class UserDetailsController implements Initializable
{
	// region Fields

	private @FXML Label label_username;

	private @FXML Label label_password;

	private @FXML Label label_email;

	private @FXML Label label_privilege;

	private @FXML ImageView imageview_user;

	private @FXML Button button_close;

	// end region -> Fields

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		InputStream user = getClass().getResourceAsStream("/newBoundaries/images/user2.png");
		if (user != null) {
			Image accountImage = new Image(user);
			imageview_user.setImage(accountImage);
		}

		User connectedUser = ApplicationEntryPoint.ConnectedUser;
		if (connectedUser == null) {
			label_username.setText(" - ");
			label_password.setText(" - ");
			label_email.setText(" - ");
			label_privilege.setText(" - ");
			return;
		}
		label_username.setText(connectedUser.getUserName());
		label_password.setText(connectedUser.getPassword());
		label_email.setText(connectedUser.getEmail());

		String priv;
		switch (connectedUser.getPrivilege()) {
			case Administrator:
				priv = "Administrator";
			break;
			case ChainManager:
				priv = "Chain Manager";
			break;
			case CompanyEmployee:
				priv = "Company Employee";
			break;
			case Costumer:
				priv = "Costumer";
			break;
			case CostumerService:
				priv = "Costumer Service Employee";

			break;
			case ServiceSpecialist:
				priv = "Costumer Service Specialist";

			break;
			case ShopEmployee:
				priv = "Shop Employee";

			break;
			case ShopManager:
			default:
				priv = " - ";
			break;
		}
		label_privilege.setText(priv);
	}

	@FXML
	private void onClosePressed(ActionEvent event)
	{
		Stage currentStage = (Stage) imageview_user.getScene().getWindow();
		currentStage.close();
	}

}
