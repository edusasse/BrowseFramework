package br.com.browseframework.base.data;

import java.io.Serializable;

import br.com.browseframework.base.data.type.PageType;

/**
 * 
 * @author Eduardo
 *
 */
public class Page implements Serializable, PageType {

	private static final long serialVersionUID = -7507441917807011444L;

	// Start row
	private int startRow;
	// Maximum number of records retrieved from the data source
	private int pageSize;

	/**
	 * Empty constructor
	 */
	public Page() { }

	/**
	 * Contructor
	 * @param startRow
	 * @param pageSize
	 */
	public Page(int startRow, int pageSize) {
		setStartRow(startRow);
		setPageSize(pageSize);
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.PageType#isValid()
	 */
	@Override
	public boolean isValid() {
		return getStartRow() >= 0 && getPageSize() > 0;
	}
	
	/**
	 * Returns the Start Row and Page Size
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("Start Row [");
		result.append(this.startRow);
		result.append("] ");
		result.append("Page sise [");
		result.append(this.pageSize);
		result.append("]");
		
		return result.toString();
	}

	// GETTERS && SETTERS 

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.PageType#getStartRow()
	 */
	@Override
	public int getStartRow() {
		return startRow;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.PageType#setStartRow(int)
	 */
	@Override
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.PageType#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.PageType#setPageSize(int)
	 */
	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
 
}