package br.com.browseframework.base.data.type;

import java.util.List;


public interface DataPageType<T> {

	/**
	 * Search page representation
	 * @return
	 */
	public abstract PageType getPage();

	/**
	 * Sets the search page representation
	 * 
	 * @param page
	 */
	public abstract void setPage(PageType page);

	/**
	 * Content data
	 * @return
	 */
	public abstract List<T> getData();

	/**
	 * Set the content data
	 * @param data
	 */
	public abstract void setData(List<T> data);

	/**
	 * Set total records.
	 * @param count
	 */
	public void setCount(Number count);
	
	/**
	 * Total records
	 * @return
	 */
	public Number getCount();
	
	/**
	 * Returns the first element when it exists.
	 * @return
	 */
	public T getFirst();
}