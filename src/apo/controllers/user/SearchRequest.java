package apo.controllers.user;

import apo.controllers.BaseBody;

public class SearchRequest extends BaseBody
{

	private String term;
	
	public SearchRequest()
	{
		;
	}

	public String getTerm() 
	{
		return term;
	}

	public void setTerm(String term) 
	{
		this.term = term;
	}
		
}
