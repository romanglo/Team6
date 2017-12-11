
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
	private Button btn_connectivity_start;

	@FXML
	private Button btn_connectivity_stop;
	
	@FXML
	private Circle circle_connectivity_on;
	
	@FXML
	private Circle circle_connectivity_off;
	
	@FXML
	private TableView<String> setting_table;
	
	@FXML
	private TextArea log;
	
	
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
		btn_db_start.setDisable(false);
		btn_db_stop.setDisable(true);
		circle_db_on.setFill(Paint.valueOf("grey"));
		circle_db_off.setFill(Paint.valueOf("red"));
	}
	
	@FXML
	void startConnectivity(ActionEvent event)
	{
		btn_connectivity_start.setDisable(true);
		btn_connectivity_stop.setDisable(false);
		circle_connectivity_on.setFill(Paint.valueOf("green"));
		circle_connectivity_off.setFill(Paint.valueOf("grey"));
	}
	
	@FXML
	void stopConnectivity(ActionEvent event)
	{
		btn_connectivity_start.setDisable(false);
		btn_connectivity_stop.setDisable(true);
		circle_connectivity_on.setFill(Paint.valueOf("grey"));
		circle_connectivity_off.setFill(Paint.valueOf("red"));
	}
	
	@FXML
	private void initialize()
	{
		setting_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
	}
}
