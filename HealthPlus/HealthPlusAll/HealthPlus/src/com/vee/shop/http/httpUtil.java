/**
 * 
 */
package com.vee.shop.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.vee.shop.bean.AddressBean;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.CategoryBean;
import com.vee.shop.bean.ImageBean;
import com.vee.shop.bean.OrderBean;
import com.vee.shop.bean.OrderDetailBean;
import com.vee.shop.bean.ParameterBean;
import com.vee.shop.bean.ProductBean;
import com.vee.shop.bean.ProductDetailBean;
import com.vee.shop.bean.QueryOrderBean;
import com.vee.shop.bean.StationBean;
import com.vee.shop.util.Constants;
import com.vee.shop.util.LogUtil;

public class httpUtil {

	private static final String TAG = "httpUtil";
	private static final String ALLSYSTEMSETTING_PREFERENCES = "systemsetting";
	private static SharedPreferences settings;
	public static String uuid;
	public static String userAgent;

	public static void initUUID(Context context) {
		if (settings == null) {
			settings = context.getSharedPreferences(
					ALLSYSTEMSETTING_PREFERENCES, Context.MODE_PRIVATE);
		}
		uuid = settings.getString("uuid", null);
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("uuid", uuid);
			editor.commit();
		}
		userAgent = Constants.PRE_USER_AGENT + "UUID/" + uuid;
	}

	public static String getPostJsonString(String actionUrl,
			List<NameValuePair> params) {
		String result;
		HttpPost httpRequest = new HttpPost(actionUrl);
		// LogUtil.d(TAG, "post url: " + "\r\n" +actionUrl);
		try {
			if (null != params) {
				HttpEntity httpentity = new UrlEncodedFormEntity(params,
						"utf-8");
				httpRequest.setEntity(httpentity);
			}
			httpRequest.setHeader("User-Agent", userAgent);

			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// read timeout
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.SO_TIMEOUT, 5000);
			// connect timeout
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
			// 取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// HttpStatus.SC_OK表示连接成功
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// 取得返回的字符串
				result = EntityUtils.toString(httpResponse.getEntity());
				if (result.equals("[]")) {
					result = "null";
				}
				// LogUtil.d(TAG, "get post result\r\n"+result);
			} else {
				result = "null";
				LogUtil.d(TAG, "post error\r\n" + statusCode + "\r\n"
						+ EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (Exception e) {
			LogUtil.d(TAG, "catch post exception");
			result = "null";
			e.printStackTrace();
		}
		return result;
	}

	public static String getJsonString(String actionUrl,
			List<NameValuePair> params) {
		String result;
		String url = actionUrl;
		if (null == url)
			return null;
		if (null != params) {
			StringBuilder sbBuilder = new StringBuilder();
			for (int i = 0; i < params.size(); i++) {
				if (i == 0) {
					sbBuilder.append("?");
				} else {
					sbBuilder.append("&");
				}
				sbBuilder.append(params.get(i).getName());
				sbBuilder.append("=");
				sbBuilder.append(params.get(i).getValue());
			}
			url = url + sbBuilder.toString();
		}
		HttpGet httpGet = new HttpGet(url);
		try {

			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// read timeout
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.SO_TIMEOUT, 5000);
			// connect timeout
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
			httpGet.setHeader("User-Agent", userAgent);
			// 取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpGet);
			// HttpStatus.SC_OK表示连接成功
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// 取得返回的字符串
				// result = EntityUtils.toString(httpResponse.getEntity());
				result = EntityUtils
						.toString(httpResponse.getEntity(), "UTF-8");
				// if (result.equals("[]")) {
				// result = "null";
				// }
			} else {
				result = "null";
				LogUtil.d(TAG, "httpget fail\r\n" + statusCode + "\r\n"
						+ EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (Exception e) {
			LogUtil.d(TAG, "httpget exception");
			result = "null";
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<CategoryBean> parseCategoryList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		try {
			JSONArray jsonArray_allCategory = new JSONArray(json);
			CategoryBean categoryBean;
			for (int i = 0; i < jsonArray_allCategory.length(); i++) {
				JSONObject jo_category = jsonArray_allCategory.getJSONObject(i);
				categoryBean = new CategoryBean();
				if (null == jo_category.getString("productcategoryurl")) {
					categoryBean.setUrl(null);
				} else {
					categoryBean.setUrl(jo_category
							.getString("productcategoryurl"));
				}

				if (null == jo_category.getString("productcategoryimgurl")) {
					categoryBean.setImgUrl(null);
				} else {
					categoryBean.setImgUrl(jo_category
							.getString("productcategoryimgurl"));
				}

				if (null == jo_category.getString("productcategoryname")) {
					categoryBean.setName(null);
				} else {
					categoryBean.setName(jo_category
							.getString("productcategoryname"));
				}

				if (null == jo_category.getString("productcategoryid")) {
					categoryBean.setId(null);
				} else {
					categoryBean.setId(jo_category
							.getString("productcategoryid"));
				}
				listCategoryBean.add(categoryBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		/*
		 * for (int i = 0; i < listCategoryBean.size(); i++) { LogUtil.d(TAG,
		 * listCategoryBean.get(i).getUrl()); LogUtil.d(TAG,
		 * listCategoryBean.get(i).getImgUrl()); LogUtil.d(TAG,
		 * listCategoryBean.get(i).getName()); LogUtil.d(TAG,
		 * listCategoryBean.get(i).getId()); }
		 */
		return listCategoryBean;
	}

	public static ArrayList<ProductBean> parseProductList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<ProductBean> listProductBean = new ArrayList<ProductBean>();
		try {
			JSONArray jsonArray_allCategory = new JSONArray(json);
			ProductBean productBean;
			for (int i = 0; i < jsonArray_allCategory.length(); i++) {
				JSONObject jo_category = jsonArray_allCategory.getJSONObject(i);
				productBean = new ProductBean();
				if (null == jo_category.getString("producturl")) {
					productBean.setUrl(null);
				} else {
					productBean.setUrl(jo_category.getString("producturl"));
				}

				if (null == jo_category.getString("productimgurl")) {
					productBean.setImgUrl(null);
				} else {
					productBean.setImgUrl(jo_category
							.getString("productimgurl"));
				}

				if (null == jo_category.getString("title")) {
					productBean.setName(null);
				} else {
					productBean.setName(jo_category.getString("title"));
				}

				if (null == jo_category.getString("price")) {
					productBean.setPrice(null);
				} else {
					productBean.setPrice(jo_category.getString("price"));
				}

				if (null == jo_category.getString("desc")) {
					productBean.setDesc(null);
				} else {
					productBean.setDesc(jo_category.getString("desc"));
				}

				if (null == jo_category.getString("productid")) {
					productBean.setId(null);
				} else {
					productBean.setId(jo_category.getString("productid"));
				}

				if (null == jo_category.getString("producthotimgurl")) {
					productBean.setHotImgUrl(null);
				} else {
					productBean.setHotImgUrl(jo_category
							.getString("producthotimgurl"));
				}
				listProductBean.add(productBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * for (int i = 0; i < listProductBean.size(); i++) { LogUtil.d(TAG,
		 * listProductBean.get(i).getUrl()); LogUtil.d(TAG,
		 * listProductBean.get(i).getImgUrl()); LogUtil.d(TAG,
		 * listProductBean.get(i).getName()); LogUtil.d(TAG,
		 * listProductBean.get(i).getPrice()); LogUtil.d(TAG,
		 * listProductBean.get(i).getDesc()); LogUtil.d(TAG,
		 * listProductBean.get(i).getId()); }
		 */

		return listProductBean;
	}

	// public static ProductDetailBean parseProductDetail(String json) {
	// if (json == null || "".equals(json.trim())
	// || "null".equals(json.trim()))
	// return null;
	// ProductDetailBean productDetailBean = new ProductDetailBean();
	// try {
	// JSONObject jo_product = new JSONObject(json);
	// if (null == jo_product.getString("title")) {
	// productDetailBean.setName(null);
	// } else {
	// productDetailBean.setName(jo_product.getString("title"));
	// }
	// if (null == jo_product.getString("price")) {
	// productDetailBean.setPrice(null);
	// } else {
	// productDetailBean.setPrice(jo_product.getString("price"));
	// }
	// if (null == jo_product.getString("originalprice")) {
	// productDetailBean.setOriginalprice(null);
	// } else {
	// productDetailBean.setOriginalprice(jo_product
	// .getString("originalprice"));
	// }
	// if (null == jo_product.getString("availablecount")) {
	// productDetailBean.setAvailablecount(null);
	// } else {
	// productDetailBean.setAvailablecount(jo_product
	// .getString("availablecount"));
	// }
	// if (null == jo_product.getString("productid")) {
	// productDetailBean.setId(null);
	// } else {
	// productDetailBean.setId(jo_product.getString("productid"));
	// }
	// if (null == jo_product.getString("addcarturl")) {
	// productDetailBean.setAddCharUrl(null);
	// } else {
	// productDetailBean.setAddCharUrl(jo_product
	// .getString("addcarturl"));
	// }
	// if (null == jo_product.getString("score")) {
	// productDetailBean.setScore(null);
	// } else {
	// productDetailBean.setScore(jo_product.getString("score"));
	// }
	// if (!(jo_product.get("listimage") instanceof JSONArray)) {
	// productDetailBean.setImgList(null);
	// } else {
	// JSONArray ja_imglistArray = jo_product
	// .getJSONArray("listimage");
	// for (int i = 0; i < ja_imglistArray.length(); i++) {
	// productDetailBean.getImgList().add(
	// ja_imglistArray.getString(i));
	// }
	// }
	// if (!(jo_product.get("suitable") instanceof JSONArray)) {
	// productDetailBean.setSuitableList(null);
	// } else {
	// JSONArray ja_suitlistArray = jo_product
	// .getJSONArray("suitable");
	// for (int i = 0; i < ja_suitlistArray.length(); i++) {
	// productDetailBean.getSuitableList().add(
	// ja_suitlistArray.getString(i));
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// return null;
	// }
	//
	// return productDetailBean;
	// }

	public static ProductDetailBean parseProductDetail(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ProductDetailBean productDetailBean = new ProductDetailBean();
		try {
			JSONObject jo_product = new JSONObject(json);
			if (null == jo_product.getString("title")) {
				productDetailBean.setName(null);
			} else {
				productDetailBean.setName(jo_product.getString("title"));
			}
			if (null == jo_product.getString("price")) {
				productDetailBean.setPrice(null);
			} else {
				productDetailBean.setPrice(jo_product.getString("price"));
			}
			if (null == jo_product.getString("originalprice")) {
				productDetailBean.setOriginalprice(null);
			} else {
				productDetailBean.setOriginalprice(jo_product
						.getString("originalprice"));
			}
			if (null == jo_product.getString("availablecount")) {
				productDetailBean.setAvailablecount(null);
			} else {
				productDetailBean.setAvailablecount(jo_product
						.getString("availablecount"));
			}
			if (null == jo_product.getString("productid")) {
				productDetailBean.setId(null);
			} else {
				productDetailBean.setId(jo_product.getString("productid"));
			}
			if (null == jo_product.getString("addcarturl")) {
				productDetailBean.setAddCharUrl(null);
			} else {
				productDetailBean.setAddCharUrl(jo_product
						.getString("addcarturl"));
			}
			if (null == jo_product.getString("score")) {
				productDetailBean.setScore(null);
			} else {
				productDetailBean.setScore(jo_product.getString("score"));
			}
			if (!(jo_product.get("imagelist") instanceof JSONArray)) {
				productDetailBean.setImageList(null);
			} else {
				JSONArray ja_imglistArray = jo_product
						.getJSONArray("imagelist");
				if (ja_imglistArray.length() > 0) {
					for (int i = 0; i < ja_imglistArray.length(); i++) {
						JSONObject jo_imgbean = ja_imglistArray
								.getJSONObject(i);
						ImageBean imgBean = new ImageBean();
						if (null == jo_imgbean.getString("type")) {
							imgBean.setType(null);
						} else {
							imgBean.setType(jo_imgbean.getString("type"));
						}
						if (null == jo_imgbean.getString("imageurl")) {
							imgBean.setImageurl(null);
						} else {
							imgBean.setImageurl(jo_imgbean
									.getString("imageurl"));
						}
						if (null == jo_imgbean.getString("sequence")) {
							imgBean.setSequence(-1);
						} else {
							imgBean.setSequence(Integer.parseInt(jo_imgbean
									.getString("sequence")));
						}
						productDetailBean.getImageList().add(imgBean);
					}
					Collections.sort(productDetailBean.getImageList());
				} else {
					productDetailBean.setImageList(null);
				}
			}
			if (!(jo_product.get("parameterlist") instanceof JSONArray)) {
				productDetailBean.setParaList(null);
			} else {
				JSONArray ja_parlistArray = jo_product
						.getJSONArray("parameterlist");
				if (ja_parlistArray.length() > 0) {
					for (int i = 0; i < ja_parlistArray.length(); i++) {
						JSONObject jo_parbean = ja_parlistArray
								.getJSONObject(i);
						ParameterBean parBean = new ParameterBean();
						if (null == jo_parbean.getString("parametername")) {
							parBean.setParametername(null);
						} else {
							parBean.setParametername(jo_parbean
									.getString("parametername"));
						}
						if (null == jo_parbean.getString("parametervalue")) {
							parBean.setParametervalue(null);
						} else {
							parBean.setParametervalue(jo_parbean
									.getString("parametervalue"));
						}
						if (null == jo_parbean.getString("sequence")) {
							parBean.setSequence(-1);
						} else {
							parBean.setSequence(Integer.parseInt(jo_parbean
									.getString("sequence")));
						}
						productDetailBean.getParaList().add(parBean);
					}
					Collections.sort(productDetailBean.getParaList());
				} else {
					productDetailBean.setParaList(null);
				}
			}
			if (!(jo_product.get("suitable") instanceof JSONArray)) {
				productDetailBean.setSuitableList(null);
			} else {
				JSONArray ja_suitlistArray = jo_product
						.getJSONArray("suitable");
				for (int i = 0; i < ja_suitlistArray.length(); i++) {
					productDetailBean.getSuitableList().add(
							ja_suitlistArray.getString(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return productDetailBean;
	}

	public static ArrayList<CartItemBean> parseCartList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<CartItemBean> listCartBean = new ArrayList<CartItemBean>();
		try {
			JSONObject jo_allCart = new JSONObject(json);
			JSONArray jsonArray_allCart = jo_allCart
					.getJSONArray("listCartProduct");
			CartItemBean cartItemBean;
			for (int i = 0; i < jsonArray_allCart.length(); i++) {
				JSONObject jo_cart = jsonArray_allCart.getJSONObject(i);
				cartItemBean = new CartItemBean();
				if (null == jo_cart.getString("productid")) {
					cartItemBean.setId(null);
				} else {
					cartItemBean.setId(jo_cart.getString("productid"));
				}
				if (null == jo_cart.getString("productimgurl")) {
					cartItemBean.setImgUrl(null);
				} else {
					cartItemBean.setImgUrl(jo_cart.getString("productimgurl"));
				}
				if (null == jo_cart.getString("producttitle")) {
					cartItemBean.setName(null);
				} else {
					cartItemBean.setName(jo_cart.getString("producttitle"));
				}
				if (null == jo_cart.getString("price")) {
					cartItemBean.setPrice(null);
				} else {
					cartItemBean.setPrice(jo_cart.getString("price"));
				}
				if (null == jo_cart.getString("count")) {
					cartItemBean.setCount(null);
				} else {
					cartItemBean.setCount(jo_cart.getString("count"));
				}
				if (null == jo_cart.getString("availablecount")) {
					cartItemBean.setAvailablecount(null);
				} else {
					cartItemBean.setAvailablecount(jo_cart
							.getString("availablecount"));
				}
				if (!cartItemBean.getCount().equals("0"))
					listCartBean.add(cartItemBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * for (int i = 0; i < listCartBean.size(); i++) { LogUtil.d(TAG,
		 * listCartBean.get(i).getName()); LogUtil.d(TAG,
		 * listCartBean.get(i).getId()); LogUtil.d(TAG,
		 * listCartBean.get(i).getCount()); LogUtil.d(TAG,
		 * listCartBean.get(i).getPrice()); LogUtil.d(TAG,
		 * listCartBean.get(i).getAvailablecount()); }
		 */

		return listCartBean;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<CartItemBean> parseCartListNew(
			Map<String, Object> result) {
		if (result == null
				|| (result.get("listCartProduct").toString()).equals("[]"))
			return null;

		ArrayList<CartItemBean> listCartBean = new ArrayList<CartItemBean>();
		try {
			List<Map<String, Object>> list_allCart = (List<Map<String, Object>>) result
					.get("listCartProduct");
			CartItemBean cartItemBean;
			for (int i = 0; i < list_allCart.size(); i++) {
				Map<String, Object> jo_cart = list_allCart.get(i);
				cartItemBean = new CartItemBean();
				if (null == jo_cart.get("productid")) {
					cartItemBean.setId(null);
				} else {
					cartItemBean.setId(jo_cart.get("productid").toString());
				}
				if (null == jo_cart.get("productimgurl")) {
					cartItemBean.setImgUrl(null);
				} else {
					cartItemBean.setImgUrl(jo_cart.get("productimgurl")
							.toString());
				}
				if (null == jo_cart.get("producttitle")) {
					cartItemBean.setName(null);
				} else {
					cartItemBean
							.setName(jo_cart.get("producttitle").toString());
				}
				if (null == jo_cart.get("price")) {
					cartItemBean.setPrice(null);
				} else {
					cartItemBean.setPrice(jo_cart.get("price").toString());
				}
				if (null == jo_cart.get("count")) {
					cartItemBean.setCount(null);
				} else {
					cartItemBean.setCount(jo_cart.get("count").toString());
				}
				if (null == jo_cart.get("availablecount")) {
					cartItemBean.setAvailablecount(null);
				} else {
					cartItemBean.setAvailablecount(jo_cart
							.get("availablecount").toString());
				}
				if (!cartItemBean.getCount().equals("0"))
					listCartBean.add(cartItemBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * for (int i = 0; i < listCartBean.size(); i++) { LogUtil.d(TAG,
		 * listCartBean.get(i).getName()); LogUtil.d(TAG,
		 * listCartBean.get(i).getId()); LogUtil.d(TAG,
		 * listCartBean.get(i).getCount()); LogUtil.d(TAG,
		 * listCartBean.get(i).getPrice()); LogUtil.d(TAG,
		 * listCartBean.get(i).getAvailablecount()); }
		 */

		return listCartBean;
	}

	public static HashMap<String, CartItemBean> parseCartMap(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		HashMap<String, CartItemBean> cartMap = new HashMap<String, CartItemBean>();
		try {
			JSONObject jo_allCart = new JSONObject(json);
			JSONArray jsonArray_allCart = jo_allCart
					.getJSONArray("listCartProduct");
			CartItemBean cartItemBean;
			for (int i = 0; i < jsonArray_allCart.length(); i++) {
				JSONObject jo_cart = jsonArray_allCart.getJSONObject(i);
				cartItemBean = new CartItemBean();
				if (null == jo_cart.getString("productid")) {
					cartItemBean.setId(null);
				} else {
					cartItemBean.setId(jo_cart.getString("productid"));
				}
				if (null == jo_cart.getString("productimgurl")) {
					cartItemBean.setImgUrl(null);
				} else {
					cartItemBean.setImgUrl(jo_cart.getString("productimgurl"));
				}
				if (null == jo_cart.getString("producttitle")) {
					cartItemBean.setName(null);
				} else {
					cartItemBean.setName(jo_cart.getString("producttitle"));
				}
				if (null == jo_cart.getString("price")) {
					cartItemBean.setPrice(null);
				} else {
					cartItemBean.setPrice(jo_cart.getString("price"));
				}
				if (null == jo_cart.getString("count")) {
					cartItemBean.setCount(null);
				} else {
					cartItemBean.setCount(jo_cart.getString("count"));
				}
				if (null == jo_cart.getString("availablecount")) {
					cartItemBean.setAvailablecount(null);
				} else {
					cartItemBean.setAvailablecount(jo_cart
							.getString("availablecount"));
				}
				if (!cartItemBean.getCount().equals("0"))
					cartMap.put(cartItemBean.getId(), cartItemBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		/*
		 * for (int i = 0; i < listCartBean.size(); i++) { LogUtil.d(TAG,
		 * listCartBean.get(i).getName()); LogUtil.d(TAG,
		 * listCartBean.get(i).getId()); LogUtil.d(TAG,
		 * listCartBean.get(i).getCount()); LogUtil.d(TAG,
		 * listCartBean.get(i).getPrice()); LogUtil.d(TAG,
		 * listCartBean.get(i).getAvailablecount()); }
		 */
		return cartMap;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, CartItemBean> parseCartMapNew(
			Map<String, Object> result) {
		if (result == null
				|| (result.get("listCartProduct").toString()).equals("[]"))
			return null;
		HashMap<String, CartItemBean> cartMap = new HashMap<String, CartItemBean>();
		try {
			List<Map<String, Object>> list_allCart = (List<Map<String, Object>>) result
					.get("listCartProduct");
			CartItemBean cartItemBean;
			for (int i = 0; i < list_allCart.size(); i++) {
				Map<String, Object> mp_cart = list_allCart.get(i);
				cartItemBean = new CartItemBean();
				if (null == mp_cart.get("productid")) {
					cartItemBean.setId(null);
				} else {
					cartItemBean.setId(mp_cart.get("productid").toString());
				}
				if (null == mp_cart.get("productimgurl")) {
					cartItemBean.setImgUrl(null);
				} else {
					cartItemBean.setImgUrl(mp_cart.get("productimgurl")
							.toString());
				}
				if (null == mp_cart.get("producttitle")) {
					cartItemBean.setName(null);
				} else {
					cartItemBean
							.setName(mp_cart.get("producttitle").toString());
				}
				if (null == mp_cart.get("price")) {
					cartItemBean.setPrice(null);
				} else {
					cartItemBean.setPrice(mp_cart.get("price").toString());
				}
				if (null == mp_cart.get("count")) {
					cartItemBean.setCount(null);
				} else {
					cartItemBean.setCount(mp_cart.get("count").toString());
				}
				if (null == mp_cart.get("availablecount")) {
					cartItemBean.setAvailablecount(null);
				} else {
					cartItemBean.setAvailablecount(mp_cart
							.get("availablecount").toString());
				}
				if (!cartItemBean.getCount().equals("0"))
					cartMap.put(cartItemBean.getId(), cartItemBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cartMap;
	}

	public static ArrayList<AddressBean> parseAddressList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<AddressBean> listAddress = new ArrayList<AddressBean>();
		try {
			JSONArray jsonArray_allAddress = new JSONArray(json);
			AddressBean addressItemBean;
			for (int i = 0; i < jsonArray_allAddress.length(); i++) {
				JSONObject jo_cart = jsonArray_allAddress.getJSONObject(i);
				addressItemBean = new AddressBean();
				if (null == jo_cart.getString("cartaddressid")) {
					addressItemBean.setId(null);
				} else {
					addressItemBean.setId(jo_cart.getString("cartaddressid"));
				}
				if (null == jo_cart.getString("province")) {
					addressItemBean.setProvince(null);
				} else {
					addressItemBean.setProvince(jo_cart.getString("province"));
				}
				if (null == jo_cart.getString("city")) {
					addressItemBean.setCity(null);
				} else {
					addressItemBean.setCity(jo_cart.getString("city"));
				}
				if (null == jo_cart.getString("district")) {
					addressItemBean.setDistrict(null);
				} else {
					addressItemBean.setDistrict(jo_cart.getString("district"));
				}
				if (null == jo_cart.getString("detail")) {
					addressItemBean.setDetail(null);
				} else {
					addressItemBean.setDetail(jo_cart.getString("detail"));
				}
				if (null == jo_cart.getString("receiver")) {
					addressItemBean.setReceiver(null);
				} else {
					addressItemBean.setReceiver(jo_cart.getString("receiver"));
				}
				if (null == jo_cart.getString("postcode")) {
					addressItemBean.setPostcode(null);
				} else {
					addressItemBean.setPostcode(jo_cart.getString("postcode"));
				}
				if (null == jo_cart.getString("mobile")) {
					addressItemBean.setMobile(null);
				} else {
					addressItemBean.setMobile(jo_cart.getString("mobile"));
				}
				if (null == jo_cart.getString("deleteurl")) {
					addressItemBean.setDeleteUrl(null);
				} else {
					addressItemBean
							.setDeleteUrl(jo_cart.getString("deleteurl"));
				}
				if (null == jo_cart.getString("updateurl")) {
					addressItemBean.setUpdateUrl(null);
				} else {
					addressItemBean
							.setUpdateUrl(jo_cart.getString("updateurl"));
				}
				listAddress.add(addressItemBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * for (int i = 0; i < listAddress.size(); i++) { LogUtil.d(TAG,
		 * listAddress.get(i).getId()); LogUtil.d(TAG,
		 * listAddress.get(i).getProvince()); LogUtil.d(TAG,
		 * listAddress.get(i).getCity()); LogUtil.d(TAG,
		 * listAddress.get(i).getDistrict()); LogUtil.d(TAG,
		 * listAddress.get(i).getDetail()); LogUtil.d(TAG,
		 * listAddress.get(i).getReceiver()); LogUtil.d(TAG,
		 * listAddress.get(i).getPostcode()); LogUtil.d(TAG,
		 * listAddress.get(i).getMobile()); LogUtil.d(TAG,
		 * listAddress.get(i).getUpdateUrl()); LogUtil.d(TAG,
		 * listAddress.get(i).getDeleteUrl()); }
		 */

		return listAddress;
	}

	public static OrderBean parseOrder(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		OrderBean orderBean = new OrderBean();
		try {
			JSONObject jo_order = new JSONObject(json);
			if (null == jo_order.getString("returncode")) {
				orderBean.setReturncode(null);
			} else {
				orderBean.setReturncode(jo_order.getString("returncode"));
			}
			if (null == jo_order.getString("returndesc")) {
				orderBean.setReturndesc(null);
			} else {
				orderBean.setReturndesc(jo_order.getString("returndesc"));
			}
			if (null == jo_order.getString("orderid")) {
				orderBean.setId(null);
			} else {
				orderBean.setId(jo_order.getString("orderid"));
			}
			if (null == jo_order.getString("receiver")) {
				orderBean.setReceiver(null);
			} else {
				orderBean.setReceiver(jo_order.getString("receiver"));
			}
			if (null == jo_order.getString("mobile")) {
				orderBean.setMobile(null);
			} else {
				orderBean.setMobile(jo_order.getString("mobile"));
			}
			if (null == jo_order.getString("address")) {
				orderBean.setAddress(null);
			} else {
				orderBean.setAddress(jo_order.getString("address"));
			}
			if (null == jo_order.getString("receivetime")) {
				orderBean.setReceivetime(null);
			} else {
				orderBean.setReceivetime(jo_order.getString("receivetime"));
			}
			if (null == jo_order.getString("fapiao")) {
				orderBean.setInvoice(null);
			} else {
				orderBean.setInvoice(jo_order.getString("fapiao"));
			}
			if (null == jo_order.getString("totalprice")) {
				orderBean.setTotalprice(null);
			} else {
				orderBean.setTotalprice(jo_order.getString("totalprice"));
			}
			if (orderBean.getReturncode().equals("200")) {
				if (null == jo_order.getString("danweifapiaoname")) {
					orderBean.setInvoiceName(null);
				} else {
					orderBean.setInvoiceName(jo_order
							.getString("danweifapiaoname"));
				}
			} else {
				orderBean.setInvoiceName(null);
			}
			if (orderBean.getReturncode().equals("200")) {
				if (null == jo_order.getString("ordernumber")) {
					orderBean.setOrdernumber(null);
				} else {
					orderBean.setOrdernumber(jo_order.getString("ordernumber"));
				}
			} else {
				orderBean.setOrdernumber(null);
			}
			if (orderBean.getReturncode().equals("200")) {

				if (jo_order.get("listproduct") != null
						&& (jo_order.get("listproduct") instanceof JSONArray)) {
					JSONArray ja_listproduct = jo_order
							.getJSONArray("listproduct");
					if (ja_listproduct != null && ja_listproduct.length() > 0) {
						List<CartItemBean> cartBeanList = new ArrayList<CartItemBean>();
						CartItemBean cartItem = null;
						JSONObject jo_item = null;
						for (int i = 0; i < ja_listproduct.length(); i++) {
							jo_item = ja_listproduct.getJSONObject(i);
							cartItem = new CartItemBean();
							cartItem.setId("" + jo_item.getInt("productid"));
							cartItem.setImgUrl(jo_item
									.getString("productimgurl"));
							cartItem.setName(jo_item.getString("producttitle"));
							cartItem.setPrice(jo_item.getString("price"));
							cartItem.setCount("" + jo_item.getInt("count"));
							cartBeanList.add(cartItem);
						}
						orderBean.setCartItemList(cartBeanList);
					}

				}
			} else {
				orderBean.setCartItemList(null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * LogUtil.d(TAG, orderBean.getReturncode()); LogUtil.d(TAG,
		 * orderBean.getReturndesc()); LogUtil.d(TAG, orderBean.getId());
		 * LogUtil.d(TAG, orderBean.getReceiver()); LogUtil.d(TAG,
		 * orderBean.getMobile()); LogUtil.d(TAG, orderBean.getAddress());
		 * LogUtil.d(TAG, orderBean.getReceivetime()); LogUtil.d(TAG,
		 * orderBean.getInvoice()); LogUtil.d(TAG, orderBean.getTotalprice());
		 */

		return orderBean;
	}

	public static ArrayList<QueryOrderBean> parseOrderList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<QueryOrderBean> listQueryOrder = new ArrayList<QueryOrderBean>();
		try {
			JSONArray jsonArray_allOrder = new JSONArray(json);
			QueryOrderBean queryOrderBean;
			for (int i = 0; i < jsonArray_allOrder.length(); i++) {
				queryOrderBean = new QueryOrderBean();
				JSONObject jo_order = jsonArray_allOrder.getJSONObject(i);
				if (null == jo_order.getString("orderid")) {
					queryOrderBean.setId(null);
				} else {
					queryOrderBean.setId(jo_order.getString("orderid"));
				}
				if (null == jo_order.getString("imgurl")) {
					queryOrderBean.setImgurl(null);
				} else {
					queryOrderBean.setImgurl(jo_order.getString("imgurl"));
				}
				if (null == jo_order.getString("orderdate")) {
					queryOrderBean.setOrderdate(null);
				} else {
					queryOrderBean
							.setOrderdate(jo_order.getString("orderdate"));
				}
				if (null == jo_order.getString("totalprice")) {
					queryOrderBean.setTotalprice(null);
				} else {
					queryOrderBean.setTotalprice(jo_order
							.getString("totalprice"));
				}
				if (null == jo_order.getString("status")) {
					queryOrderBean.setStatus(null);
				} else {
					queryOrderBean.setStatus(jo_order.getString("status"));
				}
				if (null == jo_order.getString("detailurl")) {
					queryOrderBean.setDetailurl(null);
				} else {
					queryOrderBean
							.setDetailurl(jo_order.getString("detailurl"));
				}
				if (null == jo_order.getString("cancel")) {
					queryOrderBean.setCancel(null);
				} else {
					queryOrderBean.setCancel(jo_order.getString("cancel"));
				}
				if ("0".equals(queryOrderBean.getCancel())) {
					listQueryOrder.add(queryOrderBean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return listQueryOrder;
	}

	public static OrderDetailBean parseOrderDetail(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		OrderDetailBean orderDetailBean = new OrderDetailBean();
		try {
			JSONObject jo_orderdetail = new JSONObject(json);
			if (null == jo_orderdetail.getString("orderid")) {
				orderDetailBean.setId(null);
			} else {
				orderDetailBean.setId(jo_orderdetail.getString("orderid"));
			}
			if (null == jo_orderdetail.getString("receiver")) {
				orderDetailBean.setReceiver(null);
			} else {
				orderDetailBean.setReceiver(jo_orderdetail
						.getString("receiver"));
			}
			if (null == jo_orderdetail.getString("mobile")) {
				orderDetailBean.setMobile(null);
			} else {
				orderDetailBean.setMobile(jo_orderdetail.getString("mobile"));
			}
			if (null == jo_orderdetail.getString("address")) {
				orderDetailBean.setAddress(null);
			} else {
				orderDetailBean.setAddress(jo_orderdetail.getString("address"));
			}
			if (null == jo_orderdetail.getString("receivetime")) {
				orderDetailBean.setReceivetime(null);
			} else {
				orderDetailBean.setReceivetime(jo_orderdetail
						.getString("receivetime"));
			}
			if (null == jo_orderdetail.getString("fapiao")) {
				orderDetailBean.setInvoice(null);
			} else {
				orderDetailBean.setInvoice(jo_orderdetail.getString("fapiao"));
			}
			if (null == jo_orderdetail.getString("totalprice")) {
				orderDetailBean.setTotalprice(null);
			} else {
				orderDetailBean.setTotalprice(jo_orderdetail
						.getString("totalprice"));
			}
			if (null == jo_orderdetail.getString("danweifapiaoname")) {
				orderDetailBean.setInvoiceName(null);
			} else {
				orderDetailBean.setInvoiceName(jo_orderdetail
						.getString("danweifapiaoname"));
			}
			if (null == jo_orderdetail.getString("ordernumber")) {
				orderDetailBean.setOrdernumber(null);
			} else {
				orderDetailBean.setOrdernumber(jo_orderdetail
						.getString("ordernumber"));
			}
			if (null == jo_orderdetail.getString("orderdate")) {
				orderDetailBean.setOrderdate(null);
			} else {
				orderDetailBean.setOrderdate(jo_orderdetail
						.getString("orderdate"));
			}
			if (null == jo_orderdetail.getString("status")) {
				orderDetailBean.setStatus(null);
			} else {
				orderDetailBean.setStatus(jo_orderdetail.getString("status"));
			}
			if (null == jo_orderdetail.getString("paytype")) {
				orderDetailBean.setPaytype(null);
			} else {
				orderDetailBean.setPaytype(jo_orderdetail.getString("paytype"));
			}
			JSONArray ja_productlist = jo_orderdetail
					.getJSONArray("listproduct");
			ArrayList<CartItemBean> productList = orderDetailBean
					.getProductList();
			for (int i = 0; i < ja_productlist.length(); i++) {
				JSONObject jo_product = ja_productlist.getJSONObject(i);
				CartItemBean cartItemBean = new CartItemBean();
				if (null == jo_product.getString("productid")) {
					cartItemBean.setId(null);
				} else {
					cartItemBean.setId(jo_product.getString("productid"));
				}
				if (null == jo_product.getString("productimgurl")) {
					cartItemBean.setImgUrl(null);
				} else {
					cartItemBean.setImgUrl(jo_product
							.getString("productimgurl"));
				}
				if (null == jo_product.getString("producttitle")) {
					cartItemBean.setName(null);
				} else {
					cartItemBean.setName(jo_product.getString("producttitle"));
				}
				if (null == jo_product.getString("price")) {
					cartItemBean.setPrice(null);
				} else {
					cartItemBean.setPrice(jo_product.getString("price"));
				}
				if (null == jo_product.getString("count")) {
					cartItemBean.setCount(null);
				} else {
					cartItemBean.setCount(jo_product.getString("count"));
				}
				productList.add(cartItemBean);
			}
			orderDetailBean.setProductList(productList);
			return orderDetailBean;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<StationBean> parseStationList(String json) {
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		ArrayList<StationBean> stationList = new ArrayList<StationBean>();
		JSONArray jsonArray_allStation;
		try {
			jsonArray_allStation = new JSONArray(json);
			StationBean stationBean;
			for (int i = 0; i < jsonArray_allStation.length(); i++) {
				stationBean = new StationBean();
				JSONObject jo_station = jsonArray_allStation.getJSONObject(i);
				if (null == jo_station.getString("location")) {
					stationBean.setLocation(null);
				} else {
					stationBean.setLocation(jo_station.getString("location"));
				}
				if (null == jo_station.getString("authcode")) {
					stationBean.setAuthcode(null);
				} else {
					stationBean.setAuthcode(jo_station.getString("authcode"));
				}
				if (null == jo_station.getString("name")) {
					stationBean.setName(null);
				} else {
					stationBean.setName(jo_station.getString("name"));
				}
				if (null == jo_station.getString("address")) {
					stationBean.setAddress(null);
				} else {
					stationBean.setAddress(jo_station.getString("address"));
				}
				if (null == jo_station.getString("phone")) {
					stationBean.setPhone(null);
				} else {
					stationBean.setPhone(jo_station.getString("phone"));
				}
				stationList.add(stationBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return stationList;
	}

	public static String parseServerResponse(String json) {
		String result = null;
		if (json == null || "".equals(json.trim())
				|| "null".equals(json.trim()))
			return null;
		try {
			JSONObject jo_response = new JSONObject(json);
			int returncode = jo_response.getInt("returncode");
			// String returndesc = jo_response.getString("returndesc");
			result = String.valueOf(returncode);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/*
	 * public static ArrayList<MovieBean> parseMovieList(String json) { if (json
	 * == null || "".equals(json.trim()) || "null".equals(json.trim())) return
	 * null; ArrayList<MovieBean> listMoveBean = new ArrayList<MovieBean>(); try
	 * { JSONArray jsonArray_allMovie = new JSONArray(json); MovieBean
	 * moviebean; for (int i = 0; i < jsonArray_allMovie.length(); i++) {
	 * JSONObject jo_movie = jsonArray_allMovie.getJSONObject(i); moviebean =
	 * new MovieBean(); if (null == jo_movie.getString("name")) {
	 * moviebean.setName("null"); } else {
	 * moviebean.setName(jo_movie.getString("name")); } if (null ==
	 * jo_movie.getString("imgurl")) { moviebean.setImgUrl("null"); } else {
	 * moviebean.setImgUrl(jo_movie.getString("imgurl")); } if (null ==
	 * jo_movie.getString("imgurl_local")) { moviebean.setImgUrl_local("null");
	 * } else { moviebean.setImgUrl_local(jo_movie .getString("imgurl_local"));
	 * } listMoveBean.add(moviebean); } } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } // for (int i = 0; i <
	 * listMoveBean.size(); i++) { // LogUtil.d(TAG,
	 * listMoveBean.get(i).getName()); // LogUtil.d(TAG,
	 * listMoveBean.get(i).getImgUrl()); // LogUtil.d(TAG,
	 * listMoveBean.get(i).getImgUrl_local()); // } return listMoveBean; }
	 * 
	 * public static MovieBean parseMovieBean(String json) { if (json == null ||
	 * "".equals(json.trim()) || "null".equals(json.trim())) return null;
	 * MovieBean moviebean = new MovieBean(); try { JSONObject jo_movie = new
	 * JSONObject(json); if (null == jo_movie.getString("name")) {
	 * moviebean.setName("null"); } else {
	 * moviebean.setName(jo_movie.getString("name")); } if (null ==
	 * jo_movie.getString("imgurl")) { moviebean.setImgUrl("null"); } else {
	 * moviebean.setImgUrl(jo_movie.getString("imgurl")); } if (null ==
	 * jo_movie.getString("imgurl_local")) { moviebean.setImgUrl_local("null");
	 * } else { moviebean.setImgUrl_local(jo_movie.getString("imgurl_local")); }
	 * if (null == jo_movie.getString("district")) {
	 * moviebean.setDistrict("null"); } else {
	 * moviebean.setDistrict(jo_movie.getString("district")); } if (null ==
	 * jo_movie.getString("premieredate")) { moviebean.setPremiereDate("null");
	 * } else { moviebean.setPremiereDate(jo_movie.getString("premieredate")); }
	 * if (null == jo_movie.getString("description")) {
	 * moviebean.setDescription("null"); } else {
	 * moviebean.setDescription(jo_movie.getString("description")); }
	 * 
	 * JSONArray ja_genres = jo_movie.getJSONArray("genres"); if (null ==
	 * ja_genres) { moviebean.getGenreList().add("null"); } else { for (int i =
	 * 0; i < ja_genres.length(); i++) {
	 * moviebean.getGenreList().add(ja_genres.getString(i)); } } JSONArray
	 * ja_siteurls = jo_movie.getJSONArray("siteurls"); if (null == ja_siteurls)
	 * { moviebean.getSiteUrlList().add("null"); } else { for (int i = 0; i <
	 * ja_siteurls.length(); i++) { moviebean.getSiteUrlList().add(
	 * TextUtil.clearUrl(ja_siteurls.getString(i)));
	 * moviebean.getSourceList().add(
	 * TextUtil.getSource(ja_siteurls.getString(i))); } }
	 * 
	 * JSONArray ja_resiteurls = jo_movie.getJSONArray("redirectsiteurls"); if
	 * (null == ja_resiteurls) { moviebean.getRedirectSiteUrlList().add("null");
	 * } else { for (int i = 0; i < ja_resiteurls.length(); i++) {
	 * moviebean.getRedirectSiteUrlList().add(
	 * TextUtil.clearUrl(ja_resiteurls.getString(i))); } }
	 * 
	 * JSONArray ja_flashurl = jo_movie.getJSONArray("flashurl"); if (null ==
	 * ja_flashurl) { moviebean.getFlashUrlList().add("null"); } else { for (int
	 * i = 0; i < ja_flashurl.length(); i++) { moviebean.getFlashUrlList().add(
	 * TextUtil.clearUrl(ja_flashurl.getString(i))); } }
	 * 
	 * JSONArray ja_reflashurl = jo_movie.getJSONArray("redirectflashurl"); if
	 * (null == ja_reflashurl) {
	 * moviebean.getRedirectFlashUrlList().add("null"); } else { for (int i = 0;
	 * i < ja_reflashurl.length(); i++) {
	 * moviebean.getRedirectFlashUrlList().add(
	 * TextUtil.clearUrl(ja_reflashurl.getString(i))); } }
	 * 
	 * JSONArray ja_director = jo_movie.getJSONArray("director"); if (null ==
	 * ja_director) { moviebean.getDirectorList().add("null"); } else { for (int
	 * i = 0; i < ja_director.length(); i++) {
	 * moviebean.getDirectorList().add(ja_director.getString(i)); } } JSONArray
	 * ja_actors = jo_movie.getJSONArray("actors"); if (null == ja_actors) {
	 * moviebean.getActorList().add("null"); } else { for (int i = 0; i <
	 * ja_actors.length(); i++) {
	 * moviebean.getActorList().add(ja_actors.getString(i)); } } } catch
	 * (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return moviebean;
	 * 
	 * }
	 * 
	 * public static ArrayList<FilterBean> parseFilterList(String json) { if
	 * (json == null || "".equals(json.trim()) || "null".equals(json.trim()))
	 * return null; ArrayList<FilterBean> listFilterBean = new
	 * ArrayList<FilterBean>(); try { JSONObject jo_allFilter = new
	 * JSONObject(json);
	 * 
	 * JSONArray ja_genres = jo_allFilter.getJSONArray("genres"); LogUtil.d(TAG,
	 * "ja_genres.length()"); FilterBean gfilterBean = new FilterBean();
	 * gfilterBean.setName("genres"); for (int i = 0; i < ja_genres.length();
	 * i++) { // if ("*" != ja_genres.getString(i)) {
	 * gfilterBean.getValueList().add(ja_genres.getString(i)); // } }
	 * listFilterBean.add(gfilterBean);
	 * 
	 * JSONArray ja_districts = jo_allFilter.getJSONArray("districts");
	 * FilterBean dfilterBean = new FilterBean();
	 * dfilterBean.setName("districts"); for (int j = 0; j <
	 * ja_districts.length(); j++) { // if ("*" != ja_districts.getString(i)) {
	 * dfilterBean.getValueList().add(ja_districts.getString(j)); // } }
	 * listFilterBean.add(dfilterBean);
	 * 
	 * JSONArray ja_premieredates = jo_allFilter .getJSONArray("premieredates");
	 * FilterBean pfilterBean = new FilterBean();
	 * pfilterBean.setName("premieredates"); for (int k = 0; k <
	 * ja_premieredates.length(); k++) { // if ("*" !=
	 * ja_premieredates.getString(i)) {
	 * pfilterBean.getValueList().add(ja_premieredates.getString(k)); // } }
	 * listFilterBean.add(pfilterBean); } catch (JSONException e) {
	 * e.printStackTrace(); } for (int i = 0; i < listFilterBean.size(); i++) {
	 * LogUtil.d(TAG, "listFilterBean.get(" + i + ").getName()  " +
	 * listFilterBean.get(i).getName()); } return listFilterBean; }
	 * 
	 * public static String parseServerResponse(String json){ String result =
	 * null; if (json == null || "".equals(json.trim()) ||
	 * "null".equals(json.trim())) return null; try { JSONObject jo_response =
	 * new JSONObject(json); int returncode = jo_response.getInt("returncode");
	 * // String returndesc = jo_response.getString("returndesc"); result =
	 * String.valueOf(returncode); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return result; }
	 * 
	 * public static String paikeRegister(final User user){ String status =
	 * null; ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	 * params.add(new BasicNameValuePair("userid", user.userId)); params.add(new
	 * BasicNameValuePair("usernickname", user.userId)); params.add(new
	 * BasicNameValuePair("useravatar", user.userId)); status =
	 * parseServerResponse(getPostJsonString(Constants.PAIKE_REGISTER_URL,
	 * params)); LogUtil.d(TAG, "paikeRegister  "+status); return status; }
	 */
}
