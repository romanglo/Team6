
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * SurveysReport: A POJO to database 'surveys_reports' table.
 * 
 */
public class SurveysReport implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 5765120908638121496L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("yyyy");

	private int m_shopManagerId;

	private java.util.Date m_year;

	private int m_quarter;

	private float m_firstAnswerAverage;

	private float m_secondAnswerAverage;

	private float m_thirdAnswerAverage;

	private float m_fourthAnswerAverage;

	private float m_fifthAnswerAverage;

	private float m_sixthAnswerAverage;

	/**
	 * @return the shopManagerId
	 */
	public int getShopManagerId()
	{
		return m_shopManagerId;
	}

	/**
	 * @param shopManagerId
	 *            the shopManagerId to set
	 */
	public void setShopManagerId(int shopManagerId)
	{
		m_shopManagerId = shopManagerId;
	}

	/**
	 * @return the surveyDate
	 */
	public java.util.Date getSurveyDate()
	{
		return m_year;
	}

	/**
	 * @param surveyDate
	 *            the surveyDate to set
	 */
	public void setSurveyDate(java.util.Date surveyDate)
	{
		m_year = surveyDate;
	}

	/**
	 * @return the quarter
	 */
	public int getQuarter()
	{
		return m_quarter;
	}

	/**
	 * @param quarter
	 *            the quarter to set
	 */
	public void setQuarter(int quarter)
	{
		m_quarter = quarter;
	}

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
		return "SurveysReport [shopManagerId=" + m_shopManagerId + ", year=" + s_dateForamt.format(m_year) + ", quarter="
				+ m_quarter + ", firstAnswerAverage=" + m_firstAnswerAverage + ", m_secondAnswerAverage="
				+ m_secondAnswerAverage + ", thirdAnswerAverage=" + m_thirdAnswerAverage + ", fourthAnswerAverage="
				+ m_fourthAnswerAverage + ", fifthAnswerAverage=" + m_fifthAnswerAverage + ", sixthAnswerAverage="
				+ m_sixthAnswerAverage + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_quarter;
		result = prime * result + m_shopManagerId;
		result = prime * result + ((m_year == null) ? 0 : s_dateForamt.format(m_year).hashCode());
		return result;
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
		if (m_quarter != other.m_quarter) {
			return false;
		}
		if (m_shopManagerId != other.m_shopManagerId) {
			return false;
		}
		if (m_year == null) {
			if (other.m_year != null) {
				return false;
			}
		} else if (!s_dateForamt.format(m_year).equals(s_dateForamt.format(other.m_year))) {
			return false;
		}
		return true;
	}


}
