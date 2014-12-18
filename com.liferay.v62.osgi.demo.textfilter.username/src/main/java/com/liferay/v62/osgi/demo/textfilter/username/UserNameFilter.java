package com.liferay.v62.osgi.demo.textfilter.username;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.PortalUtil;
import com.liferay.v62.osgi.demo.textfilter.api.TextFilter;

@Component(immediate = true)
public class UserNameFilter implements TextFilter {

	private UserLocalService userLocalService;

	@Reference
	public void addUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	public void removeUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	public String filter(PortletRequest request, String text) {

		long companyId = PortalUtil.getCompanyId(request);

		StringBuffer result = new StringBuffer();
		Pattern p = Pattern.compile("@[^\\s]*");
		Matcher m = p.matcher(text);
		while (m.find()) {
			String screenName = m.group().substring(1);
			System.out.println("Found " + screenName);
			User user = null;
			try {
				user = userLocalService.getUserByScreenName(companyId,
						screenName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user != null) {
				m.appendReplacement(
						result,
						Matcher.quoteReplacement(getUserText(user)));
			}
		}
		m.appendTail(result);
		System.out.println("Line is now: " + result);

		return result.toString();
	}

	private String getUserText(User user) {
		return "<@liferay_ui[\"user-display\"] userId=" + user.getUserId()
				+ " displayStyle=4/>";
	}

}
