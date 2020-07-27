package com.github.TKnudsen.ComplexDataObject.data.interfaces;

/**
 * <p>
 * Title: ISelfDescription
 * </p>
 * 
 * <p>
 * Description: ability to provide textual information about the class and
 * actual instance. Beneficial, e.g., when objects are used in
 * visual-interactive interfaces.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.07
 */
public interface ISelfDescription {

	/**
	 * Short name of the instance that shall describe itself.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Long name of the instance that shall describe itself.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Redundant but beneficial: override toString().
	 * 
	 * @return
	 */
	public abstract String toString();
}
