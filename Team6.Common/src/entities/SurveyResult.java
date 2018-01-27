
package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * ShopSurvey : A POJO to database 'survey_result' table.
 * 
 */
public class SurveyResult implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 1106855993256507570L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private int m_id;

	private int m_surveyId;

	private java.util.Date m_enterDate;

	private int m_firstAnswer, m_secondAnswer, m_thirdAnswer, m_fourthAnswer, m_fifthAnswer, m_sixthAnswer;

	private String m_summary;

	/**
	 * @return the id
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
	 * @return the surveyId
	 */
	public int getSurveyId()
	{
		return m_surveyId;
	}

	/**
	 * @param surveyId
	 *            the surveyId to set
	 */
	public void setSurveyId(int surveyId)
	{
		m_surveyId = surveyId;
	}

	/**
	 * @return the enterDate
	 */
	public java.util.Date getEnterDate()
	{
		return m_enterDate;
	}

	/**
	 * @param enterDate
	 *            the enterDate to set
	 */
	public void setEnterDate(java.util.Date enterDate)
	{
		m_enterDate = enterDate;
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
	 * @param fourthanswer
	 *            the fourthanswer to set
	 */
	public void setFourthanswer(int fourthanswer)
	{
		m_fourthAnswer = fourthanswer;
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
	 * @return the summary
	 */
	public String getSummary()
	{
		return m_summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary)
	{
		m_summary = summary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopSurvey [id=" + m_id + ", surveyId=" + m_surveyId + ", enterDate=" + s_dateForamt.format(m_enterDate)
				+ ", firstAnswer=" + m_firstAnswer + ", secondAnswer=" + m_secondAnswer + ", thirdAnswer="
				+ m_thirdAnswer + ", fourthAnswer=" + m_fourthAnswer + ", fifthAnswer=" + m_fifthAnswer
				+ ", sixthAnswer=" + m_sixthAnswer + ", summary=" + m_summary + "]";
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
		if (!(obj instanceof SurveyResult)) {
			return false;
		}
		SurveyResult other = (SurveyResult) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
