package org.grits.toolbox.display.control.table.datamodel;

import java.util.Comparator;

public class GRITSComparator implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		//System.out.println(arg0+","+arg1);
		if(((Double)arg0).equals((Double) arg1))
		{
			return 0;
		}
		return ((Double) arg0).compareTo((Double) arg1);
	}
	
}
