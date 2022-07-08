package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;

public class GRITSHeaderMenuConfiguration extends HeaderMenuConfiguration {	
	public GRITSHeaderMenuConfiguration(NatTable natTable) {
		super(natTable);
	}
	
	@Override
	protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
		PopupMenuBuilder pmb = new PopupMenuBuilder(natTable);
		pmb.withMenuItemProvider( GRITSMenuItemProviders.hideColumnMenuItemProvider() );
		pmb.withMenuItemProvider( GRITSMenuItemProviders.showAllColumnsMenuItemProvider() );
		pmb.withMenuItemProvider( GRITSMenuItemProviders.columnChooserMenuItemProvider() );
		pmb.withAutoResizeSelectedColumnsMenuItem();
		return pmb;
	}
	

	@Override
	protected PopupMenuBuilder createRowHeaderMenu(NatTable natTable) {
		PopupMenuBuilder pmb = new PopupMenuBuilder(natTable);
		pmb.withAutoResizeSelectedRowsMenuItem();
		return pmb;								
	}
}
