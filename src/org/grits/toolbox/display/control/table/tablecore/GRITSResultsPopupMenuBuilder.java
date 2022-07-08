package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;

import org.grits.toolbox.display.control.table.process.TableDataProcessor;

public class GRITSResultsPopupMenuBuilder extends PopupMenuBuilder {
	protected TableDataProcessor GRITSToTableDataExtractor;

	public GRITSResultsPopupMenuBuilder(NatTable parent, TableDataProcessor GRITSToTableDataExtractor) {
		super(parent);
		this.GRITSToTableDataExtractor = GRITSToTableDataExtractor;
	}

}
