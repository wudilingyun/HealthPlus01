package org.springframework.social.greenhouse.api;

public class DetailResponse
{
	private String description;
	private int returncode;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the returncode
	 */
	public int getReturncode() {
		return returncode;
	}
	/**
	 * @param returncode the returncode to set
	 */
	public void setReturncode(int returncode) {
		this.returncode = returncode;
	}
}
