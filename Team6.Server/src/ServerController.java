
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class ServerController 
{
	@FXML
	private Button btn_db_start;

	@FXML
	private Button btn_db_stop;
	
	@FXML
	private Circle circle_db_on;
	
	@FXML
	private Circle circle_db_off;
	
	@FXML
	void startDb(ActionEvent event)
	{
		btn_db_start.setDisable(true);
		btn_db_stop.setDisable(false);
		circle_db_on.setFill(Paint.valueOf("green"));
		circle_db_off.setFill(Paint.valueOf("grey"));

	}
	
	@FXML
	void stopDb(ActionEvent event)
	{
		
	}
	
	@FXML
	void initialize()
	{

	}
}
