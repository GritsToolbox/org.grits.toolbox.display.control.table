package org.grits.toolbox.display.control.table.datamodel;

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

import java.util.Comparator;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.LayerCell;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;

import ca.odell.glazedlists.gui.AdvancedTableFormat;

public class GRITSColumnTableFormat<R> implements AdvancedTableFormat<R> {

	private IConfigRegistry configRegistry;
	private final ILayer columnHeaderDataLayer;
		
	public GRITSColumnTableFormat(IConfigRegistry configRegistry, ILayer columnHeaderDataLayer) {
		this.configRegistry = configRegistry;
		this.columnHeaderDataLayer = columnHeaderDataLayer;
	}

	public Class<?> getColumnClass(int col) {
		return null;
	}

	public Comparator<?> getColumnComparator(final int col) {
		LayerCell cell = (LayerCell) columnHeaderDataLayer.getCellByPosition(col, 0);
		if (cell == null) {
		    return null;
		}
		Comparator<?> comparator = configRegistry.getConfigAttribute(
										SortConfigAttributes.SORT_COMPARATOR,
										cell.getDisplayMode(),
										cell.getConfigLabels().getLabels());
		
		return (comparator instanceof NullComparator) ? null : comparator;
	}

	public String getColumnName(int col) {
		LayerCell cell = (LayerCell) columnHeaderDataLayer.getCellByPosition(col, 0);
		if (cell == null) {
		    return null;
		}
		return cell.getDataValue().toString();
	}

	public int getColumnCount() {
		return columnHeaderDataLayer.getColumnCount();
	}

	public Object getColumnValue(R rowObj, int col) {
		GRITSListDataRow list = (GRITSListDataRow) rowObj;
		
		return list.getDataRow().get(col);
	}
	
}