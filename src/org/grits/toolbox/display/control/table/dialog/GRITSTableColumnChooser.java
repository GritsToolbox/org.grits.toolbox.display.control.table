package org.grits.toolbox.display.control.table.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.nebula.widgets.nattable.Messages;
import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnChooserUtils;
import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnEntry;
import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnGroupEntry;
import org.eclipse.nebula.widgets.nattable.columnChooser.ISelectionTreeListener;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.group.command.ColumnGroupExpandCollapseCommand;
import org.eclipse.nebula.widgets.nattable.group.command.ReorderColumnGroupCommand;
import org.eclipse.nebula.widgets.nattable.group.command.ReorderColumnsAndGroupsCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.reorder.command.ColumnReorderCommand;
import org.eclipse.nebula.widgets.nattable.reorder.command.MultiColumnReorderCommand;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer.MoveDirectionEnum;
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.display.control.table.tablecore.GRITSColumnChooserUtils;
import org.grits.toolbox.display.control.table.tablecore.IGritsTable;

/**
 * @author D Brent Weatherly (dbrentw@uga.edu)
 *
 */
public class GRITSTableColumnChooser {

	private static final Comparator<ColumnEntry> COLUMN_ENTRY_LABEL_COMPARATOR = new Comparator<ColumnEntry>() {
		public int compare(ColumnEntry o1, ColumnEntry o2) {
			return ((GRITSColumnEntry) o1).getLabel().compareToIgnoreCase(((GRITSColumnEntry) o2).getLabel());
		}
	};
	
	protected ColumnChooserDialog columnChooserDialog;
	protected List<ColumnEntry> hiddenColumnEntries;
	protected List<ColumnEntry> visibleColumnsEntries;
	protected boolean sortAvailableColumns;
	protected Boolean asGlobalPreference = null;
	protected IGritsTable gritsTable = null;

	public GRITSTableColumnChooser(
			Shell shell, 
			boolean sortAvailableColumns, boolean asGlobalPreference, IGritsTable gritsTable) {
		
		this.gritsTable = gritsTable;		
		this.sortAvailableColumns = sortAvailableColumns;
		this.asGlobalPreference = asGlobalPreference;
		columnChooserDialog = getNewColumnChooserDialog(shell);
	}
		
	protected ColumnChooserDialog getNewColumnChooserDialog(Shell shell) {
		if ( this.asGlobalPreference ) 	// called from Preferences page. No need to add extra checkboxes and stuff
			columnChooserDialog = new ColumnChooserDialog(shell, 
					Messages.getString("ColumnChooser.availableColumns"), 
					Messages.getString("ColumnChooser.selectedColumns")); //$NON-NLS-1$ //$NON-NLS-2$
		else
			columnChooserDialog = new GRITSTableColumnChooserDialog(shell, 
					Messages.getString("ColumnChooser.availableColumns"), 
					Messages.getString("ColumnChooser.selectedColumns"), 
					this); //$NON-NLS-1$ //$NON-NLS-2$	
		
		return columnChooserDialog;
	}
	
	public IGritsTable getGRITSTable() {
		return gritsTable;
	}
	
	protected boolean hasColumnGroupHeader() {
		return gritsTable.hasColumnGroupHeader();
	}
	
	protected ColumnHideShowLayer getColumnHideShowLayer() {
		return gritsTable.getColumnHideShowLayer();
	}
	
	protected DataLayer getColumnHeaderDataLayer() {
		return gritsTable.getColumnHeaderDataLayer();
	}
	
	protected ColumnHeaderLayer getColumnHeaderLayer() {
		return gritsTable.getColumnHeaderLayer();
	}
	
	protected ColumnGroupModel getColumnGroupModel() {
		return gritsTable.getColumnGroupModel();
	}
	
	protected SelectionLayer getSelectionLayer() {
		return gritsTable.getSelectionLayer();
	}
	
	public void reInit( IGritsTable gritsTable ) {
		this.gritsTable = gritsTable;
	}
	
	public void setDialogSettings(IDialogSettings dialogSettings) {
		columnChooserDialog.setDialogSettings(dialogSettings);
	}
	
	public ColumnChooserDialog getColumnChooserDialog() {
		return columnChooserDialog;
	}

	public void createDialog() {
		columnChooserDialog.create();
	}
	
	public List<ColumnEntry> getVisibleColumnsEntries() {
		return visibleColumnsEntries;
	}

	public List<ColumnEntry> getVisibleColumnEntries() {
		List<ColumnEntry> columnEntries = GRITSColumnChooserUtils.getVisibleColumnsEntries(getColumnHideShowLayer(), 
				getColumnHeaderLayer(), getColumnHeaderDataLayer());	
		return columnEntries;
	}
	
	public List<ColumnEntry> getHiddenColumnEntries() {
		List<ColumnEntry> columnEntries = GRITSColumnChooserUtils.getHiddenColumnEntries(getColumnHideShowLayer(), 
				getColumnHeaderLayer(), getColumnHeaderDataLayer());
		if (sortAvailableColumns) {
			Collections.sort(columnEntries, COLUMN_ENTRY_LABEL_COMPARATOR);
		}
		return columnEntries;
	}	
	
	public void populateDialog() {
		if ( this.gritsTable == null || this.gritsTable.getColumnHeaderDataLayer().getColumnCount() == 0 )
			return;
		hiddenColumnEntries = getHiddenColumnEntries();
		columnChooserDialog.populateAvailableTree(hiddenColumnEntries, getColumnGroupModel());

		visibleColumnsEntries = getVisibleColumnEntries();
		
		columnChooserDialog.populateSelectedTree(visibleColumnsEntries, getColumnGroupModel());

		columnChooserDialog.expandAllLeaves();

	}

	public void openDialog() {
		columnChooserDialog.open();
	}

	
	public void addListenersOnColumnChooserDialog() {	
		columnChooserDialog.addListener(new ISelectionTreeListener() {

			public void itemsRemoved(List<ColumnEntry> removedItems) {
				List<ColumnEntry> toBeRemoved = removedItems;
				if ( ! asGlobalPreference && ( (GRITSTableColumnChooserDialog) columnChooserDialog).isPropigateSelected() ) 
					toBeRemoved = propigateSelectedItems( removedItems, visibleColumnsEntries );
				ColumnChooserUtils.hideColumnEntries(toBeRemoved, getColumnHideShowLayer());
				
				refreshColumnChooserDialog();
			}

			public void itemsSelected(List<ColumnEntry> addedItems) {
				List<ColumnEntry> toBeAdded = addedItems; 
				if ( ! asGlobalPreference && ( (GRITSTableColumnChooserDialog) columnChooserDialog).isPropigateSelected() ) 
					toBeAdded = propigateSelectedItems( addedItems, hiddenColumnEntries );
				ColumnChooserUtils.showColumnEntries(toBeAdded, getColumnHideShowLayer());
				refreshColumnChooserDialog();
				columnChooserDialog.setSelectionIncludingNested(ColumnChooserUtils.getColumnEntryIndexes(addedItems));
			}

			public void itemsMoved(MoveDirectionEnum direction, List<ColumnGroupEntry> movedColumnGroupEntries, List<ColumnEntry> movedColumnEntries, List<List<Integer>> fromPositions, List<Integer> toPositions) {
				moveItems(direction, movedColumnGroupEntries, movedColumnEntries, fromPositions, toPositions);
			}

			/**
			 * Fire appropriate commands depending on the events received from the column chooser dialog
			 * @param direction
			 * @param movedColumnGroupEntries
			 * @param movedColumnEntries
			 * @param fromPositions
			 * @param toPositions
			 */
			private void moveItems(MoveDirectionEnum direction, List<ColumnGroupEntry> movedColumnGroupEntries, List<ColumnEntry> movedColumnEntries, List<List<Integer>> fromPositions, List<Integer> toPositions) {

				for (int i = 0; i < fromPositions.size(); i++) {
					boolean columnGroupMoved = columnGroupMoved(fromPositions.get(i), movedColumnGroupEntries);
					boolean multipleColumnsMoved = fromPositions.get(i).size() > 1;

					ILayerCommand command = null;
					if (!columnGroupMoved && !multipleColumnsMoved) {
						int fromPosition = fromPositions.get(i).get(0).intValue();
						int toPosition = adjustToPosition(direction, toPositions.get(i).intValue());
						command = new ColumnReorderCommand(getColumnHideShowLayer(), fromPosition, toPosition);
					} else if (columnGroupMoved && multipleColumnsMoved) {
						command = new ReorderColumnsAndGroupsCommand(getColumnHideShowLayer(), fromPositions.get(i), adjustToPosition(direction, toPositions.get(i)));
					} else if (!columnGroupMoved && multipleColumnsMoved) {
						command = new MultiColumnReorderCommand(getColumnHideShowLayer(), fromPositions.get(i), adjustToPosition(direction, toPositions.get(i)));
					} else if (columnGroupMoved && !multipleColumnsMoved) {
						command = new ReorderColumnGroupCommand(getColumnHideShowLayer(), fromPositions.get(i).get(0), adjustToPosition(direction, toPositions.get(i)));
					}
					getColumnHideShowLayer().doCommand(command);
				}

				refreshColumnChooserDialog();
				columnChooserDialog.setSelectionIncludingNested(ColumnChooserUtils.getColumnEntryIndexes(movedColumnEntries));
			}

			private int adjustToPosition(MoveDirectionEnum direction, Integer toColumnPosition) {
				if (MoveDirectionEnum.DOWN == direction) {
					return toColumnPosition + 1;
				} else {
					return toColumnPosition;
				}
			}

			private boolean columnGroupMoved(List<Integer> fromPositions, List<ColumnGroupEntry> movedColumnGroupEntries) {
				for (ColumnGroupEntry columnGroupEntry : movedColumnGroupEntries) {
					if(fromPositions.contains(columnGroupEntry.getFirstElementPosition())) return true;
				}
				return false;
			}

			public void itemsCollapsed(ColumnGroupEntry columnGroupEntry) {
				int index = columnGroupEntry.getFirstElementIndex().intValue();
				int position = getSelectionLayer().getColumnPositionByIndex(index);
				getSelectionLayer().doCommand(new ColumnGroupExpandCollapseCommand(getSelectionLayer(), position));
			}

			public void itemsExpanded(ColumnGroupEntry columnGroupEntry) {
				int index = columnGroupEntry.getFirstElementIndex().intValue();
				int position = getSelectionLayer().getColumnPositionByIndex(index);
				getSelectionLayer().doCommand(new ColumnGroupExpandCollapseCommand(getSelectionLayer(), position));
			}
		});
	}

	public List<ColumnEntry> propigateSelectedItems(List<ColumnEntry> selectedItems, List<ColumnEntry> selectedList ) {
		
		List<ColumnEntry> newList = new ArrayList<ColumnEntry>();
		for( int i = 0; i < selectedList.size(); i++ ) {
			ColumnEntry selEntry = selectedList.get(i);
			for (int j = 0; j < selectedItems.size(); j++ ) {
				ColumnEntry otherEntry = selectedItems.get(j);
				// perform the comparison on the key and not the label!
				if ( ((GRITSColumnEntry) selEntry).getKey().equals(((GRITSColumnEntry) otherEntry).getKey()) ) {
					newList.add(selEntry);
				}
//				if ( selEntry.getLabel().equals(otherEntry.getLabel()) ) {
//					newList.add(selEntry);
//				}
			}			
		}
		return newList;
	}

	protected void refreshColumnChooserDialog() {
		hiddenColumnEntries = getHiddenColumnEntries();
//		visibleColumnsEntries = GRITSColumnChooserUtils.getVisibleColumnsEntries(getColumnHideShowLayer(), 
//				getColumnHeaderLayer(), getColumnHeaderDataLayer());
		visibleColumnsEntries = getVisibleColumnEntries();
		
		columnChooserDialog.removeAllLeaves();

		columnChooserDialog.populateSelectedTree(visibleColumnsEntries, getColumnGroupModel());
		columnChooserDialog.populateAvailableTree(hiddenColumnEntries, getColumnGroupModel());
		columnChooserDialog.expandAllLeaves();
	}

}
