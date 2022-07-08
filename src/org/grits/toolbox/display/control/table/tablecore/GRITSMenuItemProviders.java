package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.Messages;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.command.RowHideCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ShowAllColumnsCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ShowAllRowsCommand;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.grits.toolbox.display.control.table.command.GRITSTableDisplayColumnChooserCommand;

public class GRITSMenuItemProviders extends MenuItemProviders {	
	
	public static IMenuItemProvider hideColumnMenuItemProvider() {
		return hideColumnMenuItemProvider("Hide column"); //$NON-NLS-1$
	}

	public static IMenuItemProvider hideColumnMenuItemProvider(final String menuLabel) {
		return new IMenuItemProvider() {

			public void addMenuItem(final NatTable natTable, final Menu popupMenu) {
				MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
				menuItem.setText(menuLabel);
				menuItem.setEnabled(true);

				menuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						int colPosition = getNatEventData(event).getColumnPosition();
						natTable.doCommand( new ColumnHideCommand(natTable, colPosition) );
						if (natTable instanceof GRITSTable)
							( (GRITSTable) natTable).performAutoResize();
					}
				});
			}
		};
	}

	public static IMenuItemProvider showAllColumnsMenuItemProvider() {
		return showAllColumnsMenuItemProvider("Show all columns"); //$NON-NLS-1$
	}

	public static IMenuItemProvider showAllColumnsMenuItemProvider(final String menuLabel) {
		return new IMenuItemProvider() {

			public void addMenuItem(final NatTable natTable, Menu popupMenu) {
				MenuItem showAllColumns = new MenuItem(popupMenu, SWT.PUSH);
				showAllColumns.setText(menuLabel);
//				showAllColumns.setImage(GUIHelper.getImage("show_column")); //$NON-NLS-1$
				showAllColumns.setEnabled(true);

				showAllColumns.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						natTable.doCommand(new ShowAllColumnsCommand());
						if (natTable instanceof GRITSTable)
							( (GRITSTable) natTable).performAutoResize();
					}
				});
			}
		};
	}

	public static IMenuItemProvider hideRowMenuItemProvider() {
		return hideRowMenuItemProvider("Hide row"); //$NON-NLS-1$
	}

	public static IMenuItemProvider hideRowMenuItemProvider(final String menuLabel) {
		return new IMenuItemProvider() {

			public void addMenuItem(final NatTable natTable, final Menu popupMenu) {
				MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
				menuItem.setText(menuLabel);
				menuItem.setEnabled(true);

				menuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						int rowPosition = getNatEventData(event).getRowPosition();
						natTable.doCommand( new RowHideCommand(natTable, rowPosition) );
					}
				});
			}
		};
	}

	public static IMenuItemProvider showAllRowMenuItemProvider() {
		return showAllRowMenuItemProvider("Show all rows"); //$NON-NLS-1$
	}

	public static IMenuItemProvider showAllRowMenuItemProvider(final String menuLabel) {
		return new IMenuItemProvider() {

			public void addMenuItem(final NatTable natTable, Menu popupMenu) {
				MenuItem showAllColumns = new MenuItem(popupMenu, SWT.PUSH);
				showAllColumns.setText(menuLabel);
//				showAllColumns.setImage(GUIHelper.getImage("show_column")); //$NON-NLS-1$
				showAllColumns.setEnabled(true);

				showAllColumns.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						natTable.doCommand(new ShowAllRowsCommand());
					}
				});
			}
		};
	}
	
	public static IMenuItemProvider columnChooserMenuItemProvider() {
		return columnChooserMenuItemProvider(Messages.getString("MenuItemProviders.chooseColumns")); //$NON-NLS-1$
	}

	public static IMenuItemProvider columnChooserMenuItemProvider(final String menuLabel) {
		return new IMenuItemProvider() {

			public void addMenuItem(final NatTable natTable, final Menu popupMenu) {
				MenuItem columnChooser = new MenuItem(popupMenu, SWT.PUSH);
				columnChooser.setText(menuLabel);
				columnChooser.setImage(GUIHelper.getImage("column_chooser")); //$NON-NLS-1$
				columnChooser.setEnabled(true);

				columnChooser.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						natTable.doCommand(new GRITSTableDisplayColumnChooserCommand(natTable));
					}
				});
			}
		};
	}

	
}	