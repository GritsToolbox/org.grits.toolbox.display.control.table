package org.grits.toolbox.display.control.table.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;
import org.grits.toolbox.core.datamodel.UnsupportedVersionException;
import org.grits.toolbox.core.preference.share.PreferenceEntity;
import org.grits.toolbox.core.preference.share.PreferenceReader;
import org.grits.toolbox.core.preference.share.PreferenceWriter;
import org.grits.toolbox.core.utilShare.XMLUtils;

import org.grits.toolbox.display.control.table.datamodel.GRITSColumnHeader;

/**
 * Base components needed for providing persistent preferences for GRITSTable
 * @author D Brent Weatherly (dbrentw@uga.edu)
 * @see GRITSTable
 */
@XmlRootElement(name = "tableViewerPreference")
public class TableViewerPreference {
	private static final Logger logger = Logger.getLogger(TableViewerPreference.class);
	private static final String PREFERENCE_NAME_ALL = "org.grits.toolbox.display.control.table.preference.TableViewerPreference";
	/*
	 * Version history:
	 * 1.0 - Original release w/ versioning
	 * 1.1 - Committed 03/08/16. Added the "columnHeaders" variable w/ support to map column key to column label in preferences
	 */
	private static final String CURRENT_VERSION = "1.1";

	private String columnSettings = "";	
	private String columnHeaders = "";
	protected TableViewerColumnSettings preferenceSettings = null;
	
	public TableViewerPreference()
	{
		super();
	}
		
	public static String getPreferenceID() {
		return PREFERENCE_NAME_ALL;
	}

	public String getColumnSettings() {
		return columnSettings;
	}
	@XmlAttribute(name="columnSettings")
	public void setColumnSettings(String columnSettings) {
		this.columnSettings = columnSettings;
	}

	public String getColumnHeaders() {
		return columnHeaders;
	}
	@XmlAttribute(name="columnHeaders")
	public void setColumnHeaders(String columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	
	/**
	 * In the case where a project's preferences differ from the workspace preferences, attempt
	 * to merge the headers into a single set.
	 * 
	 * @param _newSettings
	 * 		Column settings to be merged with the current preference settings
	 */
	public void mergeSettings( TableViewerColumnSettings _newSettings ) {
		for( GRITSColumnHeader colHeader : _newSettings.getHeaders() ) {
			if( ! getPreferenceSettings().hasColumn(colHeader.getKeyValue())) {
				getPreferenceSettings().putColumn(colHeader, -1);
			}
		}
	}
	
	public TableViewerColumnSettings getPreferenceSettings() {
		return preferenceSettings;
	}
	@XmlTransient
	public void setPreferenceSettings(TableViewerColumnSettings preferenceSettings) {
		this.preferenceSettings = preferenceSettings;
	}
	
	public boolean settingsNeedInitialization() {
		return this.preferenceSettings == null || this.preferenceSettings.getNumColumns() == 0;
	}

	protected TableViewerColumnSettings getNewTableSettings() {
		TableViewerColumnSettings spf = new TableViewerColumnSettings();
		return spf;
	}

	protected TableViewerPreferenceReader getNewReader() {
		return new TableViewerPreferenceReader();
	}
	
	public static TableViewerPreference readPreference() {
		return TableViewerPreferenceLoader.getTableViewerPreference();
	}
		
	/**
	 * Converts the String representation of the preference object and converts to the formal object.
	 * 
	 * @param xmlString
	 * 		text-based representation of preferences (stored in project)
	 * @param prefClass
	 * 		the Class to be created
	 * @return
	 */
	private static TableViewerPreference getTableViewerPreferenceFromXML(String xmlString, Class<?> prefClass) {
		TableViewerPreference preferences = (TableViewerPreference) XMLUtils.getObjectFromXML(xmlString, prefClass);
		return preferences;
	}

	/**
	 * Top-level method for creating an XML text representation of column preference settings.
	 * @param object
	 * 		the preference object to be marshalled to XML 
	 * @return
	 * 		String representation of column setting preferences
	 */
	public static String marshalXML(TableViewerPreference object) {
		String xmlString = XMLUtils.marshalObjectXML(object);
		return xmlString ;
	}
	
	/**
	 * Creates the text-based representation of the column keys to labels and column keys to positions and 
	 * then writes to xml.
	 * @return a XML-based text representation of the column preference settings
	 */
	protected String marshalXML() {		
		setColumnSettings(getPreferenceSettings().toColumnVisibilityString());
		setColumnHeaders(getPreferenceSettings().toColumnHeaderString());
		return TableViewerPreference.marshalXML(this);
	}
	
	/**
	 * Called to read preference data from file and the initializing the text-based column settings
	 * to the column settings object.
	 * @param preferenceEntity
	 * @param prefClass
	 * @return
	 * @throws UnsupportedVersionException
	 */
	public static TableViewerPreference getTableViewerPreference(PreferenceEntity preferenceEntity, Class<?> prefClass) throws UnsupportedVersionException {
		TableViewerPreference preferences = null;
		if(preferenceEntity != null) {
			preferences = TableViewerPreference.getTableViewerPreferenceFromXML(preferenceEntity.getValue(), prefClass);
			TableViewerPreference.initTableViewerColumnSettings(preferences);
		}
		return preferences;
	}

	/**
	 * Writes the preferences to file. Should be overridden by sub-classing plug-ins. 
	 * First marshals the column settings to a text value in order to use as 
	 * the preference entity value. It is then written to file.
	 * @return true if successful, false otherwise.
	 */
	public boolean writePreference() {
		PreferenceEntity preferenceEntity = new PreferenceEntity(getPreferenceID());
		preferenceEntity.setVersion(CURRENT_VERSION);
		preferenceEntity.setValue(marshalXML());
		return PreferenceWriter.savePreference(preferenceEntity);
	}

	/**
	 * Should be overridden by sub-classing plug-ins. Reads preferences from file.
	 * @return
	 * @throws UnsupportedVersionException 
	 * 		if there is no registered reader for the version of preference object 
	 */
	public static PreferenceEntity getPreferenceEntity() throws UnsupportedVersionException {
		PreferenceEntity preferenceEntity = PreferenceReader.getPreferenceByName(TableViewerPreference.getPreferenceID());
		return preferenceEntity;
	}
	
	/**
	 * Takes the text-representation of the column headers (by key and position) and 
	 * creates a map of position to list of position,label tuples 
	 * @param sColumnSettings
	 * @return 
	 * 		a map of position to list of position,label tuples 
	 */
	protected Map<Integer, List<String[]>> getPreferenceMapFromColumnSettings(String sColumnSettings) {
		Map<Integer, List<String[]>> mColNumToPrefs = new HashMap<>();
		if(sColumnSettings.length() != 0) {		
			String[] sColHeaders = sColumnSettings.split( TableViewerColumnSettings.COL_DELIMITER );
			for( int i = 0; i < sColHeaders.length; i++ ) {
				String[] sColAndVis = sColHeaders[i].split( TableViewerColumnSettings.TOK_DELIMITER );
				Integer iVisColInx = -1;
				try {
					iVisColInx = Integer.parseInt( sColAndVis[1] );
				} catch( NumberFormatException ex ) {					
					iVisColInx = -1;
				} 
				List<String[]> lPrefInfo = null;
				if( mColNumToPrefs.containsKey(iVisColInx) ) {
					lPrefInfo = mColNumToPrefs.get(iVisColInx);
				} else {
					lPrefInfo = new ArrayList<>();
					mColNumToPrefs.put(iVisColInx, lPrefInfo);
				}
				lPrefInfo.add(sColAndVis);				
			}
		}
		return mColNumToPrefs;
	}

	/**
	 * Takes the text-representation of the column headers (by key and label) and 
	 * creates a map of keys to labels
	 * @param sColumnHeaders
	 * @return a map of keys to labels
	 */
	protected Map<String, String> getHeaderMapFromColumnSettings(String sColumnHeaders) {
		Map<String, String> mKeyToLabel = new HashMap<>();
		if(sColumnHeaders.length() != 0) {		
			String[] sColHeaders = sColumnHeaders.split( TableViewerColumnSettings.COL_DELIMITER );
			for( int i = 0; i < sColHeaders.length; i++ ) {
				String[] sKeyToLabel = sColHeaders[i].split( TableViewerColumnSettings.TOK_DELIMITER );
				if( excludeColumn(sKeyToLabel[0], sKeyToLabel[1]) ) {
					continue;
				}
				mKeyToLabel.put(sKeyToLabel[0], sKeyToLabel[1]);
			}
		}
		return mKeyToLabel;
	}
	
	/**
	 * This method should be overridden by subclasses plugins. Used to skip columns that are no longer 
	 * desired. A way to deprecate older columns that are in preferences but no longer used.
	 * 
	 * @param _sColumnKey
	 * 		The key value of the GRITSColumnHeader object 
	 * @param _sColumnLabel
	 * 		The label of the GRITSColumnHeader object
	 * @return true if excluded, false otherwise
	 * @see GRITSColumnHeader
	 */
	protected boolean excludeColumn( String _sColumnKey, String _sColumnLabel ) {
		return false;
	}
	
	/**
	 * Sorts the visible column indices
	 * @param _sCols
	 * 		a set of column indices as read from the preferences
	 * @return
	 */
	private static List<Integer> getSortedCols( Set<Integer> _sCols ) {
		List<Integer> lSortedCols = new ArrayList<>();
		Iterator<Integer> i = _sCols.iterator();
		while( i.hasNext() ) {
			Integer iColNum = i.next();
			lSortedCols.add(iColNum);
		}
		Collections.sort(lSortedCols);
		return lSortedCols;
	}

	/**
	 * This method should be overridden by subclassing plug-ins.
	 * @param _sKey
	 * 		the key for a a column
	 * @return the column header if found, else null
	 */
	public GRITSColumnHeader getColumnHeader(String _sKey) {
		return null;
	}

	/**
	 * After reading from file the text-representation of column settings (columnSettings, which must be 
	 * set in provided preference object), this method initializes a new TableViewerColumnSettings object 
	 * and attempts to translate the text-representation into the formal object.
	 * 
	 * @param preference
	 * @see TableViewerColumnSettings
	 * @see GRITSColumnHeader
	 */
	protected static void initTableViewerColumnSettings(TableViewerPreference preference ) {
		try {
			if( preference == null || preference.getColumnSettings() == null ) {
				return;
			}
			
			// takes the text-representation of column settings and creates a mapping of
			// the visible column index to the tuple of [column_key, preferred index]
			// Note that only -1 (for not visible) should have multiple entries
			Map<Integer, List<String[]>> mColNumToPrefs = preference.getPreferenceMapFromColumnSettings(preference.getColumnSettings());
			if(mColNumToPrefs == null || mColNumToPrefs.isEmpty()) {
				return;
			}
			// create a mapping of the column key to the column label
			Map<String, String> mKeyToLabel = preference.getHeaderMapFromColumnSettings(preference.getColumnHeaders());
			
			// get the visible column indices sorted
			List<Integer> lSortedCols = getSortedCols( mColNumToPrefs.keySet() );
			Iterator<Integer> itr = lSortedCols.iterator();
			
			// starting at zero-based order. we reorder here just in case the stored preferences
			// are messed up (don't start at zero, aren't contiguous, etc). We preserve the order
			// but make sure it is clean
			int iVisColInx = 0;
			TableViewerColumnSettings spf = preference.getNewTableSettings();
			// iterate over the sorted columns
			while( itr.hasNext() ) {
				Integer iColNum = itr.next();
				List<String[]> lCols = mColNumToPrefs.get(iColNum);

				if( iColNum >= 0 && lCols.size() > 1 ) { // error in preferences, needs to be reset
					// if this happens, there are more than 1 columns at a given position, and that can't happen.
					// report the errror and make the user reset them
					String errorMsg = "Invalid column preferences detected for column number " + iColNum;
					logger.error(errorMsg);
				}
				Iterator<String[]> itr2 = lCols.iterator();
				while( itr2.hasNext() ) {
					String[] sColAndVis = itr2.next();
					GRITSColumnHeader header = preference.getColumnHeader(sColAndVis[0]);
					if( header == null && mKeyToLabel != null && mKeyToLabel.containsKey(sColAndVis[0]) ) { // try again from preference
						header = new GRITSColumnHeader(mKeyToLabel.get(sColAndVis[0]), sColAndVis[0]);
					}
					// here's where the collapsing happens. 
					// If the position is -1 (invisible) then stay that way,
					// otherwise take the next contiguous position
					int iColInx = iColNum > 0 ? iVisColInx : iColNum;
					if ( header != null )
						spf.setVisColInx(header, iColInx);
					else {
						// Column header in preferences that isn't part of the "standard" set. This can happen with 
						// user-added data. We store these in a separate map and then handle them later
						logger.warn("Unrecognized column in preferences: " + sColAndVis[0]);
						spf.getUnrecognizedHeaders().put(sColAndVis[0], new Integer(iColInx));
					}
					if( iColInx >= 0 ) {
//						logger.debug("Preference:  header: " + sColAndVis[0] + ", pref col: " + iVisColInx);
						iVisColInx++;
					}
				}
			}
			// assuming we're successful, then set the setting object for this preference object
			if ( spf.getNumColumns() > 0 ) {
				preference.setPreferenceSettings(spf);
			}
		} catch( Exception ex ) {
			String errorMsg = "An error occurred loading preferences";
			logger.error(errorMsg, ex);
		}
	}
	
}