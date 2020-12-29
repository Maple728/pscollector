/**
 * 
 */
/**
 * @author Maple
 *
 */
package org.maple.profitsystem.exceptions;

public class PSException extends Exception {

	private static final long serialVersionUID = -8274399266097282562L;
	
	private String msg;
	
	public PSException() {
	}
	
	public PSException(String message) {
		msg = message;
	}
	
	public String getMessage() {
		return msg;
	}
}