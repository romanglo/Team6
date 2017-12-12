
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class ServerController implements Initializable 
{
	/*Database start-stop button declaration*/
	@FXML private Button btn_db_start;
	@FXML private Button btn_db_stop;
	
	/*Database status declaration*/
	@FXML private Circle circle_db_on;	
	@FXML private Circle circle_db_off;
	
	/*Connectivity start-stop button declaration*/
	@FXML private Button btn_connectivity_start;
	@FXML private Button btn_connectivity_stop;
	
	/*Connectivity status declaration*/
	@FXML private Circle circle_connectivity_on;
	@FXML private Circle circle_connectivity_off;
	
	/*Log text view declaration*/
	@FXML private TextArea log;
	
	/*Setting table and columns declaration*/
	@FXML private TableView<SettingsRow> setting_table;
	@FXML private TableColumn<SettingsRow, String> setting_column;
	@FXML private TableColumn<SettingsRow, String>  value_column;
	
	
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
	void logChanged(ActionEvent event)
	{
		log.selectPositionCaret(log.getLength()); 
        log.deselect();
	}
	
	
	ObservableList<SettingsRow> getSettings()
	{
		ObservableList<SettingsRow> settings = FXCollections.observableArrayList();
		settings.add(new SettingsRow("IP","1.1.1.1"));
		settings.add(new SettingsRow("PORT","5555"));
		return settings;
	}
	
	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
		setting_column.setCellValueFactory(new PropertyValueFactory<>("setting"));
		value_column.setCellValueFactory(new PropertyValueFactory<>("value"));
		setting_table.setItems(getSettings());
		setting_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		log.setText(" hi\n yo\n hi\n hi\n yo\n yo\n hi\n yo\n hi\n yo\n hi\n yo\n hi");
		
	}
	

}
