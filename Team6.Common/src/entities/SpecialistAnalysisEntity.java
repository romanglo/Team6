
package entities;

import java.io.Serializable;

/**
 *
 * SpecialistAnalysisEntity : Describes an entity of Specialist analysis on
 * survey.
 * 
 */
public class SpecialistAnalysisEntity implements IEntity
{

	// region Fields

	/**
	 * Serial version unique ID, necessary due to the class implements
	 * {@link Serializable}
	 */
	private static final long serialVersionUID = 4904663979714230241L;

	private int m_id;

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

	// end region -> Setters

	// region Constructors
	/**
	 * Specialist Analysis Entity Constructor
	 *
	 * @param id
	 *            the survey id to set
	 * @param analysis
	 *            the specialist analysis to set
	 */
	public SpecialistAnalysisEntity(int id, String analysis)
	{
		m_id = id;
		m_analysis = analysis;
	}

	// end region -> Constructors

	// region Public Methods

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
		result = prime * result + m_id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SpecialistAnalysisEntity)) return false;
		SpecialistAnalysisEntity other = (SpecialistAnalysisEntity) obj;
		if (m_id != other.m_id) return false;
		return true;
	}

	// end region -> Public Methods
}
