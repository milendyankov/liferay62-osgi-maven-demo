package com.liferay.v62.osgi.demo.textfilter.api;

import javax.portlet.PortletRequest;

public interface TextFilter {

	String filter (PortletRequest request, String text);
}
