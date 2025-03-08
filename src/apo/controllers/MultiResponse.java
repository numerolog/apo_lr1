package apo.controllers;

import java.util.List;

public class MultiResponse<T> extends BaseBody
{

	private List<T> list;
	
	public MultiResponse(BaseBody request, List<T> list) 
	{
		super(request);
		this.list = list;
	}

	public List<T> getList() 
	{
		return list;
	}

	public void setList(List<T> list) 
	{
		this.list = list;
	}
	
}
