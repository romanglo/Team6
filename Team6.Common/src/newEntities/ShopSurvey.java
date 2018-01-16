
package newEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * ShopSurvey : A POJO to database 'surveys_in_shops' table.
 * 
 */
public class ShopSurvey implements IEntity
{

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 1106855993256507570L;

	private static final DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	private int m_id;

	private int m_surveyId;

	private int m_shopManagerId;

	private java.util.Date m_startDate;

	private int m_answer1 = 0, m_answer2 = 0, m_answer3 = 0, m_answer4 = 0, m_answer5 = 0, m_answer6 = 0;

	private int m_numberOfAnswers = 0;

	private String m_summary;

	private boolean m_closed = false;

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
	 * @return the startDate
	 */
	public java.util.Date getStartDate()
	{
		return m_startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(java.util.Date startDate)
	{
		m_startDate = startDate;
	}

	/**
	 * @return the answer1
	 */
	public int getAnswer1()
	{
		return m_answer1;
	}

	/**
	 * @param answer1
	 *            the answer1 to set
	 */
	public void setAnswer1(int answer1)
	{
		m_answer1 = answer1;
	}

	/**
	 * @return the answer2
	 */
	public int getAnswer2()
	{
		return m_answer2;
	}

	/**
	 * @param answer2
	 *            the answer2 to set
	 */
	public void setAnswer2(int answer2)
	{
		m_answer2 = answer2;
	}

	/**
	 * @return the answer3
	 */
	public int getAnswer3()
	{
		return m_answer3;
	}

	/**
	 * @param answer3
	 *            the answer3 to set
	 */
	public void setAnswer3(int answer3)
	{
		m_answer3 = answer3;
	}

	/**
	 * @return the answer4
	 */
	public int getAnswer4()
	{
		return m_answer4;
	}

	/**
	 * @param answer4
	 *            the answer4 to set
	 */
	public void setAnswer4(int answer4)
	{
		m_answer4 = answer4;
	}

	/**
	 * @return the answer5
	 */
	public int getAnswer5()
	{
		return m_answer5;
	}

	/**
	 * @param answer5
	 *            the answer5 to set
	 */
	public void setAnswer5(int answer5)
	{
		m_answer5 = answer5;
	}

	/**
	 * @return the answer6
	 */
	public int getAnswer6()
	{
		return m_answer6;
	}

	/**
	 * @param answer6
	 *            the answer6 to set
	 */
	public void setAnswer6(int answer6)
	{
		m_answer6 = answer6;
	}

	/**
	 * @return the numberOfAnswers
	 */
	public int getNumberOfAnswers()
	{
		return m_numberOfAnswers;
	}

	/**
	 * @param numberOfAnswers
	 *            the numberOfAnswers to set
	 */
	public void setNumberOfAnswers(int numberOfAnswers)
	{
		m_numberOfAnswers = numberOfAnswers;
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
	 * @return the closed
	 */
	public boolean isClosed()
	{
		return m_closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(boolean closed)
	{
		m_closed = closed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ShopSurvey [id=" + m_id + ", surveyId=" + m_surveyId + ", shopManagerId=" + m_shopManagerId
				+ ", startDate=" + s_dateForamt.format(m_startDate) + ", answer1=" + m_answer1 + ", answer2="
				+ m_answer2 + ", answer3=" + m_answer3 + ", answer4=" + m_answer4 + ", answer5=" + m_answer5
				+ ", answer6=" + m_answer6 + ", numberOfAnswers=" + m_numberOfAnswers + ", summary=" + m_summary
				+ ", closed=" + m_closed + "]";
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
		if (!(obj instanceof ShopSurvey)) {
			return false;
		}
		ShopSurvey other = (ShopSurvey) obj;
		if (m_id != other.m_id) {
			return false;
		}
		return true;
	}

}
