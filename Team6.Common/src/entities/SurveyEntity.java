package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * SpecialistAnalysisEntity : Describes an entity of Specialist analysis on
 * survey.
 * 
 */
public class SurveyEntity implements IEntity
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4904663979714230241L;

	private int m_id;
	
	private ArrayList<AnswersEntity> m_answers;
	
	private String[] m_question=new String[6];

	private String m_analysis;

	// end region -> Fields

	// region Getters

	/**
	 * @return An unique ID of the product.
	 */
	public int getSurveyId()
	{
		return m_id;
	}

	/**
	 * @return The name of the product.
	 */
	public String getSpecialistAnalysis()
	{
		return m_analysis;
	}
	
	/**
	 *
	 * @return
	 * 		int array of answers
	 */
	public ArrayList<AnswersEntity> getAnswers()
	{
		return m_answers;
	}
	
	/**
	 * @return
	 * 		String array of question
	 */
	public String[] getQuestion()
	{
		return m_question;
	}
	
	// end region -> Getters

	// region Setters

	/**
	 * Set survey id
	 *
	 * @param id
	 *            the id to set
	 */
	public void setSurveyId(int id)
	{
		m_id = id;
	}

	/**
	 * set specialist analysis
	 *
	 * @param analysis
	 *            the analysis to set
	 */
	public void setAnalysis(String analysis)
	{
		m_analysis = analysis;
	}
	
	/**
	 * @param m_question
	 * 				question array to set.
	 */
	public void setQuestion(String[] m_question)
	{
		this.m_question=m_question;
	}
	
	/**
	 * @param m_answers
	 * 				answers array to set.
	 */
	public void setAnswers(ArrayList<AnswersEntity> m_answers)
	{
		this.m_answers=m_answers;
	}


	// end region -> Setters

	// region Constructors
	/**
	 * SurveyEntity Constructor
	 *
	 * @param m_question
	 * 				Qustions array
	 * @param m_answers
	 * 				Answers array
	 */
	public SurveyEntity( String[] m_question)
	{
		this.m_question=m_question;
		this.m_answers = new ArrayList<>();
	}
	
	/**
	 * Survey Entity Constructor - in order to send message get. 
	 *
	 * @param id
	 * 		Survey id.
	 */		
	public SurveyEntity(int id)
	{
		this.m_id=id;
	}
	// end region -> Constructors

	// region Public Methods

	
	public void AddAnswers(AnswersEntity ans_entity)
	{
		m_answers.add(ans_entity);
	}
	
	@Override
	public String toString()
	{
		return "SpecialistAnalysisEntity [ID=" + m_id + ", Analysis=" + m_analysis + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_analysis == null) ? 0 : m_analysis.hashCode());
		result = prime * result + ((m_answers == null) ? 0 : m_answers.hashCode());
		result = prime * result + m_id;
		result = prime * result + Arrays.hashCode(m_question);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SurveyEntity)) return false;
		SurveyEntity other = (SurveyEntity) obj;
		if (m_analysis == null) {
			if (other.m_analysis != null) return false;
		} else if (!m_analysis.equals(other.m_analysis)) return false;
		if (m_answers == null) {
			if (other.m_answers != null) return false;
		} else if (!m_answers.equals(other.m_answers)) return false;
		if (m_id != other.m_id) return false;
		if (!Arrays.equals(m_question, other.m_question)) return false;
		return true;
	}





	// end region -> Public Methods
}
