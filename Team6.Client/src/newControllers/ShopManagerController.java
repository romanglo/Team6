
package newControllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import newMessages.Message;

/**
 *
 * ExampleController: TODO Yoni
 * 
 * 
 */
public class ShopManagerController extends BaseController
{

	// region Fields

	private @FXML AnchorPane anchorpane_option1;

	private @FXML AnchorPane anchorpane_option21;

	private @FXML AnchorPane anchorpane_option2;

	// end region -> Fields

	// region BaseController Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInitialize() throws Exception
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onSelection(String title)
	{
		switch (title) {
			case "Option 1":
				anchorpane_option1.setVisible(true);
				anchorpane_option21.setVisible(false);
			break;

			case "Option 2":
				anchorpane_option1.setVisible(false);
				anchorpane_option21.setVisible(true);
			break;

			default:
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getSideButtonsNames()
	{
		return new String[] { "Option 1", "Option 2" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(Message msg) throws Exception
	{

	}

	// end region -> BaseController Implementation
}
