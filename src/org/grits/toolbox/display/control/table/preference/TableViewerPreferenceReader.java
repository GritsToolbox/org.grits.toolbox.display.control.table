package org.grits.toolbox.display.control.table.preference;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.grits.toolbox.core.preference.share.PreferenceReader;
import org.jdom.Element;
import org.jdom.JDOMException;

public class TableViewerPreferenceReader extends PreferenceReader {
	protected Element element = null;
	//log4J Logger
	private static final Logger logger = Logger.getLogger(TableViewerPreferenceReader.class);

	public String getTableViewerColumnSettings(String _prefId) throws IOException, JDOMException {
		element = getPreferenceElement(_prefId);
		if(element == null)
		{
			return null;
		}
		return element.getAttributeValue(TableViewerColumnSettings.COL_VIS_ATTR);
//		return getTableViewerColumnSettings();
	}

//	protected TableViewerColumnSettings getNewTableSettings() {
//		TableViewerColumnSettings spf = new TableViewerColumnSettings();
//		return spf;
//	}

//	private Map<Integer, List<String[]>> getPreferencesFromFile() {
//		Map<Integer, List<String[]>> mColNumToPrefs = new HashMap<>();
//		if(element == null) {
//			return null;
//		}
//		String sColAttr = element.getAttributeValue(TableViewerColumnSettings.COL_VIS_ATTR);
//		if(sColAttr.length() != 0) {		
//			String[] sColHeaders = sColAttr.split( TableViewerColumnSettings.COL_DELIMITER );
//			for( int i = 0; i < sColHeaders.length; i++ ) {
//				String[] sColAndVis = sColHeaders[i].split( TableViewerColumnSettings.TOK_DELIMITER );
//				Integer iVisColInx = -1;
//				try {
//					iVisColInx = Integer.parseInt( sColAndVis[1] );
//				} catch( NumberFormatException ex ) {					
//					iVisColInx = -1;
//				} 
//				List<String[]> lPrefInfo = null;
//				if( mColNumToPrefs.containsKey(iVisColInx) ) {
//					lPrefInfo = mColNumToPrefs.get(iVisColInx);
//				} else {
//					lPrefInfo = new ArrayList<>();
//					mColNumToPrefs.put(iVisColInx, lPrefInfo);
//				}
//				lPrefInfo.add(sColAndVis);				
//			}
//		}
//		return mColNumToPrefs;
//	}
//
//	private List<Integer> getSortedCols( Set<Integer> _sCols ) {
//		List<Integer> lSortedCols = new ArrayList<>();
//		Iterator<Integer> i = _sCols.iterator();
//		while( i.hasNext() ) {
//			Integer iColNum = i.next();
//			lSortedCols.add(iColNum);
//		}
//		Collections.sort(lSortedCols);
//		return lSortedCols;
//	}
//
//	protected TableViewerColumnSettings getTableViewerEntity() {
//		try {
//			Map<Integer, List<String[]>> mColNumToPrefs = getPreferencesFromFile();
//			if(mColNumToPrefs == null || mColNumToPrefs.isEmpty()) {
//				return null;
//			}
//			List<Integer> lSortedCols = getSortedCols( mColNumToPrefs.keySet() );
//			Iterator<Integer> itr = lSortedCols.iterator();
//			int iVisColInx = 0;
//			TableViewerColumnSettings spf = getNewTableSettings();
//			while( itr.hasNext() ) {
//				Integer iColNum = itr.next();
//				List<String[]> lCols = mColNumToPrefs.get(iColNum);
//
//				if( iColNum >= 0 && lCols.size() > 1 ) { // error in preferences, needs to be reset
//					String errorMsg = "Invalid column preferences detected for column number " + iColNum;
//					logger.error(errorMsg);
//				}
//				Iterator<String[]> itr2 = lCols.iterator();
//				while( itr2.hasNext() ) {
//					String[] sColAndVis = itr2.next();
//					GRITSColumnHeader header = getColumnHeader(sColAndVis[0]);
//					int iColInx = iColNum > 0 ? iVisColInx : iColNum;
//					if ( header != null )
//						spf.setVisColInx(header, iColInx);
//					else {
//						logger.warn("Unrecognized column in preferences: " + sColAndVis[0]);
//						spf.getUnrecognizedHeaders().put(sColAndVis[0], new Integer(iColInx));
//					}
//					if( iColInx >= 0 ) {
//						logger.debug("Preference:  header: " + sColAndVis[0] + ", pref col: " + iVisColInx);
//						iVisColInx++;
//					}
//				}
//			}
//			if ( spf.getNumColumns() > 0 )
//				return spf;
//		} catch( Exception ex ) {
//			String errorMsg = "An error occurred loading preferences";
//			logger.error(errorMsg, ex);
//		}
//		return null;
//	}

	public Element getElement()
	{
		return this.element;
	}

//	public GRITSColumnHeader getColumnHeader(String _sKey) {
//		return null;
//	}

}
