package com.github.TKnudsen.ComplexDataObject.model.processors.utility;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;

/**
 * Default implementations of the getter and setter methods of the {@link IUniqueValuesIdentifier} 
 * interface and definition of constructors allowing none, one or both of the input 
 * arguments to be specified.
 * 
 * @author Robert Heimbach
 *
 */
public abstract class AbstractUniqueValuesIdentifier implements IUniqueValuesIdentifier {
	
	ComplexDataContainer container;
	String attribute;

	public AbstractUniqueValuesIdentifier(){
	}
	
	public AbstractUniqueValuesIdentifier(ComplexDataContainer container){
		this.container = container;
	}
	
	public AbstractUniqueValuesIdentifier(String attribute){
		this.attribute = attribute;
	}
	
	public AbstractUniqueValuesIdentifier(ComplexDataContainer container, String attribute) {
		this.container = container;
		this.attribute = attribute;
	}
	
	public String getAttribute(){
		return this.attribute;
	}
	
	public void setAttribute(String newAttribute){
		this.attribute = newAttribute;
	}
	
	public Boolean hasAttribute(){
		return (this.attribute != null);
	}
	
	public ComplexDataContainer getDataContainer(){
		return this.container;
	}
	
	public void setDataContainer(ComplexDataContainer newContainer){
		this.container = newContainer;
	}
	
	public Boolean hasDataContainer(){
		return (this.container != null);
	}
}
