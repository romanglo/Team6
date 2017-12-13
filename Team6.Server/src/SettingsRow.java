import configurations.ServerConfiguration;
import javafx.scene.control.TableView;

/**
 *
 * SettingsRow: POJO class for binding between {@link ServerConfiguration} to
 * {@link TableView}.
 * 
 */
@SuppressWarnings("javadoc")
public class SettingsRow {

	private String setting;
	private String value;
	private String type;

	public SettingsRow(String parent, String setting, String value) {
		super();
		this.setting = setting;
		this.value = value;
		this.setType(parent);
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

	public String getType() {
		return type;
	}

	public void setType(String parent) {
		this.type = parent;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Type=" + type + ", Setting=" + setting + ", Value=" + value + "]";
	}

}
