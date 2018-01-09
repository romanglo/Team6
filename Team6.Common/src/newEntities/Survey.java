
package newEntities;

import java.io.Serializable;

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

	private int m_id;

	private String m_firstQuestion;

	private String m_secondQuestion;

	private String m_thirdQuestion;

	private String m_fourthQuestion;

	private String m_fifthQuestion;

	private String m_sixthQuestion;

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
	 * @return the firstQuestion
	 */
	public String getFirstQuestion()
	{
		return m_firstQuestion;
	}

	/**
	 * @param firstQuestion
	 *            the firstQuestion to set
	 */
	public void setFirstQuestion(String firstQuestion)
	{
		m_firstQuestion = firstQuestion;
	}

	/**
	 * @return the secondQuestion
	 */
	public String getSecondQuestion()
	{
		return m_secondQuestion;
	}

	/**
	 * @param secondQuestion
	 *            the secondQuestion to set
	 */
	public void setSecondQuestion(String secondQuestion)
	{
		m_secondQuestion = secondQuestion;
	}

	/**
	 * @return the thirdQuestion
	 */
	public String getThirdQuestion()
	{
		return m_thirdQuestion;
	}

	/**
	 * @param thirdQuestion
	 *            the thirdQuestion to set
	 */
	public void setThirdQuestion(String thirdQuestion)
	{
		m_thirdQuestion = thirdQuestion;
	}

	/**
	 * @return the fourthQuestion
	 */
	public String getFourthQuestion()
	{
		return m_fourthQuestion;
	}

	/**
	 * @param fourthQuestion
	 *            the fourthQuestion to set
	 */
	public void setFourthQuestion(String fourthQuestion)
	{
		m_fourthQuestion = fourthQuestion;
	}

	/**
	 * @return the fifthQuestion
	 */
	public String getFifthQuestion()
	{
		return m_fifthQuestion;
	}

	/**
	 * @param fifthQuestion
	 *            the fifthQuestion to set
	 */
	public void setFifthQuestion(String fifthQuestion)
	{
		m_fifthQuestion = fifthQuestion;
	}

	/**
	 * @return the sixthQuestion
	 */
	public String getSixthQuestion()
	{
		return m_sixthQuestion;
	}

	/**
	 * @param sixthQuestion
	 *            the sixthQuestion to set
	 */
	public void setSixthQuestion(String sixthQuestion)
	{
		m_sixthQuestion = sixthQuestion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Survey [id=" + m_id + "firstQuestion=" + m_firstQuestion + ", secondQuestion=" + m_secondQuestion
				+ ", thirdQuestion=" + m_thirdQuestion + ", fourthQuestion=" + m_fourthQuestion + ", fifthQuestion="
				+ m_fifthQuestion + ", sixthQuestion=" + m_sixthQuestion + "]";
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
