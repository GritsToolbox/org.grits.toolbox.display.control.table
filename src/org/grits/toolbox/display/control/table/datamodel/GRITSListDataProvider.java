package org.grits.toolbox.display.control.table.datamodel;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;

public class GRITSListDataProvider<T> implements IRowDataProvider<T> {

	protected List<GRITSListDataRow> list;
	
	public GRITSListDataProvider(List<GRITSListDataRow> list) {
		this.list = list;
	}	

	public int getColumnCount() {
		if ( list.isEmpty() )
			return 0;
		GRITSListDataRow rowData = list.get(0);
		return rowData.getDataRow().size();
	}
	
	public int getRowCount() {
		return list.size();
	}

	public Object getDataValue(int columnIndex, int rowIndex) {
		GRITSListDataRow rowData = list.get(rowIndex);
		Object rowObj = rowData.getDataRow().get(columnIndex);
		return rowObj;
	}
	
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		GRITSListDataRow rowData = list.get(rowIndex);
		rowData.getDataRow().set(columnIndex, newValue);
	}
	
	public List<GRITSListDataRow> getList() {
		return list;
	}

	public GRITSListDataRow getGRITSListDataRow(int rowIndex) {
		return (GRITSListDataRow) list.get(rowIndex);
	}
	
	@Override
	public T getRowObject(int rowIndex) {
		// TODO Auto-generated method stub
		return (T) list.get(rowIndex).getDataRow();
	}

	@Override
	public int indexOfRowObject(T rowObject) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
