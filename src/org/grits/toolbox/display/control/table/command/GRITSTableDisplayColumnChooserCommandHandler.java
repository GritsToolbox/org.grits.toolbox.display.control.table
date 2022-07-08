/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.grits.toolbox.display.control.table.command;


import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.nattable.command.AbstractLayerCommandHandler;
import org.grits.toolbox.display.control.table.dialog.GRITSTableColumnChooser;
import org.grits.toolbox.display.control.table.tablecore.IGritsTable;

/**
 * @author D Brent Weatherly (dbrentw@uga.edu)
 *
 */
public class GRITSTableDisplayColumnChooserCommandHandler 
	extends AbstractLayerCommandHandler<GRITSTableDisplayColumnChooserCommand> {

	protected final boolean sortAvailableColumns;
	protected IGritsTable gritsTable;
	
	public GRITSTableDisplayColumnChooserCommandHandler(
			IGritsTable natTable ) {

		this(false, natTable);
	}

	public GRITSTableDisplayColumnChooserCommandHandler(
			boolean sortAvalableColumns,
			IGritsTable parent ) {
		
		this.sortAvailableColumns = sortAvalableColumns;
		this.gritsTable = parent;
	}
	
	
	@Override
	public boolean doCommand(GRITSTableDisplayColumnChooserCommand command) {
		GRITSTableColumnChooser columnChooser = getNewGRITSTableColumnChooser(command);
		columnChooser.createDialog();
		columnChooser.populateDialog();
		columnChooser.addListenersOnColumnChooserDialog();
		columnChooser.openDialog();
		if( columnChooser.getColumnChooserDialog().getReturnCode() == Window.OK ) {			
			return true;
		}
		return false;
	}
	
	public GRITSTableColumnChooser getNewGRITSTableColumnChooser(GRITSTableDisplayColumnChooserCommand command) {
		GRITSTableColumnChooser columnChooser = new GRITSTableColumnChooser(
				command.getNatTable().getShell(),
				sortAvailableColumns, false, gritsTable  );
		return columnChooser;
		
	}
	
	public Class<GRITSTableDisplayColumnChooserCommand> getCommandClass() {
		return GRITSTableDisplayColumnChooserCommand.class;
	}

}
