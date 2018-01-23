
package newBoundaries;

import client.ClientConfiguration;
import javafx.scene.control.TableView;

/**
 *
 * SettingsRow: POJO class for binding between {@link ClientConfiguration} to
 * {@link TableView}.
 * 
 */
public class SettingsRow
{

	/* Fields region */

	private String m_setting;

	private String m_value;

	/* End of --> Fields region */

	/* Constructors region */

	@SuppressWarnings("javadoc")
	public SettingsRow(String setting, String value)
	{
		super();
		m_setting = setting;
		m_value = value;
	}

	/* End of --> Constructors region */

	/* Getters region */

	/**
	 * @return the settings
	 */
	public String getSetting()
	{
		return m_setting;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return m_value;
	}

	/* End of --> Getters region */

	/* Setters region */

	/**
	 * @param setting
	 *            a settings to set.
	 */
	public void setSetting(String setting)
	{
		this.m_setting = setting;
	}

	/**
	 * @param value
	 *            a value to set.
	 */
	public void setValue(String value)
	{
		this.m_value = value;
	}

	/* End of --> Setters region */
}
