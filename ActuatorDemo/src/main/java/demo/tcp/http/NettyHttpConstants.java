package demo.tcp.http;

/**
 * 요청 처리 상수 값 클래스
 */
public class NettyHttpConstants {

	public static final String FIRST_URI = "/service/first";
	public static final String SECOND_URI = "/service/second";
	public static final String THIRD_URI = "/service/third";
	
	public static final byte[] MESSAGE_VALID_REQUEST = "{\"result\":\"valid request\", \"status\":\"SUCCESS\"}".getBytes();;
	public static final byte[] MESSAGE_INVALID_URI_REQUEST = "{\"result\":\"invalid uri\", \"status\":\"FAIL\"}".getBytes();
	public static final byte[] MESSAGE_INVALID_PARAM_REQUEST = "{\"result\":\"invalid parameter\", \"status\":\"FAIL\"}".getBytes();
	public static final byte[] MESSAGE_INVALID_PARAM_METHOD = "{\"result\":\"invalid method\", \"status\":\"FAIL\"}".getBytes();
	/** 잘못된 호출 메세지 			*/ public static final byte[] MESSAGE_INVALID_REQUEST = "{\"result\":\"invalid request\", \"status\": \"FAIL\"}".getBytes();
	/** 서버 에러 .. 			*/ public static final byte[] MESSAGE_SERVER_ERROR = "{\"result\":\"server error\" ,\"status\":\"FAIL\"}".getBytes();
	
	/** 정상요청 						*/ public static final int CODE_VALID_REQUEST = 200;
	/** 비정상 URI 입력					*/ public static final int CODE_INVALID_URI_REQUEST = 201;
	/** 비정상 파라메터 입력				*/ public static final int CODE_INVALID_PARAM_REQUEST = 202;
	/** 비정상 메소드 입력 (POST/GET만 지원)	*/ public static final int CODE_INVALID_PARAM_METHOD = 203;
	/** 무엇인가 요청값이 잘못 된 경우			*/ public static final int CODE_INVALID_REQUEST = 204;
	/** 요청 처리를 잘못하여 생긴 문제			*/ public static final int CODE_SERVER_ERROR = 205;
	
	public static final String PARAM_KEY_KEYWORD = "keyword";
	public static final String PARAM_KEY_CATEGORY = "category";
	public static final String PARAM_KEY_START_OFFSET = "startOffset";
	public static final String PARAM_KEY_END_OFFSET = "endOffset";
	
	public static final String CONTENT_TYPE_APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
	
}
