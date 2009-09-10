package util;

import java.util.Comparator;

import javax.faces.model.SelectItem;


public class SelectedItemComparator implements Comparator<SelectItem> {

	public int compare(SelectItem o1, SelectItem o2) {
		
		return o1.getLabel().compareTo(o2.getLabel());
			
	}
	
	
}
