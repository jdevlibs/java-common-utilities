/*
* -----------------------------------------------------------------------------------
* Copyright © 2020 by Cana enterprise co,.Ltd. All rights reserved.
* -----------------------------------------------------------------------------------
*/
package io.github.jdevlibs.utils.exception;

/**
* @author Supot Saelao
* @version 1.0
*/
public class SystemException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public SystemException(Throwable ex) {
		super(ex);
	}

	public SystemException(Throwable ex, String message) {
		super(message, ex);
	}
	
	public SystemException(String message) {
		super(message);
	}
}
