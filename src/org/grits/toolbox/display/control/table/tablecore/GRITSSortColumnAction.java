package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.resize.command.InitializeAutoResizeColumnsCommand;
import org.eclipse.nebula.widgets.nattable.resize.command.InitializeAutoResizeRowsCommand;
import org.eclipse.nebula.widgets.nattable.sort.command.SortColumnCommand;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.util.GCFactory;
import org.eclipse.swt.events.MouseEvent;

public class GRITSSortColumnAction implements IMouseAction {
	private final boolean accumulate;
	private NatTable natTable = null;
	private boolean bHasGroupHeader = false;
	
	public GRITSSortColumnAction(boolean accumulate, boolean bHasGroupHeader ) {
		this.accumulate = accumulate;
		this.bHasGroupHeader = bHasGroupHeader;
	}
	
	public void run(NatTable natTable, MouseEvent event) {
		this.natTable = natTable;
		int rowPosition = ((NatEventData)event.data).getRowPosition();
		if( bHasGroupHeader && rowPosition == 0 ) // added to ignore sorting on the group headre if there
			return;
		int columnPosition = ((NatEventData)event.data).getColumnPosition();
		natTable.doCommand(new SortColumnCommand(natTable, columnPosition, accumulate));
		//need to resize picture column and all rows
		resizeRowColumns();
	}
	
	private void resizeRowColumns() {
		//autosize row
		//how many rows size of list elements
		//0 is the column header
		for(int i=0; i<=natTable.getRowCount(); i++)
		{
			natTable.doCommand(new InitializeAutoResizeRowsCommand(natTable, i, natTable.getConfigRegistry(), new GCFactory(natTable)));
		}
		//autosize column
		//0 is the row header
		natTable.doCommand(new InitializeAutoResizeColumnsCommand(natTable, 1, natTable.getConfigRegistry(), new GCFactory(natTable)));
	}
}
