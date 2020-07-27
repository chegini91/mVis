package com.github.TKnudsen.ComplexDataObject.model.processors.utility;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Iterates over the values of a given attribute stored in the supplied ComplexDataContainer and
 * returns a List of all the unique values found.
 * 
 * <p> Assumes that the values to be scanned are stored as lists by each ComplexDataObject.
 * 
 * @author Robert Heimbach
 *
 * @see AbstractUniqueValuesIdentifier
 */
public class UniqueValuesIdentifier extends AbstractUniqueValuesIdentifier {
	
	@Override
	public Set<Object> getUniqueValues() {
		
		if(!(this.hasAttribute() && this.hasDataContainer())){
			return null;
		}
		
		// Get values
		Map<Long, Object> attributeValues = getDataContainer().getAttributeValues(attribute);

		Set<Object> uniqueValues = null;
		
		if (attributeValues != null){
			
			uniqueValues = new HashSet<Object>();
			
			// it is assumed that each ComplexDataObject has stored its value as a List of Items
			for(Object itemList : attributeValues.values()){
				
				// cast it to a collection, because we want to iterate over the list
				if(itemList instanceof List<?>){
					List<?> values = (List<?>) itemList;
					
					for(Object value : values){
						uniqueValues.add(value);
					}
				}
			}
			
		}
		
		return uniqueValues;
	}
}
