
package newEntities;

import java.io.Serializable;

/**
 *
 * SurveysReport: A POJO to database 'surveys_reports' table.
 * 
 * @see Report
 * @see IEntity
 * @see Serializable
 */
public class SurveysReport extends Report
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 5765120908638121496L;

	private float m_firstAnswerAverage;

	private float m_secondAnswerAverage;

	private float m_thirdAnswerAverage;

	private float m_fourthAnswerAverage;

	private float m_fifthAnswerAverage;

	private float m_sixthAnswerAverage;

	/**
	 * @return the firstAnswerAverage
	 */
	public float getFirstAnswerAverage()
	{
		return m_firstAnswerAverage;
	}

	/**
	 * @param firstAnswerAverage
	 *            the firstAnswerAverage to set
	 */
	public void setFirstAnswerAverage(float firstAnswerAverage)
	{
		m_firstAnswerAverage = firstAnswerAverage;
	}

	/**
	 * @return the secondAnswerAverage
	 */
	public float getSecondAnswerAverage()
	{
		return m_secondAnswerAverage;
	}

	/**
	 * @param secondAnswerAverage
	 *            the secondAnswerAverage to set
	 */
	public void setSecondAnswerAverage(float secondAnswerAverage)
	{
		m_secondAnswerAverage = secondAnswerAverage;
	}

	/**
	 * @return the thirdAnswerAverage
	 */
	public float getThirdAnswerAverage()
	{
		return m_thirdAnswerAverage;
	}

	/**
	 * @param thirdAnswerAverage
	 *            the thirdAnswerAverage to set
	 */
	public void setThirdAnswerAverage(float thirdAnswerAverage)
	{
		m_thirdAnswerAverage = thirdAnswerAverage;
	}

	/**
	 * @return the fourthAnswerAverage
	 */
	public float getFourthAnswerAverage()
	{
		return m_fourthAnswerAverage;
	}

	/**
	 * @param fourthAnswerAverage
	 *            the fourthAnswerAverage to set
	 */
	public void setFourthAnswerAverage(float fourthAnswerAverage)
	{
		m_fourthAnswerAverage = fourthAnswerAverage;
	}

	/**
	 * @return the fifthAnswerAverage
	 */
	public float getFifthAnswerAverage()
	{
		return m_fifthAnswerAverage;
	}

	/**
	 * @param fifthAnswerAverage
	 *            the fifthAnswerAverage to set
	 */
	public void setFifthAnswerAverage(float fifthAnswerAverage)
	{
		m_fifthAnswerAverage = fifthAnswerAverage;
	}

	/**
	 * @return the sixthAnswerAverage
	 */
	public float getSixthAnswerAverage()
	{
		return m_sixthAnswerAverage;
	}

	/**
	 * @param sixthAnswerAverage
	 *            the sixthAnswerAverage to set
	 */
	public void setSixthAnswerAverage(float sixthAnswerAverage)
	{
		m_sixthAnswerAverage = sixthAnswerAverage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "SurveysReport [" + super.toString() + ", firstAnswerAverage=" + m_firstAnswerAverage
				+ ", m_secondAnswerAverage=" + m_secondAnswerAverage + ", thirdAnswerAverage=" + m_thirdAnswerAverage
				+ ", fourthAnswerAverage=" + m_fourthAnswerAverage + ", fifthAnswerAverage=" + m_fifthAnswerAverage
				+ ", sixthAnswerAverage=" + m_sixthAnswerAverage + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SurveysReport)) {
			return false;
		}
		SurveysReport other = (SurveysReport) obj;

		return super.equals(other);
	}

}
