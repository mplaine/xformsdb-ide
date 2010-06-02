package fi.tkk.media.xide.client.Server.RPC;

public class PublishException extends Exception {
	
	public static final int APPLICATION_ALREADY_IN_TOMCAT = 1;
	public static final int COMPONENT_ALREADY_IN_XIDE = 2;
	public static final int COMPONENT_ALREADY_IN_DATABASE = 3;
	
	// PRIVATE VARIABLES
	private int code = 0;

	// PUBLIC CONSTRUCTORS
	public PublishException(String description) {
		super(description);
	}
	
	public PublishException(int code, String description) {
		super(description);
		this.code = code;
	}
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}
