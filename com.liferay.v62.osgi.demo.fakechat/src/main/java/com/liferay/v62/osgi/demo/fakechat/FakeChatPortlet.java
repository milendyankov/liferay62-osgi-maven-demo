package com.liferay.v62.osgi.demo.fakechat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.osgi.util.service.Reference;
import com.liferay.osgi.util.service.ReflectionServiceTracker;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.freemarker.FreeMarkerPortlet;
import com.liferay.v62.osgi.demo.textfilter.api.FilterRegistry;
import com.liferay.v62.osgi.demo.textfilter.api.TextFilter;

public class FakeChatPortlet extends FreeMarkerPortlet {

    /**
     * An OSGi service to track and inject other services with the portlet. We
     * can not use a component framework like DS here as the portlet instance is
     * created by the portal.
     */
    private ReflectionServiceTracker reflectionServiceTracker;
	
    /**
     * Registry for all filters registered as OSGi services
     */
	private FilterRegistry filterRegistry;

	/**
	 * In memory storage for messages 
	 */
	private List<String> messages = new LinkedList<String>();

	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		renderRequest.setAttribute("messages", messages);
		super.doView(renderRequest, renderResponse);
	}

	public void addEntry(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		String text = ParamUtil.getString(actionRequest, "message", "");
		
		for (TextFilter textFilter : filterRegistry.getTextFilters()) {
			text = textFilter.filter(actionRequest, text);
		}
		
		messages.add(text);

	}

	@Reference
	public void setFilterRegistry(FilterRegistry filterRegistry) {
		this.filterRegistry = filterRegistry;
	}
	
	
    /**
     * Initializes the {@link ReflectionServiceTracker} responsible to inject
     * services
     */
    @Override
    public void init()
            throws PortletException {

            super.init();
            reflectionServiceTracker = new ReflectionServiceTracker(this);

    }
    
    /**
     * Clear the {@link ReflectionServiceTracker} instance created during
     * {@link #init()}
     */
    @Override
    public void destroy() {

            super.destroy();
            if (reflectionServiceTracker != null) {
                    reflectionServiceTracker.close();
            }
    }
     


	

}
