package entities;


public class AnswersEntity
{

	// region Fields
		private int m_answer1;
		private int m_answer2;
		private int m_answer3;
		private int m_answer4;
		private int m_answer5;
		private int m_answer6;

	// end region -> Fields
		
	// region Getters
		
		/**
		 * @return
		 * 		answer 1.
		 */
		public int getAnswer1()
		{
			return m_answer1;
		}
		
		/**
		 * @return
		 * 		answer 2.
		 */
		public int getAnswer2()
		{
			return m_answer2;
		}
		
		/**
		 * @return
		 * 		answer 3.
		 */
		public int getAnswer3()
		{
			return m_answer3;
		}
		
		/**
		 * @return
		 * 		answer 4.
		 */
		public int getAnswer4()
		{
			return m_answer4;
		}
		
		/**
		 * @return
		 * 		answer 5.
		 */
		public int getAnswer5()
		{
			return m_answer5;
		}
		
		/**
		 * @return
		 * 		answer 6.
		 */
		public int getAnswer6()
		{
			return m_answer6;
		}
				

	// end region -> Getters

	// region Setters
		
		/**
		 * @param m_answer1
		 * 				answer 1 to set.
		 */				
		public void setAnswer1(int m_answer1)
		{
			this.m_answer1 = m_answer1;
		}

		/**
		 * @param m_answer2
		 * 				answer 2 to set.
		 */				
		public void setAnswer2(int m_answer2)
		{
			this.m_answer2 = m_answer2;
		}
		
		/**
		 * @param m_answer3
		 * 				answer 3 to set.
		 */				
		public void setAnswer3(int m_answer3)
		{
			this.m_answer3 = m_answer3;
		}
		
		/**
		 * @param m_answer4
		 * 				answer 4 to set.
		 */				
		public void setAnswer4(int m_answer4)
		{
			this.m_answer4 = m_answer4;
		}
		
		/**
		 * @param m_answer5
		 * 				answer 5 to set.
		 */				
		public void setAnswer5(int m_answer5)
		{
			this.m_answer5 = m_answer5;
		}
		
		/**
		 * @param m_answer6
		 * 				answer 6 to set.
		 */				
		public void setAnswer6(int m_answer6)
		{
			this.m_answer6 = m_answer6;
		}
		

	// end region -> Setters

	// region Constructors
		
		/**
		 * AnswerEntity Constructor
		 *
		 * @param ans1
		 * 			answer 1.
		 * @param ans2
		 * 			answer 2.
		 * @param ans3
		 * 			answer 3.
		 * @param ans4
		 * 			answer 4.
		 * @param ans5
		 * 			answer 5.
		 * @param ans6
		 * 			answer 6.
		 */
		public AnswersEntity(int ans1,int ans2,int ans3,int ans4,int ans5,int ans6)
		{
			m_answer1=ans1;
			m_answer2=ans2;
			m_answer3=ans3;
			m_answer4=ans4;
			m_answer5=ans5;
			m_answer6=ans6;
		}


	// end region -> Constructors

	// region Public Methods
		
		@Override
		public String toString()
		{
			return "AnswersEntity [answer1=" + m_answer1 + ", answer2=" + m_answer2 + ", answer3=" + m_answer3
					+ ", answer4=" + m_answer4 + ", answer5=" + m_answer5 + ", answer6=" + m_answer6 + "]";
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + m_answer1;
			result = prime * result + m_answer2;
			result = prime * result + m_answer3;
			result = prime * result + m_answer4;
			result = prime * result + m_answer5;
			result = prime * result + m_answer6;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof AnswersEntity)) return false;
			AnswersEntity other = (AnswersEntity) obj;
			if (m_answer1 != other.m_answer1) return false;
			if (m_answer2 != other.m_answer2) return false;
			if (m_answer3 != other.m_answer3) return false;
			if (m_answer4 != other.m_answer4) return false;
			if (m_answer5 != other.m_answer5) return false;
			if (m_answer6 != other.m_answer6) return false;
			return true;
		}
		
		
		
	// end region -> Public Methods

	// region Private Methods
	// end region -> Private Methods
}
