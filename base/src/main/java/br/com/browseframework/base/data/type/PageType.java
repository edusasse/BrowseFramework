package br.com.browseframework.base.data.type;

public interface PageType {

	/**
	 * Verifies if the start row and page size are bigger than 0
	 * @return
	 */
	public abstract boolean isValid();

	public abstract int getStartRow();

	public abstract void setStartRow(int startRow);

	public abstract int getPageSize();

	public abstract void setPageSize(int pageSize);

}