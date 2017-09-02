package br.com.browseframework.base.data;

import java.io.Serializable;
import java.util.List;

import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.PageType;

/**
 * Represents a data wrapper 
 * @author Eduardo
 *
 * @param <T>
 */
public class DataPage<T> implements Serializable, DataPageType<T> {

	private static final long serialVersionUID = -1251264802171104259L;
	// Data in the wrapper
	private List<T> data;
	// Page
	private PageType page;
	// Total
	private Number count;

	/**
	 * Constructor.
	 * 
	 * @param data
	 * @param count
	 * @param page
	 */
	public DataPage(List<T> data, Number count, PageType page) {
		this(data, page);
		setCount(count);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param data
	 * @param page
	 */
	public DataPage(List<T> data, PageType page) {
		setData(data);
		setPage(page);
		if (data != null){
			setCount(data.size());
		} else {
			setCount(0);
		}
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.DataPageType#getPage()
	 */
	@Override
	public PageType getPage() {
		return page;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.DataPageType#setPage(br.com.browseframework.data.PageType)
	 */
	@Override
	public void setPage(PageType page) {
		this.page = page;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.DataPageType#getData()
	 */
	@Override
	public List<T> getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.DataPageType#setData(java.util.List)
	 */
	@Override
	public void setData(List<T> data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.data.type.DataPageType#getCount()
	 */
	public Number getCount() {
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.data.type.DataPageType#setCount(java.lang.Number)
	 */
	public void setCount(Number count) {
		this.count = count;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.data.type.DataPageType#getFirst()
	 */
	public T getFirst(){
		T result = null;
		if (getData() != null && !getData().isEmpty()){
			result = getData().get(0);
		}
		
		return result;
	}

}