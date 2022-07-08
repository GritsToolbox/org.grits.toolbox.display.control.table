package org.grits.toolbox.display.control.table.datamodel;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.formula.functions.T;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.AbstractTableComparatorChooser;
import org.grits.toolbox.display.control.table.tablecore.GRITSTable;

public class GRITSSortModel implements ISortModel {

	public static final String PERSISTENCE_KEY_GLAZEDLISTS_SORT_MODEL = ".glazedListsSortModel"; //$NON-NLS-1$

	private GRITSComparatorChooser<T> comparatorChooser;
	private GRITSColumnTableFormat<T> tableFormat;
	protected final SortedList<T> sortedList;
	protected final IConfigRegistry configRegistry;
	protected final ILayer columnHeaderDataLayer;
	protected final GRITSTable parent;
	
	
	public GRITSSortModel(SortedList<T> sortedList, IConfigRegistry configRegistry, GRITSTable parent) {
		this.sortedList = sortedList;
		this.configRegistry = configRegistry;
		this.parent = parent;
		this.columnHeaderDataLayer = parent.getColumnHeaderDataLayer();
	}

	protected GRITSComparatorChooser<T> getComparatorChooser() {
		if (comparatorChooser == null) {
			tableFormat = new GRITSColumnTableFormat<T>(configRegistry, columnHeaderDataLayer);
			comparatorChooser =
					new GRITSComparatorChooser<T>(
							sortedList,
							tableFormat
					);
		}

		return comparatorChooser;
	}

	public int getRowPositionFromIndex( int _iIndex ) {
		return _iIndex;
	}
	
	public List<Integer> getSortedColumnIndexes() {
		return getComparatorChooser().getSortingColumns();
	}

	public int getSortOrder(int columnIndex) {
		return getComparatorChooser().getClickSequence(columnIndex);
	}

	public SortDirectionEnum getSortDirection(int columnIndex) {
		return getComparatorChooser().getSortDirectionForColumnIndex(columnIndex);
	}

	public boolean isColumnIndexSorted(int columnIndex) {
		return getComparatorChooser().isColumnIndexSorted(columnIndex);
	}

	public List<Comparator> getComparatorsForColumnIndex(int columnIndex) {
		return getComparatorChooser().getComparatorsForColumn(columnIndex);
	}

	public void sort(int columnIndex, SortDirectionEnum sortDirection, boolean accumulate) {
		getComparatorChooser().sort(columnIndex, sortDirection, accumulate);
//		parent.performAutoResizeAfterPaint();
//		this.sortedList.toString();
	}

	/**
	 * Restore state by leveraging {@link AbstractTableComparatorChooser}
	 */
	 public void loadState(String prefix, Properties properties) {
		 Object savedObject = properties.get(prefix + PERSISTENCE_KEY_GLAZEDLISTS_SORT_MODEL);
		 if(savedObject == null) {
			 return;
		 }
		 getComparatorChooser().fromString(savedObject.toString());
	 }

	 /**
	  * Save state by leveraging {@link AbstractTableComparatorChooser}
	  */
	  public void saveState(String prefix, Properties properties) {
		  properties.put(prefix + PERSISTENCE_KEY_GLAZEDLISTS_SORT_MODEL, getComparatorChooser().toString());
	  }

	  public void clear() {
		  getComparatorChooser().clearComparator();
	  }

	  @Override
	  public String toString() {
		  return super.toString();
	  }

	@Override
	public Comparator<?> getColumnComparator(int columnIndex) {
		return this.tableFormat.getColumnComparator(columnIndex);
	}

}
