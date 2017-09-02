package br.com.browseframework.base.crud.paging;

import br.com.browseframework.base.data.type.PageType;

public interface Pagination {

	/**
	 * Adds default pagination behavior.
	 * @param query
	 * @param p
	 * @return
	 */
	public abstract String pageQuery(String query, PageType p);

}