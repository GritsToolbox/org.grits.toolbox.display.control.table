package org.grits.toolbox.display.control.table.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.nebula.widgets.nattable.Messages;
import org.eclipse.nebula.widgets.nattable.columnChooser.ColumnEntry;
import org.eclipse.nebula.widgets.nattable.util.ArrayUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.grits.toolbox.core.utilShare.ErrorUtils;
import org.grits.toolbox.display.control.table.tablecore.GRITSTable;

/**
 * @author D Brent Weatherly (dbrentw@uga.edu)
 *
 */
public class GRITSTableColumnChooserDialog extends ColumnChooserDialog {

	protected Composite extraItemComposite = null;
	protected Button replicateButton = null;
	protected Button persistenceButton = null;
	protected GRITSTableColumnChooser colChooser = null;
	protected List<List<Integer>> columnGroups = null;

	public GRITSTableColumnChooserDialog(Shell parentShell, String availableLabel, 
			String selectedLabel, GRITSTableColumnChooser colChooser) {
		super(parentShell, availableLabel, selectedLabel);
		this.colChooser = colChooser;
	}

	protected void createButtonsForButtonBar(Composite parent) { 
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("AbstractColumnChooserDialog.doneButton"), true); //$NON-NLS-1$
	}

	public GRITSTableColumnChooser getParentColChooser() {
		return colChooser;
	}

	@Override
	protected void constrainShellSize() {
		// TODO Auto-generated method stub
		super.constrainShellSize();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		return super.createDialogArea(parent);
	}

	@Override
	protected Point getInitialSize() {
		if (getDialogBoundsSettings() == null) {
			return new Point(600, 500);
		}
		Point initialSize = super.getInitialSize();
		return initialSize.x < 600 && initialSize.y < 500 ? new Point(600, 500): initialSize;
	}

	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		// TODO Auto-generated method stub
		return super.getDialogBoundsSettings();
	}

	@Override
	protected int getDialogBoundsStrategy() {
		// TODO Auto-generated method stub
		return super.getDialogBoundsStrategy();
	}

	@Override
	public void setDialogSettings(IDialogSettings dialogSettings) {
		// TODO Auto-generated method stub
		super.setDialogSettings(dialogSettings);
	}

	@Override
	public void populateDialogArea(Composite parent) {
		super.populateDialogArea(parent);

		GridData gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		extraItemComposite = new Composite(parent, SWT.NONE);
		extraItemComposite.setLayoutData(gridData);
		GridLayout gl = new GridLayout(1, false);
		extraItemComposite.setLayout(gl);

		gridData = new GridData();
		replicateButton = new Button(extraItemComposite, SWT.CHECK);
		replicateButton.setText("Replicate column options to all experiments");
		replicateButton.setSelection(true);
		replicateButton.setLayoutData(gridData);
		replicateButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				if( ! replicateButton.getSelection() ) {
					ErrorUtils.createSimpleMessageBox(getShell(), "Message", "NOTE: Changing the order of columns\nbetween " +
																			 "experiments is not persistent");
				}
			}
		});
		if ( ! getParentColChooser().hasColumnGroupHeader() ) {
			replicateButton.setEnabled(false);
		}

		gridData = new GridData();
		persistenceButton = new Button(extraItemComposite, SWT.CHECK);
		persistenceButton.setText("Save column options as default preferences");
		persistenceButton.setSelection(GRITSTable.updatePreferencesFromColumnChooser.getUpdate());
		persistenceButton.setLayoutData(gridData);
		persistenceButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				GRITSTable.updatePreferencesFromColumnChooser.setUpdate( persistenceButton.getSelection() );
			}
		});
	}

	public boolean isPropigateSelected() {
		return this.replicateButton.isEnabled() && this.replicateButton.getSelection();
	}

	private void addAllLeaves( TreeItem[] _tree, List<TreeItem> allLeaves ) {
		for( int i = 0; i < _tree.length; i++ ) {
			if ( _tree[i].getItemCount() == 0 ) {
				allLeaves.add( _tree[i] );
			} else {
				addAllLeaves( _tree[i].getItems(), allLeaves );
			}
		}		
	}

	@Override
	protected List<Integer> getGroupMembers(int iInx) {
		if( this.columnGroups != null ) {
			for( int i = 0; i < columnGroups.size(); i++ ) {
				List<Integer> groupEntries = columnGroups.get(i);
				for( int j = 0; j < groupEntries.size(); j++ ) {
					Integer iPos = groupEntries.get(j);
					if( iPos == iInx ) {
						return groupEntries;
					}
				}
			}
		}
		return null;
	}

	@Override
	protected boolean isPartOfAGroup(int iInx) {
		if( this.columnGroups != null ) {
			for( int i = 0; i < columnGroups.size(); i++ ) {
				List<Integer> groupEntries = columnGroups.get(i);
				for( int j = 0; j < groupEntries.size(); j++ ) {
					Integer iPos = groupEntries.get(j);
					if( iPos == iInx ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void toggleColumnGroupSelection(TreeItem treeItem) {
		if( isColumnGroupLeaf(treeItem) ) {
			ErrorUtils.createSimpleMessageBox(getShell(), "Message", "Changing order of experiments is not allowed");
			getSelectedTree().deselect(treeItem);
		}
	}
	
	
	protected void initializeSelectedGroups() {
		List<TreeItem> allLeaves = new ArrayList<TreeItem>();
		List<TreeItem> selTree = ArrayUtil.asList(super.getSelectedTree().getItems());
		this.columnGroups = new ArrayList<>();
		addAllLeaves(super.getSelectedTree().getItems(), allLeaves);
		int iLeafCnt = 0;
		for( int i = 0; i < selTree.size(); i++ ) {
			TreeItem selItem = selTree.get(i);
			if( selItem.getItemCount() > 0 ) {
				List<Integer> groupEntries = new ArrayList<Integer>();
				this.columnGroups.add(groupEntries);
				List<TreeItem> groupItems = ArrayUtil.asList(selItem.getItems());
				for( int j = 0; j < groupItems.size(); j++ ) {
					//					GRITSColumnEntry selEntry = (GRITSColumnEntry) groupItems.get(j).getData();
					//					groupItems.get(j).
					groupEntries.add(iLeafCnt++);
				}
			}
		}
	}

	public TreeItem[] propigateSelectedItems() {
		List<TreeItem> alNewList = new ArrayList<TreeItem>();
		List<TreeItem> selTree = ArrayUtil.asList(super.getSelectedTree().getSelection());
		List<TreeItem> allLeaves = new ArrayList<TreeItem>();
		addAllLeaves(super.getSelectedTree().getItems(), allLeaves);
		for( int i = 0; i < selTree.size(); i++ ) {
			TreeItem selItem = selTree.get(i);
			if ( selItem.getItemCount() == 0 ) { // does this mean a leaf?  Not going to propigate movement of whole groups
				for( int j = 0; j < allLeaves.size(); j++ ) {
					TreeItem otherItem = allLeaves.get(j);
					if ( otherItem.getItemCount() > 0 )
						continue;
					GRITSColumnEntry selEntry = (GRITSColumnEntry) selItem.getData();
					GRITSColumnEntry otherEntry = (GRITSColumnEntry) otherItem.getData();
					if( selEntry.getKey().equals(otherEntry.getKey()) ) {
						alNewList.add(otherItem);
					}
				}
			}
		}
		if ( alNewList.isEmpty() ) 
			return null;

		TreeItem[] newTree = new TreeItem[alNewList.size()];
		for( int i = 0; i < alNewList.size(); i++ ) {
			newTree[i] = alNewList.get(i);
		}
		return newTree;
	}

	@Override
	protected void moveSelectedUp() {
		initializeSelectedGroups();
		if( isPropigateSelected() ) {
			TreeItem[] curList = super.getSelectedTree().getSelection();
			TreeItem[] selList = propigateSelectedItems();
			super.getSelectedTree().setSelection(selList);
			super.moveSelectedUp();
		} else {
			super.moveSelectedUp();
		}
	}

	@Override
	protected void moveSelectedDown() {
		initializeSelectedGroups();
		if( isPropigateSelected() ) {
			TreeItem[] curList = super.getSelectedTree().getSelection();
			TreeItem[] selList = propigateSelectedItems();
			super.getSelectedTree().setSelection(selList);
			super.moveSelectedDown();
		} else {
			super.moveSelectedDown();
		}
	}

	@Override
	void moveSelectedToTop() {
		initializeSelectedGroups();
		if( isPropigateSelected() ) {
			TreeItem[] curList = super.getSelectedTree().getSelection();
			TreeItem[] selList = propigateSelectedItems();
			super.getSelectedTree().setSelection(selList);
			super.moveSelectedToTop();
		} else {
			super.moveSelectedToTop();
		}
	}

	@Override
	void moveSelectedToBottom() {
		initializeSelectedGroups();
		if( isPropigateSelected() ) {
			TreeItem[] curList = super.getSelectedTree().getSelection();
			TreeItem[] selList = propigateSelectedItems();
			super.getSelectedTree().setSelection(selList);
			super.moveSelectedToBottom();
		} else {
			super.moveSelectedToBottom();
		}
	}

}
