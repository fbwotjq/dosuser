package demo.tcp.http;


public class HttpValidateStatus {

	public static final boolean REQUEST_STATUS_FALSE = false;
	public static final boolean REQUEST_STATUS_TRUE = true;
	
	public static final HttpValidateStatus INVALID_REQUEST = new HttpValidateStatus(
			REQUEST_STATUS_FALSE, 
			NettyHttpConstants.CODE_INVALID_REQUEST,
			NettyHttpConstants.MESSAGE_INVALID_REQUEST
			);
	public static final HttpValidateStatus INVALID_URI = new HttpValidateStatus(
			REQUEST_STATUS_FALSE, 
			NettyHttpConstants.CODE_INVALID_URI_REQUEST,
			NettyHttpConstants.MESSAGE_INVALID_URI_REQUEST
			);
	public static final HttpValidateStatus SERVER_ERROR = new HttpValidateStatus(
			REQUEST_STATUS_FALSE, 
			NettyHttpConstants.CODE_SERVER_ERROR,
			NettyHttpConstants.MESSAGE_SERVER_ERROR
			);
	public static final HttpValidateStatus VALID_REQUEST = new HttpValidateStatus(
			REQUEST_STATUS_TRUE, 
			NettyHttpConstants.CODE_VALID_REQUEST,
			NettyHttpConstants.MESSAGE_VALID_REQUEST
			);
	public static final HttpValidateStatus INVALID_PARAMETER = new HttpValidateStatus(
			REQUEST_STATUS_TRUE, 
			NettyHttpConstants.CODE_INVALID_PARAM_REQUEST,
			NettyHttpConstants.MESSAGE_INVALID_PARAM_REQUEST
			);
	
	public HttpValidateStatus(boolean status, int statusCode, byte[] statusMessage){
		 this.status = status;
		 this.statusCode = statusCode;
		 this.statusMessage = statusMessage;
	}
	
	private boolean status;
	private int statusCode;
	private byte[] statusMessage;
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public byte[] getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(byte[] statusMessage) {
		this.statusMessage = statusMessage;
	}
	
}
