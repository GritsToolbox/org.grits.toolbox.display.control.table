package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.grits.toolbox.display.control.table.datamodel.GRITSTableDataObject;
import org.grits.toolbox.display.control.table.process.TableDataProcessor;

/**
 * Interface that defines the extension of NatTable to GRITSTable
 * 
 * @author D Brent Weatherly (dbrentw@uga.edu)
 */
public interface IGritsTable {
	/**
	 * @return true if the header data has more than 1 row, false otherwise
	 */
	public boolean hasColumnGroupHeader();
	
	/**
	 * Resizes rows that are visible. 
	 */
	public void performAutoResize();

	/**
	 * Updates the positions in the GRITStable based on current preferences 
	 * @return true if no errors encountered
	 */
	public boolean updateViewFromPreferenceSettings();
	
	/**
	 * Updates the preferences in the GRITS table data object based on current column settings.
	 */
	public void updatePreferenceSettingsFromCurrentView();
	
	/**
	 * @return the current column hide/show layer object
	 */
	public ColumnHideShowLayer getColumnHideShowLayer();
	
	/**
	 * @return the current column header data layer object
	 */
	public DataLayer getColumnHeaderDataLayer();	
	
	/**
	 * @return the current column header layer object
	 */
	public ColumnHeaderLayer getColumnHeaderLayer();
	
	/**
	 * @return the current column group model (really only useful for multi-row headers)
	 */
	public ColumnGroupModel getColumnGroupModel();	
	
	/**
	 * @return the current selection layer object
	 */
	public SelectionLayer getSelectionLayer();
	
	/**
	 * @return the data processor responsible for reading/writing data from file and formatting for the table
	 */
	public TableDataProcessor getTableDataProcessor();
	
	
	/**
	 * @return the current GRITS table data object that holds the data read from the project file
	 */
	public GRITSTableDataObject getGRITSTableDataObject();
}
