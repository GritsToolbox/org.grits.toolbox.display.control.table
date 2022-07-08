package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.sort.config.DefaultSortConfiguration;
import org.eclipse.nebula.widgets.nattable.sort.event.ColumnHeaderClickEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;

public class GRITSSingleClickConfiguration extends DefaultSortConfiguration {
	private boolean bHasGroupHeader = false;
	public GRITSSingleClickConfiguration(boolean bHasGroupHeader) {
		super();
		this.bHasGroupHeader = bHasGroupHeader;
	}
	
	public GRITSSingleClickConfiguration(ICellPainter cellPainter, boolean bHasGroupHeader) {
		super(cellPainter);
		this.bHasGroupHeader = bHasGroupHeader;
	}
	
	/**
	 * Remove the original key bindings and implement new ones.
	 */
	@Override
	public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
		// Register new bindings
		uiBindingRegistry.registerFirstSingleClickBinding(
              new ColumnHeaderClickEventMatcher(SWT.NONE, 1), new GRITSSortColumnAction(false, this.bHasGroupHeader));

		uiBindingRegistry.registerSingleClickBinding(
	             MouseEventMatcher.columnHeaderLeftClick(SWT.ALT), new GRITSSortColumnAction(true, this.bHasGroupHeader));
	}
}
