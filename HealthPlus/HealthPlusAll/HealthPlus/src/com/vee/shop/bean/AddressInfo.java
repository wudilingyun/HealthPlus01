package com.vee.shop.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class AddressInfo implements Serializable
{
	private static final long serialVersionUID = 5L;
	private String index;
	private String parentIndex;
	private String name;
	private String step;
	private String zipCode;
	private ArrayList<Integer> subIndex = new ArrayList();
	//private int subIndex[];
	//private int length = 0;
	
	public AddressInfo()
	{
		index = null;
		parentIndex = null;
		name = null;
		step = null;
		zipCode = null;
	}
	
	public AddressInfo(String index0,String parentIndex0,String name0,String step0,String zipCode0)
	{
		index = index0;
		parentIndex = parentIndex0;
		name = name0;
		step = step0;
		zipCode = zipCode0;
	}
	
	public String getIndex()
	{
		return index;
	}
	
	public String getParentIndex()
	{
		return parentIndex;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getStep()
	{
		return step;
	}
	
	public String getZipCode()
	{
		return zipCode;
	}
	
	public void setZipCode(String zipcode)
	{
		zipCode = zipcode;
	}
	
	public void addSubIndex(int i)
	{
		subIndex.add((Integer)i);
	}
	
	public ArrayList<Integer> getSubIndex()
	{
		return subIndex;
	}
	
}
    	 