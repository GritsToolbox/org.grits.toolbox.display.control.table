package org.grits.toolbox.display.control.table.preference;

import org.apache.log4j.Logger;
import org.grits.toolbox.core.datamodel.UnsupportedVersionException;
import org.grits.toolbox.core.preference.share.PreferenceEntity;

public class TableViewerPreferenceLoader {
	private static final Logger logger = Logger.getLogger(TableViewerPreferenceLoader.class);

	public static TableViewerPreference getTableViewerPreference()  {
		TableViewerPreference preferences = null;
		try {
			PreferenceEntity preferenceEntity = TableViewerPreference.getPreferenceEntity(); 
			if( preferenceEntity == null ) { // previous version
				preferences = TableViewerPreferencePreVersion.getTableViewerPreferencesPreVersioning();
				
				if( preferences != null ) {
					TableViewerPreferencePreVersion.removeElements();
					preferences.writePreference();
				}
			} else {
				preferences = TableViewerPreference.getTableViewerPreference(preferenceEntity, TableViewerPreference.class);
			}
		} catch (UnsupportedVersionException ex) {
			logger.error(ex.getMessage(), ex);
			
		} catch( Exception ex ) {
			logger.error(ex.getMessage(), ex);
		}		
		if( preferences == null ) { // well, either no preferences yet or some error. initialize to defaults and return
			preferences = new TableViewerPreference();
		}
		return preferences;
	}

}
