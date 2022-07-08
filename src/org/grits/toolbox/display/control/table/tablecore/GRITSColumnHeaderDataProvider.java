package org.grits.toolbox.display.control.table.tablecore;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

import org.grits.toolbox.display.control.table.datamodel.GRITSColumnHeader;

public class GRITSColumnHeaderDataProvider implements IDataProvider {
	List<GRITSColumnHeader> header = null;
	public GRITSColumnHeaderDataProvider( List<GRITSColumnHeader> list ) {
		this.header = list;
	}
	
	public int getColumnCount() {
		return header.size();
	}

	public int getRowCount() {
		return 1;
	}

	public String getDataKey(int columnIndex, int rowIndex) {
		// output column names
		List<GRITSColumnHeader> list = header;
		if (columnIndex < 0 || columnIndex >= list.size()) {
			return null;
		}
		// String sVal = list.get(columnIndex).getLabel();
		return list.get(columnIndex).getKeyValue();
	}

	public String getDataValue(int columnIndex, int rowIndex) {
		// output column names
		List<GRITSColumnHeader> list = header;
		if (columnIndex < 0 || columnIndex >= list.size()) {
			return null;
		}
		// String sVal = list.get(columnIndex).getLabel();
		return list.get(columnIndex).getLabel();
	}

	public void setDataValue(int columnIndex, int rowIndex,
			Object newValue) {
	}

}
