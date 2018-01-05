
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * Survey: A POJO to database 'surveys' table.
 * 
 */
public class Survey implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = -2059679189113267694L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private int m_id;

	private java.util.Date m_surveyDate;

	private int m_shopManagerId;

	private int m_firstAnswer;

	private int m_secondAnswer;

	private int m_thirdAnswer;

	private int m_fourthAnswer;

	private int m_fifthAnswer;

	private int m_sixthAnswer;

	/**
	 * @return the Id
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		m_id = id;
	}

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
		this.m_shopManagerId = shopManagerId;
	}

	/**
	 * @return the surveyDate
	 */
	public java.util.Date getSurveyDate()
	{
		return m_surveyDate;
	}

	/**
	 * @param surveyDate
	 *            the surveyDate to set
	 */
	public void setSurveyDate(java.util.Date surveyDate)
	{
		m_surveyDate = surveyDate;
	}

	/**
	 * @return the firstAnswer
	 */
	public int getFirstAnswer()
	{
		return m_firstAnswer;
	}

	/**
	 * @param firstAnswer
	 *            the firstAnswer to set
	 */
	public void setFirstAnswer(int firstAnswer)
	{
		m_firstAnswer = firstAnswer;
	}

	/**
	 * @return the secondAnswer
	 */
	public int getSecondAnswer()
	{
		return m_secondAnswer;
	}

	/**
	 * @param secondAnswer
	 *            the secondAnswer to set
	 */
	public void setSecondAnswer(int secondAnswer)
	{
		m_secondAnswer = secondAnswer;
	}

	/**
	 * @return the thirdAnswer
	 */
	public int getThirdAnswer()
	{
		return m_thirdAnswer;
	}

	/**
	 * @param thirdAnswer
	 *            the thirdAnswer to set
	 */
	public void setThirdAnswer(int thirdAnswer)
	{
		m_thirdAnswer = thirdAnswer;
	}

	/**
	 * @return the fourthAnswer
	 */
	public int getFourthAnswer()
	{
		return m_fourthAnswer;
	}

	/**
	 * @param fourthAnswer
	 *            the fourthAnswer to set
	 */
	public void setFourthAnswer(int fourthAnswer)
	{
		m_fourthAnswer = fourthAnswer;
	}

	/**
	 * @return the fifthAnswer
	 */
	public int getFifthAnswer()
	{
		return m_fifthAnswer;
	}

	/**
	 * @param fifthAnswer
	 *            the fifthAnswer to set
	 */
	public void setFifthAnswer(int fifthAnswer)
	{
		m_fifthAnswer = fifthAnswer;
	}

	/**
	 * @return the sixthAnswer
	 */
	public int getSixthAnswer()
	{
		return m_sixthAnswer;
	}

	/**
	 * @param sixthAnswer
	 *            the sixthAnswer to set
	 */
	public void setSixthAnswer(int sixthAnswer)
	{
		m_sixthAnswer = sixthAnswer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Survey [id=" + m_id + ", surveyDate=" + s_dateForamt.format(m_surveyDate) + ", shopManagerId="
				+ m_shopManagerId + ", firstAnswer=" + m_firstAnswer + ", secondAnswer=" + m_secondAnswer
				+ ", thirdAnswer=" + m_thirdAnswer + ", fourthAnswer=" + m_fourthAnswer + ", fifthAnswer="
				+ m_fifthAnswer + ", sixthAnswer=" + m_sixthAnswer + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_id;
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
		if (!(obj instanceof Survey)) {
			return false;
		}
		Survey other = (Survey) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
