package com.github.TKnudsen.ComplexDataObject.model.processors.utility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Splits a String which is assumed to be a list or itemization
 * of elements separated by a delimiter. Call {@link #split(Object)} 
 * to split the provided String according to the rules set in the constructor.
 * <p>
 * On default settings the comma is assumed to be the delimiter
 * and whitespace trimming is applied.
 * 
 * @author Robert Heimbach
 *
 */
public class StringSplitter implements IItemSplitter {

	private String delimiter = ",";
	private boolean trimming = true;

	public StringSplitter(){
	}
	
	public StringSplitter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public StringSplitter(boolean trimming){
		this.trimming  = trimming;
	}
	
	public StringSplitter(String delimiter, boolean trimming){
		this.delimiter = delimiter;
		this.trimming = trimming;
	}
	
	/**
	 * Splits the Object toSplit, which is required to be a String, according to the 
	 * rules defined in the constructor.
	 * 
	 * @param toSplit Object to be split. Assumed to be a String.
	 * @return The split String. Possibly null.
	 */
	public List<String> split(Object toSplit){
		
		if(toSplit == null || !(toSplit instanceof String)){
			return null;
		}
		
		String stringToSplit = (String) toSplit;
		
		// Split the string.
		String[] elements = stringToSplit.split(delimiter);
		
		// Apply trimming if needed.
		List<String> foundElements = new LinkedList<String>();
		
		if(trimming) {
			for (String ele : elements){
				foundElements.add(ele.trim());
			}
		}
		else {
			foundElements = Arrays.asList(elements);
		}
		
		return foundElements;
	}

}
