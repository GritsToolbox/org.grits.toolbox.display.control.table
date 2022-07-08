package org.grits.toolbox.display.control.table.dialog;

import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnEntry;

/**
 * @author D Brent Weatherly (dbrentw@uga.edu)
 * 
 * Adds a key to the ColumnEntry so columns can have different labels but be considered equal if 
 * they have the same key.
 *
 */
public class GRITSColumnEntry extends ColumnEntry {
	protected String key = "";
	
	public GRITSColumnEntry(String label, Integer index, Integer position) {
		this("", label, index, position);
	}

	public GRITSColumnEntry(String key, String label, Integer index, Integer position) {
		super(label, index, position);
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( ! (obj instanceof GRITSColumnEntry) ) {
			return false;
		}
		return this.getKey().equals(((GRITSColumnEntry) obj).getKey());
	}
	
	
}
