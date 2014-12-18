package com.liferay.v62.osgi.demo.textfilter.api;

import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;


@Component(
		immediate = true, 
		service = FilterRegistry.class)
public class FilterRegistry {

	List<TextFilter> textFilters = new LinkedList<TextFilter>();

	public List<TextFilter> getTextFilters() {
		return textFilters;
	}

	/**
	 * Injection point for the text filters
	 * 
	 * @param textFilter
	 */
	@Reference (
			unbind = "removeTextFilter",
			cardinality = ReferenceCardinality.MULTIPLE, 
			policy = ReferencePolicy.DYNAMIC)
	public void addTextFilter(TextFilter textFilter) {
		textFilters.add(textFilter);
	}

	public void removeTextFilter(TextFilter textFilter) {
		textFilters.remove(textFilter);
	}

}
