/**
 * 
 */
package com.vee.shop.bean;

import java.io.Serializable;

public class ParameterBean implements Serializable, Comparable<ParameterBean> {

	private static final long serialVersionUID = 3530043401782052821L;
	private String productparameterid;
	private String productid;
	private String parametername;
	private String parametervalue;
	private int sequence;

	public String getProductparameterid() {
		return productparameterid;
	}

	public void setProductparameterid(String productparameterid) {
		this.productparameterid = productparameterid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getParametername() {
		return parametername;
	}

	public void setParametername(String parametername) {
		this.parametername = parametername;
	}

	public String getParametervalue() {
		return parametervalue;
	}

	public void setParametervalue(String parametervalue) {
		this.parametervalue = parametervalue;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public int compareTo(ParameterBean obj) {
		return this.sequence - obj.sequence;
	}
}
