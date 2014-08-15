package com.vee.shop.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.yunfox.s4aservicetest.response.CartAddress;
import com.yunfox.s4aservicetest.response.CartAddressList;
import com.yunfox.s4aservicetest.response.ServerOrder;
import com.yunfox.s4aservicetest.response.ServerOrderList;

public class TextUtil {

	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	public static SpannableStringBuilder getEtErrorString(String error) {
		// int ecolor = ApplicationUtils.getResId("color", "highlight_text");
		int ecolor = 0xfff05000;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
		ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
		return ssbuilder;
	}

	public static String splitEmail(String email) {
		if (null != email) {
			String[] ss = email.split("@");
			if (ss.length > 0) {
				return ss[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String createAddressJson(CartAddressList addresslist) {
		// TODO Auto-generated method stub

		try {
			// JSONObject jsonObject = new JSONObject();

			JSONArray array = new JSONArray();
			for (int i = 0; i < addresslist.size(); i++) {
				JSONObject address = new JSONObject();
				CartAddress p = addresslist.get(i);
				address.put("cartaddressid", p.getCartaddressid());
				address.put("province", p.getProvince());
				address.put("city", p.getCity());
				address.put("district", p.getDistrict());
				address.put("detail", p.getDetail());
				address.put("receiver", p.getReceiver());
				address.put("postcode", p.getPostcode());
				address.put("mobile", p.getMobile());
				address.put("deleteurl", p.getDeleteurl());
				address.put("updateurl", p.getUpdateurl());
				array.put(address);
			}
			return array.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String createOrderListJson(ServerOrderList orderlist) {
		try {
			JSONArray array = new JSONArray();
			for (int i = 0; i < orderlist.size(); i++) {
				JSONObject order = new JSONObject();
				ServerOrder p = orderlist.get(i);
				order.put("orderid", p.getOrderid());
				order.put("imgurl", p.getImgurl());
				order.put("orderdate", p.getOrderdate());
				order.put("totalprice", p.getTotalprice());
				order.put("status", p.getStatus());
				order.put("detailurl", p.getDetailurl());
				order.put("cancel", p.getCancel());
				array.put(order);
			}
			return array.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static String createCheckoutJson(Map<String, Object> result) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("totalprice", result.get("totalprice"));
			jsonObject.put("deliveryprice", result.get("deliveryprice"));
			jsonObject.put("originalprice", result.get("originalprice"));
			List<Map<String, Object>> list_allProduct = (List<Map<String, Object>>) result
					.get("listproduct");
			JSONArray array = new JSONArray();
			for (int i = 0; i < list_allProduct.size(); i++) {
				JSONObject product = new JSONObject();
				product.put("productid", list_allProduct.get(i)
						.get("productid"));
				product.put("productimgurl",
						list_allProduct.get(i).get("productimgurl"));
				product.put("productitle",
						list_allProduct.get(i).get("productitle"));
				product.put("price", list_allProduct.get(i).get("price"));
				product.put("count", list_allProduct.get(i).get("count"));
				array.put(product);
			}
			jsonObject.put("listproduct", array);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static String createOrderJson(Map<String, Object> result) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("returncode", result.get("returncode"));
			jsonObject.put("returndesc", result.get("returndesc"));
			jsonObject.put("orderid", result.get("orderid"));
			jsonObject.put("receiver", result.get("receiver"));
			jsonObject.put("mobile", result.get("mobile"));
			jsonObject.put("address", result.get("address"));
			jsonObject.put("receivetime", result.get("receivetime"));
			jsonObject.put("fapiao", result.get("fapiao"));
			jsonObject.put("totalprice", result.get("totalprice"));
			if (result.get("returncode").toString().equals("200")) {
				jsonObject.put("danweifapiaoname",
						result.get("danweifapiaoname"));
				jsonObject.put("ordernumber", result.get("ordernumber"));
				List<Map<String, Object>> list_allProduct = (List<Map<String, Object>>) result
						.get("listproduct");
				JSONArray array = new JSONArray();
				for (int i = 0; i < list_allProduct.size(); i++) {
					JSONObject product = new JSONObject();
					product.put("productid",
							list_allProduct.get(i).get("productid"));
					product.put("productimgurl",
							list_allProduct.get(i).get("productimgurl"));
					product.put("producttitle",
							list_allProduct.get(i).get("producttitle"));
					product.put("price", list_allProduct.get(i).get("price"));
					product.put("count", list_allProduct.get(i).get("count"));
					array.put(product);
				}
				jsonObject.put("listproduct", array);
			}
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static String createOrderDetailJson(Map<String, Object> result) {

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orderid", result.get("orderid"));
			jsonObject.put("receiver", result.get("receiver"));
			jsonObject.put("mobile", result.get("mobile"));
			jsonObject.put("address", result.get("address"));
			jsonObject.put("receivetime", result.get("receivetime"));
			jsonObject.put("fapiao", result.get("fapiao"));
			jsonObject.put("totalprice", result.get("totalprice"));
			jsonObject.put("danweifapiaoname", result.get("danweifapiaoname"));
			jsonObject.put("ordernumber", result.get("ordernumber"));
			jsonObject.put("orderdate", result.get("orderdate"));
			jsonObject.put("status", result.get("status"));
			jsonObject.put("paytype", result.get("paytype"));
			List<Map<String, Object>> list_allProduct = (List<Map<String, Object>>) result
					.get("listproduct");
			JSONArray array = new JSONArray();
			for (int i = 0; i < list_allProduct.size(); i++) {
				JSONObject product = new JSONObject();
				product.put("productid", list_allProduct.get(i)
						.get("productid"));
				product.put("productimgurl",
						list_allProduct.get(i).get("productimgurl"));
				product.put("producttitle",
						list_allProduct.get(i).get("producttitle"));
				product.put("price", list_allProduct.get(i).get("price"));
				product.put("count", list_allProduct.get(i).get("count"));
				array.put(product);
			}
			jsonObject.put("listproduct", array);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
