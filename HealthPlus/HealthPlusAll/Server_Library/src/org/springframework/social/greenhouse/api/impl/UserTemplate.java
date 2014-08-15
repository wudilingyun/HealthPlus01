/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.greenhouse.api.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.greenhouse.api.GreenhouseProfile;
import org.springframework.social.greenhouse.api.Detail;
import org.springframework.social.greenhouse.api.DetailResponse;
import org.springframework.social.greenhouse.api.UserOperations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

/**
 * Implementation of the {@link UserOperations} interface providing binding to Greenhouse's user-oriented REST resources.
 * @author Roy Clarkson
 */
class UserTemplate extends AbstractGreenhouseOperations implements UserOperations {
	
	private final RestTemplate restTemplate;

	public UserTemplate(RestTemplate restTemplate, boolean isAuthorized, String apiUrlBase) {
		super(isAuthorized, apiUrlBase);
		this.restTemplate = restTemplate;
	}

	public long getAccountId() {
		requireAuthorization();
		return getUserProfile().getAccountId();
	}

	public String getDisplayName() {
		requireAuthorization();
		return getUserProfile().getDisplayName();
	}

	public GreenhouseProfile getUserProfile() {
		requireAuthorization();
		GreenhouseProfile profile = restTemplate.getForObject(buildUri("members/@self"), GreenhouseProfile.class);
		profile.setApiUrlBase(getApiUrlBase());
		return profile;
	}

	public GreenhouseProfile getUserProfile(long userId) {
		return restTemplate.getForObject(buildUri("members/" + userId), GreenhouseProfile.class);
	}
	
	public String getProtect(){
		requireAuthorization();
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(formData, requestHeaders);
		
		@SuppressWarnings("unchecked")
		//Map<String, Object> responseBody = restTemplate.exchange(buildUri("protect/get"), HttpMethod.POST, requestEntity, Map.class).getBody();
		//Map<String, Object> responseBody = restTemplate.exchange(buildUri( "cart/getaddress" ), HttpMethod.GET, requestEntity, Map.class).getBody();
/*		ResponseEntity<CartAddressList> responseBody = restTemplate.exchange(buildUri( "cart/getaddress" ), HttpMethod.GET, requestEntity, CartAddressList.class);
		CartAddressList cartAddressList = responseBody.getBody();
		
		for( CartAddress cartAddress : cartAddressList )
		{
			System.out.println(cartAddress.getDetail());
		}
*/
		ResponseEntity<ClientProductList> responseBody = restTemplate.exchange(buildUri( "client/6/productrecommend" ), HttpMethod.GET, requestEntity, ClientProductList.class);
		ClientProductList clientProductList = responseBody.getBody();
		return clientProductList.toString();
	}
	
	@SuppressWarnings("serial")
	private static class CartAddressList extends ArrayList<CartAddress> {}
	private static class ClientProductList extends ArrayList<ClientProduct> {}
	
	@Override
	public Detail getDetail() {
		// TODO Auto-generated method stub
		requireAuthorization();
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(formData, requestHeaders);
		Map<String, Object> responseBody = restTemplate.exchange(buildUri( "android/getprofile" ), HttpMethod.POST, requestEntity, Map.class).getBody();
		
		Detail profile = extractGetProfileResponse(responseBody);
		return profile;
	}
	
	private Detail extractGetProfileResponse(Map<String, Object> result)
	{
		Detail profile = new Detail();
		profile.setEmail((String) result.get("email"));
		profile.setGender(getIntegerValue(result, "gender"));
		profile.setHeight(getIntegerValue(result, "height"));
		profile.setMemberid(getIntegerValue(result, "memberid"));
		profile.setNickname((String) result.get("nickname"));
		profile.setPhone((String) result.get("phone"));
		profile.setUsername((String) result.get("username"));
		profile.setWeight(getFloatValue(result, "weight"));
		
		return profile;
	}
	
	private DetailResponse extractSaveProfileResponse(Map<String, Object> result)
	{
		DetailResponse profileresponse = new DetailResponse();
		profileresponse.setDescription((String)result.get("description"));
		profileresponse.setReturncode(Integer.parseInt(String.valueOf(result.get("returncode"))));
		
		return profileresponse;
	}
	
	// Retrieves object from map into an Integer, regardless of the object's actual type. Allows for flexibility in object type (eg, "3600" vs 3600).
	private Integer getIntegerValue(Map<String, Object> map, String key) {
		try {
			return Integer.valueOf(String.valueOf(map.get(key))); // normalize to String before creating integer value;			
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	// Retrieves object from map into an Long, regardless of the object's actual type. Allows for flexibility in object type (eg, "3600" vs 3600).
	private Float getFloatValue(Map<String, Object> map, String key) {
		try {
			return Float.valueOf(String.valueOf(map.get(key))); // normalize to String before creating integer value;			
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public DetailResponse saveDetail(MultiValueMap<String, String> formData) {
		// TODO Auto-generated method stub
		requireAuthorization();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
				formData, requestHeaders);
		Map<String, Object> responseBody = restTemplate.exchange(
				buildUri("android/saveprofile"), HttpMethod.POST, requestEntity,
				Map.class).getBody();
		
		DetailResponse profileresponse = extractSaveProfileResponse(responseBody);
		
		return profileresponse;
	}
}
