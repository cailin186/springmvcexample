/**
 * Copyright (c) 2002
 * All rights reserved.
 * @author cailin
 * @version 1.2
 * Date:2009-7-18
 */
package org.springframework.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
	public static String getProperty(String pro, String filename) {
		Properties ssoProp = new Properties();
		FileInputStream ssoFis;
		try {
			ssoFis = new FileInputStream(PropertiesUtil.class.getResource("/" + filename + ".properties").getPath());
			// ssoProp.loadFromXML(ssoFis);
			ssoProp.load(ssoFis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String properties = ssoProp.getProperty(pro);
		return properties;
	}

	public static String getProperty(String pro) {
		Properties ssoProp = new Properties();
		FileInputStream ssoFis;
		try {
			ssoFis = new FileInputStream(PropertiesUtil.class.getResource("/application.properties").getPath());
			ssoProp.load(ssoFis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String properties = ssoProp.getProperty(pro);
		return properties;
	}

	public static void main(String[] args) {

	String url = "/gate/talkplatform_appoint_consumer/appoint_multi/gate/query_tea_appoint";
	System.out.println(url.indexOf("gate"));
	}

}
