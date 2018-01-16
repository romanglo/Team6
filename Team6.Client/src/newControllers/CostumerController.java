
package newControllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 *
 * ExampleController: TODO Roman: Auto-generated type stub - Change with type
 * description
 * 
 */
public class CostumerController extends BaseController
{

	@FXML AnchorPane anchorpane_option1;

	@FXML AnchorPane anchorpane_option21;

	@FXML AnchorPane anchorpane_option2;

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
}
