/* Copyright (C) 2007  Versant Inc.  http://www.db4o.com */

package com.db4o.constraints;


/**
 * db4o-specific exception.<br><br>
 * This exception can be thrown by a 
 * {@link com.db4o.constraints.UniqueFieldValueConstraint} on commit.
 * @see com.db4o.config.ObjectField#indexed(boolean)
 * @see com.db4o.config.Configuration#add(com.db4o.config.ConfigurationItem)
 */
public class UniqueFieldValueConstraintViolationException extends ConstraintViolationException {

	/**
	 * Constructor with a message composed from the class and field
	 * name of the entity causing the exception.
	 * @param className class, which caused the exception
	 * @param fieldName field, which caused the exception
	 */
	public UniqueFieldValueConstraintViolationException(String className, String fieldName) {
		super("class: " + className + " field: " + fieldName);
	}

}
