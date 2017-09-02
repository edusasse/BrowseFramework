package br.com.browseframework.base.crud.paging.impl;

import br.com.browseframework.base.crud.paging.Pagination;
import br.com.browseframework.base.data.type.PageType;

public class PagingMySql implements Pagination {
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.base.crud.paging.Pagination#pageQuery(java.lang.String, br.com.browseframework.base.data.type.PageType)
	 */
	@Override
	public String pageQuery(String query, PageType p) {
		String result = query;

		if (query != null && p != null && p.isValid()) {
			final StringBuilder sb = new StringBuilder();
			sb.append(query);
			sb.append(" LIMIT " + p.getPageSize() + " OFFSET " + p.getStartRow());
			result = sb.toString();
		}

		return result;
	}
}
