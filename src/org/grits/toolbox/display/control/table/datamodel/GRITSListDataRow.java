package org.grits.toolbox.display.control.table.datamodel;

import java.util.ArrayList;

public class GRITSListDataRow {
	private int iId;
	protected ArrayList<Object> lDataRow;
	
	public GRITSListDataRow(int iId, ArrayList<Object> lDataRow) {		
		this.iId = iId;
		this.lDataRow = lDataRow;
	}
	
	public int getId() {
		return iId;
	}
	
	public ArrayList<Object> getDataRow() {
		return lDataRow;
	}
	
	@Override
	public Object clone() {
		GRITSListDataRow newRow = new GRITSListDataRow(this.iId, (ArrayList<Object>) lDataRow.clone());
		return newRow;
	}
		
	
}
