package org.grits.toolbox.display.control.table.preference;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.grits.toolbox.core.preference.share.PreferenceWriter;
import org.jdom.JDOMException;

public class TableViewerPreferencePreVersion extends TableViewerPreference {
	private static final Logger logger = Logger.getLogger(TableViewerPreferencePreVersion.class);
	private static final String PREVIOUS_PREFERENCE_ID = "org.grits.toolbox.display.control.table.preference.TableViewerPreference";
	
	public TableViewerPreferencePreVersion()
	{
		super();
	}
	
	public static String getPreferenceID() {
		return PREVIOUS_PREFERENCE_ID;
	}
	
	public static boolean removeElements() {
		try {
			PreferenceWriter.deletePreference(TableViewerPreference.getPreferenceID());
			return true;
		} catch( Exception ex ) {
			logger.error(ex.getMessage(), ex);
		}
		return false;
	}
	
	public static TableViewerPreference getTableViewerPreferencesPreVersioning() {
		TableViewerPreferenceReader reader = new TableViewerPreferenceReader();
		TableViewerPreference preference = new TableViewerPreference();
		try {
			String colSettings = reader.getTableViewerColumnSettings(TableViewerPreference.getPreferenceID());
			preference.setColumnSettings(colSettings);
			TableViewerPreference.initTableViewerColumnSettings(preference);			
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (JDOMException e) {
			logger.error(e.getMessage(),e);
		}
		return preference;
	}	
}