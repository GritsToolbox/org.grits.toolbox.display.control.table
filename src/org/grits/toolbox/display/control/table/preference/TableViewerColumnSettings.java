package org.grits.toolbox.display.control.table.preference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.grits.toolbox.display.control.table.datamodel.GRITSColumnHeader;
import org.grits.toolbox.display.control.table.tablecore.GRITSTable;

/**
 * Base components for storing GRITSTable column headers in table viewer preferences.
 * 
 * @author D Brent Weatherly (dbrentw@uga.edu)
 * @see GRITSColumnHeader
 * @see GRITSTable
 *
 */
public class TableViewerColumnSettings {
	// mapping of column headers to their positions
	protected LinkedHashMap<GRITSColumnHeader, Integer> htColumnHeaderVisible;
	// these are the delimiters used in marshaling column settings to preferences
	public static String TOK_DELIMITER = "~~~";
	public static String COL_DELIMITER = "===";
	public static String COL_VIS_ATTR = "columnVisibility";
	// used to support user-specified columns
	protected HashMap<String, Integer> htUnrecognizedHeaders = new HashMap<>();
	
	public TableViewerColumnSettings() {
		htColumnHeaderVisible = new LinkedHashMap<GRITSColumnHeader, Integer>();	
	}

	/**
	 * @return whether or not the preferences has some unrecognized headers to handle
	 */
	public boolean hasUnrecognizedHeaders() {
		return ! htUnrecognizedHeaders.isEmpty();
	}
	
	/**
	 * @return the mapping of unrecognized column header keys to their preferred positions
	 */
	public HashMap<String, Integer> getUnrecognizedHeaders() {
		return htUnrecognizedHeaders;
	}
	
	/**
	 * @return
	 * 		the number of keys in the visibility table
	 */
	public int getNumColumns() {
		return this.htColumnHeaderVisible.size();		
	}

	/**
	 * Tries to find a GRITS column header by key and return the object
	 * @param _sKey
	 * 		The key of interest
	 * @return
	 */
	public GRITSColumnHeader getColumnHeader( String _sKey ) {
		for ( GRITSColumnHeader header : keySet() ) {
			if ( header.getKeyValue().equals(_sKey) )
				return header;
		}
		return null;
	}

	/**
	 * Tries to find a GRITS column header by key and return its position
	 * @param _sKey
	 * 		The key of interest
	 * @return
	 */
	public int getColumnPosition( String _sKey ) {
		for ( GRITSColumnHeader header : keySet() ) {
			if ( header.getKeyValue().equals(_sKey) )
				return getColumnPosition(header);
		}
		return -1;
	}
	
	public int getColumnPosition( GRITSColumnHeader _headerObj ) {
		if ( this.htColumnHeaderVisible.containsKey(_headerObj) ) 
			return this.htColumnHeaderVisible.get(_headerObj);
		return -1;
	}
	
	public void putColumn(  GRITSColumnHeader _colHeader, int _iPosition )  {
		this.htColumnHeaderVisible.put(_colHeader, _iPosition);
	}
	
	public void putColumn(  String _sLabel, String _sKey, int _iPosition )  {
		this.htColumnHeaderVisible.put(new GRITSColumnHeader(_sLabel, _sKey), _iPosition);
	}
	
	/**
	 * Creates a new column header object and adds it to the visible list by specifying its position as the current
	 * number of columns (the end of the list)
	 * @param _sLabel
	 * @param _sKey
	 */
	public void addColumn( String _sLabel, String _sKey ) {
		this.htColumnHeaderVisible.put( new GRITSColumnHeader(_sLabel, _sKey), getNumColumns() );
	}

	/**
	 * Adds the specified column header to the visible list by specifying its position as the current
	 * number of columns (the end of the list)
	 * @param _sLabel
	 * @param _sKey
	 */
	public void addColumn( GRITSColumnHeader _colHeader ) {
		this.htColumnHeaderVisible.put( _colHeader, getNumColumns() );
	}
	
	public Set<GRITSColumnHeader> keySet() {
		return this.htColumnHeaderVisible.keySet();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		TableViewerColumnSettings newSettings = new TableViewerColumnSettings();
		newSettings.htColumnHeaderVisible =  this.htColumnHeaderVisible;
		// DBW 06/16/17: I noticed I wasn't cloning the header visibility, so this isn't really cloning.
		// We should test actual cloning (below)
//		newSettings.htColumnHeaderVisible = (LinkedHashMap<GRITSColumnHeader, Integer> ) this.htColumnHeaderVisible.clone();
		return newSettings;
	}
	
	public Set<GRITSColumnHeader> getHeaders() {
		return htColumnHeaderVisible.keySet();
	}
	
	/**
	 * Sets the position of the specified column header
	 * @param header
	 * @param _iVal
	 */
	public void setVisColInx( GRITSColumnHeader header, Integer _iVal ) {
		this.htColumnHeaderVisible.put(header, _iVal);
	}
	
	/**
	 * Tries to find a GRITS column header by key and return whether it exists
	 * @param _sKey
	 * 		The key of interest
	 * @return
	 * 		true if found, false otherwise
	 * @return
	 */
	public boolean hasColumn( String _sKey ) {
		GRITSColumnHeader header = getColumnHeader(_sKey);
		return ( header != null );
	}
	
	/**
	 * Tries to find a GRITS column header by key and return whether it is visible
	 * @param _sKey
	 * 		The key of interest
	 * @return
	 * 		true if found and visible (position != -1), false otherwise
	 */
	public boolean isVisible( String _sKey ) {
		GRITSColumnHeader header = getColumnHeader(_sKey);
		if ( header != null )
			return this.htColumnHeaderVisible.get(header) != -1;
		return false;
	}

	/**
	 * Tries to find a GRITS column header by key and return its position
	 * @param _sKey
	 * 		The key of interest
	 * @return
	 * 		the visible column index of the header (-1 if not found or invisible)
	 */
	public int getVisColInx( String _sKey ) {
		GRITSColumnHeader header = getColumnHeader(_sKey);
		if ( header != null )
			return this.htColumnHeaderVisible.get(header);	
		return -1;
	}

	/**
	 * Locates the GRITS column header at the specified position.
	 * @param _iInx
	 * 		column index
	 * @return
	 * 		the column header if found, null otherwise
	 */
	public GRITSColumnHeader getColumnAtVisColInx( Integer _iInx ) {
		for( GRITSColumnHeader header : keySet() ) {
			Integer iInx = this.htColumnHeaderVisible.get(header);
			if ( iInx == _iInx )
				return header;
		}
		return null;
	}
	
	/**
	 * @return text representation of column headers by key and position
	 */
	public String toColumnVisibilityString() {
		StringBuilder sb = new StringBuilder();
		for( GRITSColumnHeader header : keySet() ) {
			if( ! sb.toString().equals("") ) {
				sb.append(COL_DELIMITER);
			}
			Integer iVal = this.htColumnHeaderVisible.get(header);
			String sVal = header.getKeyValue() + TOK_DELIMITER + Integer.toString(iVal);			
			sb.append(sVal);
			
		}
		return sb.toString();
	}	
	
	/**
	 * @return text representation of column headers by key and label
	 */
	public String toColumnHeaderString() {
		StringBuilder sb = new StringBuilder();
		for( GRITSColumnHeader header : keySet() ) {
			if( ! sb.toString().equals("") ) {
				sb.append(COL_DELIMITER);
			}
			String sVal = header.getKeyValue() + TOK_DELIMITER + header.getLabel();			
			sb.append(sVal);
			
		}
		return sb.toString();
	}	

}
