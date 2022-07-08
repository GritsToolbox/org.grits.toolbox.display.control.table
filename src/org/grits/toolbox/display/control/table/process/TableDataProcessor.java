package org.grits.toolbox.display.control.table.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.core.datamodel.Entry;
import org.grits.toolbox.core.datamodel.property.Property;
import org.grits.toolbox.widgets.processDialog.GRITSProgressDialog;
import org.grits.toolbox.widgets.tools.INotifyingProcess;

import org.grits.toolbox.display.control.table.datamodel.GRITSColumnHeader;
import org.grits.toolbox.display.control.table.datamodel.GRITSListDataRow;
import org.grits.toolbox.display.control.table.preference.TableViewerColumnSettings;
import org.grits.toolbox.display.control.table.preference.TableViewerPreference;

/**
 * @author D. Brent Weatherly (dbrentw@uga.edu)
 *
 */
public abstract class TableDataProcessor 
{
	private static final Logger logger = Logger.getLogger(TableDataProcessor.class);
	
    protected String outputFile = null;
	protected Entry entry = null;  // we need a lot more information than just the original file
    protected boolean bCancel = false;
    protected GRITSProgressDialog progressBarDialog = null;
    protected Shell parentShell = null;
    
    // Process Types
    public final static Integer OPEN = 1;
    public final static Integer READ = 2;
    public final static Integer WRITE = 3;
    public final static Integer INVALID = -1;
    
	protected int iLastVisibleCol = 0;
	private TableViewerPreference tempPreference = null;

    private Integer processType;
    protected Property sourceProperty;
    
    // a static "Selected" column that can be added to a GRITS Table to show checkbox in the first column
    public static final GRITSColumnHeader selColHeader = new GRITSColumnHeader("Selected", "selected");
    public static final GRITSColumnHeader filterColHeader = new GRITSColumnHeader("Filter Result", "filterVal");
    public static final GRITSColumnHeader commentColHeader = new GRITSColumnHeader("Comment", "comment");
    protected List<INotifyingProcess> lLongRunningProcesses = null;    
    
    /**
	 * @param _entry - current Entry
	 * @param _sourceProperty - current property
     */
    public TableDataProcessor(Entry entry, Property sourceProperty)
    {
        this.entry = entry; 
        this.processType = TableDataProcessor.OPEN;
        this.sourceProperty= sourceProperty; 
        this.lLongRunningProcesses = new ArrayList<>();
    }
        
	/**
	 * @return boolean - success/fail of reading the data file
	 */
	public abstract boolean readDataFromFile();	
	
	/**
	 * @return boolean - success/fail of saving changes to data file
	 * @throws Exception
	 */
	public abstract boolean saveChanges() throws Exception;	
	
	/**
	 * @return boolean - success/fail of populating a GRITSTableDataObject
	 * @throws Exception
	 */
	public abstract boolean createTable() throws Exception;
			    
	/**
	 * @param iLastVisibleCol - the last visible column if a row is to be "invisible" (shows some data, hides others)
	 */
	public void setLastVisibleCol(int iLastVisibleCol) {
		this.iLastVisibleCol = iLastVisibleCol;
	}
	
	/**
	 * @return int - iLastVisibleCol
	 */
	public int getLastVisibleCol() {
		return iLastVisibleCol;
	}
	
	/**
	 * @param parentShell - current active shell of the calling parent window
	 */
	public void setParentShell(Shell parentShell) {
		this.parentShell = parentShell;
	}
	
	/**
	 * @return Shell - the parentShell
	 */
	public Shell getParentShell() {
		return parentShell;
	}
	
	/**
	 * @return Property - the source property of the Entry
	 */
	public Property getSourceProperty() {
		return sourceProperty;
	}
	
	/**
	 * @param sourceProperty - the source property of the Entry
	 */
	public void setSourceProperty(Property sourceProperty) {
		this.sourceProperty = sourceProperty;
	}
	
	public Entry getEntry() {
		return entry;
	}
	
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
		
    /**
     * @param progressBarDialog - a GRITSProgressDialog for tracking progress for loading and populating tables, can be null
     */
    public void setProgressBarDialog(GRITSProgressDialog progressBarDialog) {
		this.progressBarDialog = progressBarDialog;
	}
    
    /**
     * @return GRITSProgressDialog - progress dialog for tracking progress
     */
    public GRITSProgressDialog getProgressBarDialog() {
		return progressBarDialog;
	}
    
    /**
     * @return Integer - current process type of the processor
     * 
     * Can be OPEN, READ, WRITE, INVALID. See static values in TableDataProcessor
     */
    public Integer getProcessType() {
		return processType;
	}
    
    /**
     * @param processType
     */
    public void setProcessType(Integer processType) {
		this.processType = processType;
	}
    
    /**
     * @param _sNewFile - a data file to be written
     * 
     * Description: sets the output file to be written and sets the process type to WRITE
     */
    public void prepareThreadForWrite( String _sNewFile ) {
    	this.outputFile = _sNewFile;
    	setProcessType(TableDataProcessor.WRITE);
    }
       
	/**
	 * Sets cancel to true to alert loops in the processor and 
	 * alerts all implementing INotifyingProcesses that process has been canceled.
	 */
	public void cancelWork() {
		bCancel = true;		
		Iterator<INotifyingProcess> itr = lLongRunningProcesses.iterator();
		while( itr.hasNext() ) {
			itr.next().setCanceled(true);
		}
	}
            
	/**
	 * @return boolean - whether or not cancel has been hit
	 */
	public boolean isCanceled() {
		return bCancel;		
	}
	
    public void close() throws IOException
    {
    	// TODO: implement this?
    }

    /**
     * @param iMaxNumCols - maximum number of columns in this row
     * @param iId - a unique identifier for this row
     * @return GRITSListDataRow - a row to be displayed in a GRITS Table
     * 
     * Description: Instantiates and initializes a blank GRITSListDataRow to be stored in GRITSTableDataObject
     */
    public static GRITSListDataRow getNewRow( int iMaxNumCols, int iId ) {
       	ArrayList<Object> alRow = new ArrayList<Object>(iMaxNumCols);
    	for( int i = 0; i < iMaxNumCols; i++ ) {
    		alRow.add(null);
    	}
       	GRITSListDataRow newRow = new GRITSListDataRow(iId, alRow);
    	return newRow;
    }
    
	/**
	 * @return TableViewerColumnSettings - instantiates a generic TableViewerColumnSettings object.
	 * 
	 * Should be overridden by extending classes
	 */
	protected TableViewerColumnSettings getNewTableViewerSettings() {
		return new TableViewerColumnSettings();
	}
    
	/**
	 * @return TableViewerColumnSettings - instantiates and initializes a generic TableViewerColumnSettings object.
	 * 
	 * Should be overridden by extending classes
	 */
	protected TableViewerColumnSettings initializeColumnSettings() {
		try {
			TableViewerColumnSettings newSettings = getNewTableViewerSettings();
			return newSettings;
		} catch( Exception e ) {
			logger.error("initializeColumnSettings: unable to initialize all columns.", e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return a TableViewerPreference object
	 * 
	 * Description: TableViewerPreference is a generic Preference object. Each implementing processor should have a
	 * specific Preference object and should override this method to instantiate it with the specific instance.
	 */
	protected TableViewerPreference getNewTableViewerPreferences() {
		return new TableViewerPreference();
	}

	/**
	 * @return TableViewerPreferences
	 * 
	 * Description: Instantiates and initializes a TableViewerPreference, should be overridden by every implementing processor
	 */
	protected TableViewerPreference initializePreferences() {
		try {
			TableViewerColumnSettings newSettings = initializeColumnSettings();
			TableViewerPreference newPreferences = getNewTableViewerPreferences();
			newPreferences.setPreferenceSettings(newSettings);
			return newPreferences;
		} catch( Exception e ) {
			logger.error("initializePreferences: unable to initialize preferences.", e);
		}
		return null;
	}
	
	/**
	 * @return List<INotifyingProcess> - list of registered INotifyingProcesses that interact with the progress dialog
	 */
	public List<INotifyingProcess> getlLongRunningProcesses() {
		return lLongRunningProcesses;
	}
	
	/**
	 * @return TableViewerPreference - a temporary TableViewerPreference object instantiated new and compared to persistent settings
	 */ 
	public TableViewerPreference getTempPreference() {
		return tempPreference;
	}
	
	/**
	 * @param tempPreference - a temporary TableViewerPreference object instantiated new and compared to persistent settings
	 */
	public void setTempPreference(TableViewerPreference tempPreference) {
		this.tempPreference = tempPreference;
	}
    
 }