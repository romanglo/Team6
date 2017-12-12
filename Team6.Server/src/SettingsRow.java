
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
