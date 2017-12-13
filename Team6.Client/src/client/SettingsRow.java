package client;

import javafx.scene.control.TableView;

/**
*
* SettingsRow: POJO class for binding between {@link ClientConfiguration} to
* {@link TableView}.
* 
*/
@SuppressWarnings("javadoc")
public class SettingsRow {
	
	private String setting;
	private String value;
	
	public SettingsRow(String setting, String value) {
		super();
		this.setting = setting;
		this.value = value;
	}
	
	public String getSetting() {
		return setting;
	}
	
	public void setSetting(String setting) {
		this.setting = setting;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
