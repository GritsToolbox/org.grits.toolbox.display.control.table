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

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.AbstractContextFreeCommand;

public class GRITSTableDisplayColumnChooserCommand extends AbstractContextFreeCommand {

	private final NatTable natTable;

	public GRITSTableDisplayColumnChooserCommand(NatTable natTable) {
		this.natTable = natTable;
	}
	
	public NatTable getNatTable() {
		return natTable;
	}
	
}
