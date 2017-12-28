package entities;

import java.io.Serializable;

public class SpecialistAnalysisEntity implements IEntity
{
	// region Fields
	/**
	 * Serial version unique ID, necessary due to the class implements {@link Serializable
	 */
	@SuppressWarnings("javadoc") private static final long serialVersionUID = 1L;
	
	private int survey_id;
	
	private String specialist_analysis;

	// end region -> Fields
	
	// region Getters
	
	/**
	 * @return An unique ID of the product.
	 */
	public int getSurveyId()
	{
		return survey_id;
	}

	/**
	 * @return The name of the product.
	 */
	public String getSpecialistAnalysis()
	{
		return specialist_analysis;
	}

	// end region -> Getters

	// region Setters
	
	/**
	 * Set survey id
	 *
	 * @param survey_id
	 *            the survey_id to set
	 */
	public void setSurveyId(int survey_id)
	{
		this.survey_id = survey_id;
	}

	/**
	 * set specialist analysis complaint
	 *
	 * @param specialist_analysis
	 *            the specialist_analysis to set
	 */
	public void setName(String specialist_analysis)
	{
		this.specialist_analysis = specialist_analysis;
	}
	
	// end region -> Setters

	// region Constructors
	/**
	 * Complaint Entity Constructor
	 *
	 * @param survey_id
	 * 			the survey id to set
	 * @param specialist_analysis
	 * 			the specialist analysis to set
	 */
	public SpecialistAnalysisEntity(int survey_id, String specialist_analysis)
	{
		this.survey_id=survey_id;
		this.specialist_analysis=specialist_analysis;
	}
	
	// end region -> Constructors

	// region Public Methods
	
	@Override
	public String toString()
	{
		return "ProductEntity [Costumer ID=" + survey_id + ", Costumer Complaint=" + specialist_analysis + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + survey_id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SpecialistAnalysisEntity)) return false;
		SpecialistAnalysisEntity other = (SpecialistAnalysisEntity) obj;
		if (specialist_analysis == null) {
			if (other.specialist_analysis != null) return false;
		} else if (!specialist_analysis.equals(other.specialist_analysis)) return false;
		if (survey_id != other.survey_id) return false;
		return true;
	}
	
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
