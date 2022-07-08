package org.grits.toolbox.display.control.table.tablecore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.workbench.renderers.swt.SashLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;

public class DelayedResizeListener implements ControlListener {

	private List<GRITSTable> tables = null;
	private Composite sashSource = null;

	public DelayedResizeListener() {
		tables = new ArrayList<>();
	}

	public void addTable(GRITSTable table) {
		tables.add(table);
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void controlResized(ControlEvent e) {
		if (sashSource != null) {
			return;
		}
		Composite source = (Composite) e.getSource();
		while (sashSource == null && source != null) {
			if (source.getLayout() instanceof SashLayout) {
				sashSource = source;
			}
			source = source.getParent();
		}
		if (sashSource != null) {
			sashSource.addMouseListener(new MouseListener() {

				@Override
				public void mouseUp(MouseEvent e) {
					for (GRITSTable table : tables) {
						table.performAutoResize();
					}
					sashSource.removeMouseListener(this);
					sashSource = null;
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}
