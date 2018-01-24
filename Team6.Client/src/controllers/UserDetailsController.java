
package controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import client.ApplicationEntryPoint;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import newEntities.User;

/**
 *
 * UserDetailsController: Controller that related to UserDetails.fxml
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
	public void initialize(URL url, ResourceBundle bundle)
	{

		User connectedUser = ApplicationEntryPoint.ConnectedUser;
		if (connectedUser == null) {
			label_username.setText(" - ");
			label_password.setText(" - ");
			label_email.setText(" - ");
			label_privilege.setText(" - ");
			intializeUserDefaultImage();
			return;
		}

		label_username.setText(connectedUser.getUserName());
		label_password.setText(connectedUser.getPassword());
		label_email.setText(connectedUser.getEmail());

		if (connectedUser.getImage() == null) {
			intializeUserDefaultImage();
		} else {
			imageview_user.setImage(connectedUser.getImage());

		}

		initializeUserPrivilage(connectedUser);
	}

	/**
	 * 
	 * The method initialize default image to {@link User}s without image.
	 *
	 */
	private void intializeUserDefaultImage()
	{
		InputStream user = getClass().getResourceAsStream("/boundaries/images/user2.png");
		if (user != null) {
			Image accountImage = new Image(user);
			imageview_user.setImage(accountImage);
		}

	}

	private void initializeUserPrivilage(User connectedUser)
	{
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
				priv = "Shop Manager";
			break;
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
		Timeline timeline = new Timeline();
		KeyFrame key = new KeyFrame(Duration.seconds(1),
				new KeyValue(currentStage.getScene().getRoot().opacityProperty(), 0));
		timeline.getKeyFrames().add(key);
		timeline.setOnFinished((actionEvent) -> currentStage.close());
		timeline.play();
	}
}
