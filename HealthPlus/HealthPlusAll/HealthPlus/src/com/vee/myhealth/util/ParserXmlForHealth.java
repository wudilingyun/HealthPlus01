package com.vee.myhealth.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;


import com.vee.myhealth.bean.Health;

public class ParserXmlForHealth {
	private Context context;
	private Health health;

	public ParserXmlForHealth(Context context) {
		this.context = context;
	}

	List<Health> list;

	private void parseXml(String name) {
		int i, j;

		list = new ArrayList<Health>();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		Document doc = null;
		InputStream inStream = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			inStream = context.getResources().getAssets().open("health.xml");
			doc = docBuilder.parse(inStream);
			Element rootEle = doc.getDocumentElement();
			NodeList questionNode = rootEle.getElementsByTagName("group");
			int subCount = questionNode.getLength();
			for (i = 0; i < subCount; i++) {
				Element subEle = (Element) questionNode.item(i);
				String title = subEle.getAttribute("mcName");
				String id = subEle.getAttribute("id");
				if (title.equals(name))
					health = new Health();
				health.setName(title);
				health.setId(id);
				list.add(health);

			}

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
