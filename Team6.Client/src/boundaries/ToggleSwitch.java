
package boundaries;

import com.sun.istack.internal.Nullable;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * ToggleSwitch: A decorated toggle switch button, can be used in FXML file to.
 */
public class ToggleSwitch extends Parent
{

	// region Listeners Interfaces

	/**
	 * ISwitchedListener: A listener to changes in in switch state.
	 */
	public interface ISwitchedListener
	{

		/**
		 * 
		 * @param isOn
		 *            <code>true</code> if the switch is on and <code>false</code> if
		 *            does not.
		 */
		void onSwitch(boolean isOn);
	}

	// end region -> Listeners Interfaces

	// region Fields

	private BooleanProperty m_switchedOn = new SimpleBooleanProperty(false);

	private TranslateTransition m_translateAnimation = new TranslateTransition(Duration.seconds(0.25));

	private FillTransition m_fillAnimation = new FillTransition(Duration.seconds(0.25));

	private ParallelTransition m_animation = new ParallelTransition(m_translateAnimation, m_fillAnimation);

	private ISwitchedListener m_switchedListener = null;

	// end region -> Fields

	// region Getters & Setters

	/**
	 * @param switchedListener
	 *            the switchedListener to set, set <code>null</code> to reset the
	 *            handler.
	 */
	public void setSwitchedListener(@Nullable ISwitchedListener switchedListener)
	{
		m_switchedListener = switchedListener;
	}

	/**
	 * @return the switchedOn
	 */
	public boolean isSwitchedOn()
	{
		return m_switchedOn.get();
	}

	/**
	 * @param switchedOn
	 *            the switchedOn to set
	 */
	public void setSwitchedOn(boolean switchedOn)
	{
		m_switchedOn.set(switchedOn);
	}

	// end region -> Getters & Setters

	@SuppressWarnings("javadoc")
	public ToggleSwitch()
	{
		int width = 60;
		int height = 30;

		Rectangle background = new Rectangle(width, height);
		background.setArcWidth(height);
		background.setArcHeight(height);
		background.setFill(Color.WHITE);
		background.setStroke(Color.LIGHTGRAY);

		double radius = Math.min(width, height) / 2.0;
		Circle trigger = new Circle(radius);
		trigger.setCenterX(radius);
		trigger.setCenterY(radius);
		trigger.setFill(Color.WHITE);
		trigger.setStroke(Color.LIGHTGRAY);

		DropShadow shadow = new DropShadow();
		shadow.setRadius(2);
		trigger.setEffect(shadow);

		m_translateAnimation.setNode(trigger);
		m_fillAnimation.setShape(background);

		getChildren().addAll(background, trigger);

		m_switchedOn.addListener((obs, oldState, newState) -> {
			boolean isOn = newState.booleanValue();
			m_translateAnimation.setToX(isOn ? Math.abs(width - height) : 0);
			m_fillAnimation.setFromValue(isOn ? Color.WHITE : Color.LIGHTGREEN);
			m_fillAnimation.setToValue(isOn ? Color.LIGHTGREEN : Color.WHITE);
			m_animation.play();
			if (m_switchedListener != null) {
				m_switchedListener.onSwitch(isOn);
			}
		});

		setOnMouseClicked(event -> {
			m_switchedOn.set(!m_switchedOn.get());
		});

	}
}
