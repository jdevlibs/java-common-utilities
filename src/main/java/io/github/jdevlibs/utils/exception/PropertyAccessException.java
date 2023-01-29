/*
 * ----------------------------------------------------------------------------
 * Copyright © 2020 by Cana enterprise co,.Ltd. All rights reserved
 * ----------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils.exception;

/**
 * @author Supot Saelao
 * @version 1.0
 */
public class PropertyAccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PropertyAccessException(Throwable cause) {
		super(cause);
	}

	public PropertyAccessException(String message) {
		super(message);
	}
	
	public PropertyAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
