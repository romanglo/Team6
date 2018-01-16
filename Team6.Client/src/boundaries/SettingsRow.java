package boundaries;

import javafx.scene.control.TableView;

/**
*
* SettingsRow: POJO class for binding between {@link ClientConfiguration} to
* {@link TableView}.
* 
*/
@SuppressWarnings("javadoc")
public class SettingsRow {
	
	/* Fields region */
	
	private String setting;
	private String value;
	
	/* End of --> Fields region */
	
	/* Constructors region */
	
	public SettingsRow(String setting, String value) {
		super();
		this.setting = setting;
		this.value = value;
	}
	
	/* End of --> Constructors region */
	
	/* Getters region */
	
	public String getSetting() {
		return setting;
	}
	
	public String getValue() {
		return value;
	}
	
	/* End of --> Getters region */
	
	/* Setters region */
	
	public void setSetting(String setting) {
		this.setting = setting;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/* End of --> Setters region */
}