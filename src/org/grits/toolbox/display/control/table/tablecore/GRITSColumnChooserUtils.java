package org.grits.toolbox.display.control.table.tablecore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnChooserUtils;
import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnEntry;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.grits.toolbox.display.control.table.dialog.GRITSColumnEntry;

public class GRITSColumnChooserUtils {

	public static List<ColumnEntry> getHiddenColumnEntries(
			ColumnHideShowLayer columnHideShowLayer,
			ColumnHeaderLayer columnHeaderLayer, DataLayer columnHeaderDataLayer) {
		Collection<Integer> hiddenColumnIndexes = columnHideShowLayer
				.getHiddenColumnIndexes();
		ArrayList<ColumnEntry> hiddenColumnEntries = new ArrayList<ColumnEntry>();

		for (Integer hiddenColumnIndex : hiddenColumnIndexes) {
			String sKey = getColumnKey(columnHeaderLayer,
					columnHeaderDataLayer, hiddenColumnIndex);
			String sLabel = ColumnChooserUtils.getColumnLabel(columnHeaderLayer,
					columnHeaderDataLayer, hiddenColumnIndex);
			GRITSColumnEntry columnEntry = new GRITSColumnEntry(sKey, sLabel, hiddenColumnIndex,	Integer.valueOf(-1));
			hiddenColumnEntries.add(columnEntry);
		}

		return hiddenColumnEntries;
	}

	public static String getColumnKey(ColumnHeaderLayer columnHeaderLayer,
			DataLayer columnHeaderDataLayer, Integer columnIndex) {
		int position = columnHeaderDataLayer.getColumnPositionByIndex(columnIndex.intValue());
		String sKey = ((GRITSColumnHeaderDataProvider) columnHeaderDataLayer.getDataProvider()).getDataKey(position, 0);
		return sKey;
	}
	
    /**
     * Get all visible columns from the selection layer and the corresponding
     * labels in the header
     */
    public static List<ColumnEntry> getVisibleColumnsEntries(
            ColumnHideShowLayer columnHideShowLayer,
            ColumnHeaderLayer columnHeaderLayer, DataLayer columnHeaderDataLayer) {
        int visibleColumnCount = columnHideShowLayer.getColumnCount();
        ArrayList<ColumnEntry> visibleColumnEntries = new ArrayList<ColumnEntry>();

        for (int i = 0; i < visibleColumnCount; i++) {
            int index = columnHideShowLayer.getColumnIndexByPosition(i);
			String sKey = getColumnKey(columnHeaderLayer,
					columnHeaderDataLayer, index);
			String sLabel = ColumnChooserUtils.getColumnLabel(columnHeaderLayer,
					columnHeaderDataLayer, index);
			GRITSColumnEntry columnEntry = new GRITSColumnEntry(sKey, sLabel, Integer.valueOf(index), Integer.valueOf(i));

//			String label = getColumnLabel(columnHeaderLayer,
//                    columnHeaderDataLayer, index);
//            ColumnEntry columnEntry = new ColumnEntry(label,
//                    Integer.valueOf(index), Integer.valueOf(i));
            visibleColumnEntries.add(columnEntry);
        }
        return visibleColumnEntries;
    }


}
