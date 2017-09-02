package br.com.browseframework.base.exception;


import java.util.Calendar;
import java.util.Date;

/**
 * Control exception in business layer.
 * @author Eduardo
 *
 */
public abstract class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -4941388354300929295L;
	
	// Message key
	private String messageKey;

	// Message paramters
	private Object[] messageKeyParams;

	// Exception occurred time
	private Date dateTime;
	
	// Error code
	private Long errorCode;

	/**
	 * Constructor
	 * 
	 * @param resourceBundle
	 * @param messageKey
	 * @param messageKeyParams
	 * @param message
	 * @param t
	 */
	public BusinessException(Long errorCode, String messageKey, Object[] messageKeyParams, String message, Throwable t) {
		super(message, t);
		setDateTime(Calendar.getInstance().getTime());
		setErrorCode(errorCode);
		setMessageKey(messageKey);
		setMessageKeyParams(messageKeyParams);
	}

	public BusinessException(String messageKey, Object[] messageKeyParams,String message) {
		this(null, messageKey, messageKeyParams, message, null);
	}

	public BusinessException(String messageKey, String message) {
		this(null, messageKey, null, message, null);
	} 

	public BusinessException(String message) {
		this(null, null, null, message, null);
	}

	public BusinessException(String message, Throwable t) {
		this(null, null, null, message, t);
	}
  
	// GETTERS && SETTERS
	
	public String getMessageKey() {
		return messageKey;
	}
 
	public Object[] getMessageKeyParams() {
		return messageKeyParams;
	}
 
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public void setMessageKeyParams(Object[] messageKeyParams) {
		this.messageKeyParams = messageKeyParams;
	}
	 
	public Long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Long errorCode) {
		this.errorCode = errorCode;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	 
}