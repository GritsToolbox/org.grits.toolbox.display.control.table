package org.grits.toolbox.display.control.table.datamodel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.grits.toolbox.display.control.table.preference.TableViewerPreference;

public class GRITSTableDataObject {
	protected ArrayList<GRITSListDataRow> tableData = null; // intentionally generic JIC
	protected ArrayList<ArrayList<GRITSColumnHeader>> headerData = null;
    protected Hashtable<Integer, Hashtable<String, ArrayList<String>>> htHiddenRows = null; // hash with parent scan num, then concat "peak_id:scan_num"
    protected Hashtable<Integer, ArrayList<String>> htInvisibleRows = null; // hash with parent scan num, then concat "peak_id:scan_num" (scan num may not exist)
    protected Hashtable<Integer, ArrayList<String>> htManuallyChanged = null; // hash with parent scan num, then concat "peak_id:scan_num" (scan num may not exist)
    protected Hashtable<Integer, ArrayList<String>> htLocked = null; // hash with parent scan num, then concat "peak_id:scan_num" (scan num may not exist)
    protected TableViewerPreference tablePreferences = null;
    protected String sAnalysisName = null;
    
    public GRITSTableDataObject() {
        this.tableData = new ArrayList<GRITSListDataRow>();
        this.headerData = new ArrayList<ArrayList<GRITSColumnHeader>>(); // Array of Arrays to handle multiple rows, JIC
        this.htHiddenRows = new Hashtable<Integer, Hashtable<String, ArrayList<String>>>();
        this.htInvisibleRows = new Hashtable<Integer, ArrayList<String>>();
        this.htManuallyChanged = new Hashtable<Integer, ArrayList<String>>();
        this.htLocked = new Hashtable<Integer, ArrayList<String>>();
 	}
         
    public List<GRITSColumnHeader> getLastHeader() {
		int iLastHeader = getTableHeader().size() - 1;
		if( iLastHeader < 0 ) {
			return null;
		}
		List<GRITSColumnHeader> lastHeader = getTableHeader().get(iLastHeader);
		return lastHeader;
	}
    
    public void setAnalysisName(String sAnalysisName) {
		this.sAnalysisName = sAnalysisName;
	}
    
    public String getAnalysisName() {
		return sAnalysisName;
	}
        
    public void initializePreferences() {
    	setTablePreferences(TableViewerPreference.readPreference());
    }
    
    public void setTablePreferences(TableViewerPreference tablePreferences) {
		this.tablePreferences = tablePreferences;
	}
    
    public TableViewerPreference getTablePreferences() {
		return tablePreferences;
	}
      
    public Hashtable<Integer, ArrayList<String>> getInvisibleRows() {
    	return this.htInvisibleRows;
    }

    public Hashtable<Integer, ArrayList<String>> getManuallyChangedPeaks() {
    	return this.htManuallyChanged;
    }
    
    public ArrayList<GRITSListDataRow> getTableData() {
    	return tableData;
    }
    
    public void setTableData(ArrayList<GRITSListDataRow> tableData) {
		this.tableData = tableData;
	}
    
    public ArrayList<ArrayList<GRITSColumnHeader>> getTableHeader() {
		return headerData;
	}
    
    public void setHeaderData(
			ArrayList<ArrayList<GRITSColumnHeader>> headerData) {
		this.headerData = headerData;
	}
    
    public void addInvisibleRow( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alInvisibleRows = null;
    	if ( ! this.htInvisibleRows.containsKey(_iParentScanNum) ) {
    		alInvisibleRows = new ArrayList<String>();
    		this.htInvisibleRows.put(_iParentScanNum, alInvisibleRows);
    	} else {
    		alInvisibleRows = this.htInvisibleRows.get(_iParentScanNum);
    	}
    	alInvisibleRows.add(_iRowId);
    	
    }

    public void addManuallyChangedPeak( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alManuallyChangedRows = null;
    	if ( ! this.htManuallyChanged.containsKey(_iParentScanNum) ) {
    		alManuallyChangedRows = new ArrayList<String>();
    		this.htManuallyChanged.put(_iParentScanNum, alManuallyChangedRows);
    	} else {
    		alManuallyChangedRows = this.htManuallyChanged.get(_iParentScanNum);
    	}
    	alManuallyChangedRows.add(_iRowId);
    }
    
    public void addLockedPeak (Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alLockedRows = null;
    	if ( ! this.htLocked.containsKey(_iParentScanNum) ) {
    		alLockedRows = new ArrayList<String>();
    		this.htLocked.put(_iParentScanNum, alLockedRows);
    	} else {
    		alLockedRows = this.htLocked.get(_iParentScanNum);
    	}
    	alLockedRows.add(_iRowId);
    }
    
    public void removeInvisibleRow( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alInvisibleRows = null;
    	if ( ! this.htInvisibleRows.containsKey(_iParentScanNum) ) {
    		return;
    	} else {
    		alInvisibleRows = this.htInvisibleRows.get(_iParentScanNum);
    	}
    	alInvisibleRows.remove( _iRowId);
    }

    public void removeManuallyChangedPeak( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alManuallyChangedRows = null;
    	if ( ! this.htManuallyChanged.containsKey(_iParentScanNum) ) {
    		return;
    	} else {
    		alManuallyChangedRows = this.htManuallyChanged.get(_iParentScanNum);
    	}
    	alManuallyChangedRows.remove( _iRowId);
    }
    
    public void removeLockedPeak( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	ArrayList<String> alManuallyChangedRows = null;
    	if ( ! this.htLocked.containsKey(_iParentScanNum) ) {
    		return;
    	} else {
    		alManuallyChangedRows = this.htLocked.get(_iParentScanNum);
    	}
    	alManuallyChangedRows.remove( _iRowId);
    }
    
    public boolean isInvisibleRow( Integer _iParentScanNum, String _iRowId ) {	
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return false;
    	}
    	ArrayList<String> alInvisibleRows = null;
    	if ( ! this.htInvisibleRows.containsKey(_iParentScanNum) ) {
    		return false;
    	} else {
    		alInvisibleRows = this.htInvisibleRows.get(_iParentScanNum);
    	}
 		return alInvisibleRows.contains(_iRowId);		
 	}

     public boolean isManuallyChangedPeak( Integer _iParentScanNum, String _iRowId ) {	
     	if( _iParentScanNum == null || _iRowId == null ) {
    		return false;
    	}
     	ArrayList<String> alManuallyChangedRows = null;
     	if ( ! this.htManuallyChanged.containsKey(_iParentScanNum) ) {
     		return false;
     	} else {
     		alManuallyChangedRows = this.htManuallyChanged.get(_iParentScanNum);
     	}
  		return alManuallyChangedRows.contains(_iRowId);		
  	}
     
    public boolean isLockedPeak( Integer _iParentScanNum, String _iRowId ) {	
      	if( _iParentScanNum == null || _iRowId == null ) {
     		return false;
     	}
      	ArrayList<String> alLockedPeaks = null;
      	if ( ! this.htLocked.containsKey(_iParentScanNum) ) {
      		return false;
      	} else {
      		alLockedPeaks = this.htLocked.get(_iParentScanNum);
      	}
   		return alLockedPeaks.contains(_iRowId);		
   	}
    
    public void setHiddenRow( Integer _iParentScanNum, String _iRowId, String _sFeatureId ) {
    	if( _iParentScanNum == null || _iRowId == null || _sFeatureId == null) {
    		return;
    	}
    	Hashtable<String, ArrayList<String>> htRowToHiddenFeatures = null;
    	if( ! this.htHiddenRows.containsKey(_iParentScanNum) ) {
    		htRowToHiddenFeatures = new Hashtable<String, ArrayList<String>>();
    		this.htHiddenRows.put(_iParentScanNum, htRowToHiddenFeatures);
    	} else {
    		htRowToHiddenFeatures = this.htHiddenRows.get(_iParentScanNum);
    	}
    	ArrayList<String> alHiddenAnnotations = null;
    	if ( htRowToHiddenFeatures.containsKey(_iRowId) )
    		alHiddenAnnotations = htRowToHiddenFeatures.get(_iRowId);
    	else {
    		alHiddenAnnotations = new ArrayList<String>();
    		htRowToHiddenFeatures.put(_iRowId, alHiddenAnnotations);
    	}
    	if ( ! alHiddenAnnotations.contains(_sFeatureId) ) {
    		alHiddenAnnotations.add(_sFeatureId);
    	}    			
    }

    public void removeHiddenRow( Integer _iParentScanNum, String _iRowId, String _sFeatureId ) {
    	if( _iParentScanNum == null || _iRowId == null || _sFeatureId == null) {
    		return;
    	}
    	Hashtable<String, ArrayList<String>> htRowToHiddenFeatures = null;
    	if( ! this.htHiddenRows.containsKey(_iParentScanNum) ) {
    		return;
    	} else {
    		htRowToHiddenFeatures = this.htHiddenRows.get(_iParentScanNum);
    	}
    	ArrayList<String> alHiddenAnnotations = null;
    	if ( htRowToHiddenFeatures.containsKey(_iRowId) )
    		alHiddenAnnotations = htRowToHiddenFeatures.get(_iRowId);
    	else {
    		return;
    	}
    	if ( alHiddenAnnotations.contains(_sFeatureId) ) {
    		alHiddenAnnotations.remove(_sFeatureId);
    	}    			
    }

    public void clearHiddenRows( Integer _iParentScanNum, String _iRowId ) {
    	if( _iParentScanNum == null || _iRowId == null ) {
    		return;
    	}
    	Hashtable<String, ArrayList<String>> htRowToHiddenFeatures = null;
    	if( ! this.htHiddenRows.containsKey(_iParentScanNum) ) {
    		return;
    	} else {
    		htRowToHiddenFeatures = this.htHiddenRows.get(_iParentScanNum);
    	}
    	ArrayList<String> alHiddenAnnotations = null;
    	if ( htRowToHiddenFeatures.containsKey(_iRowId) )
    		alHiddenAnnotations = htRowToHiddenFeatures.get(_iRowId);
    	else {
    		return;
    	}
    	alHiddenAnnotations.clear();
    }

    public boolean isHiddenRow( Integer _iParentScanNum, String _iRowId, String _sFeatureId ) {	
    	Hashtable<String, ArrayList<String>> htRowToHiddenFeatures = null;
    	if( ! this.htHiddenRows.containsKey(_iParentScanNum) ) {
    		return false;
    	} else {
    		htRowToHiddenFeatures = this.htHiddenRows.get(_iParentScanNum);
    	}
    	ArrayList<String> alHiddenAnnotations = null;
    	if ( htRowToHiddenFeatures.containsKey(_iRowId) )
    		alHiddenAnnotations = htRowToHiddenFeatures.get(_iRowId);
    	else {
    		return false;
    	}
		return alHiddenAnnotations.contains(_sFeatureId);		
	}
    
    public Hashtable<Integer, Hashtable<String, ArrayList<String>>> getHtHiddenRows() {
		return htHiddenRows;
	}
    
    public Hashtable<Integer, ArrayList<String>> getHtLocked() {
		return htLocked;
	}
 }
