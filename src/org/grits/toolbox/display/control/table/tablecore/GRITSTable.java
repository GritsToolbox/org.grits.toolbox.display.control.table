package org.grits.toolbox.display.control.table.tablecore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.columnCategories.ChooseColumnsFromCategoriesCommandHandler;
import org.eclipse.nebula.widgets.nattable.columnCategories.ColumnCategoriesModel;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultComparator;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.RowHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.LayerUtil;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.layer.stack.ColumnGroupBodyLayerStack;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.reorder.command.ColumnReorderCommand;
import org.eclipse.nebula.widgets.nattable.resize.action.ColumnResizeCursorAction;
import org.eclipse.nebula.widgets.nattable.resize.command.ColumnResizeCommand;
import org.eclipse.nebula.widgets.nattable.resize.command.InitializeAutoResizeRowsCommand;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEventMatcher;
import org.eclipse.nebula.widgets.nattable.resize.mode.ColumnResizeDragMode;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.action.ClearCursorAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GCFactory;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.grits.toolbox.display.control.table.command.GRITSTableDisplayColumnChooserCommand;
import org.grits.toolbox.display.control.table.command.GRITSTableDisplayColumnChooserCommandHandler;
import org.grits.toolbox.display.control.table.datamodel.GRITSColumnHeader;
import org.grits.toolbox.display.control.table.datamodel.GRITSListDataProvider;
import org.grits.toolbox.display.control.table.datamodel.GRITSListDataRow;
import org.grits.toolbox.display.control.table.datamodel.GRITSSortModel;
import org.grits.toolbox.display.control.table.datamodel.GRITSTableDataObject;
import org.grits.toolbox.display.control.table.preference.TableViewerColumnSettings;
import org.grits.toolbox.display.control.table.process.TableDataProcessor;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

/**
 * Extension of NatTable specifically GRITS. Includes all of the generic components
 * to create a grid view of data that facilitates hiding rows, filtering rows, 
 * setting rows invisible, and persistent column preferences.
 * 
 * Note that GRITSabl
 * 
 * @author D Brent Weatherly (dbrentw@uga.edu)
 * @see GRITSColumnHeaderDataProvider
 * @see GRITSListDataProvider
 * @see GRITSTableDataObject
 * @see TableDataProcessor
 */
public class GRITSTable extends NatTable implements MouseListener, IGritsTable {
	
	private static final Color HIGLIGHT_LIGHT = GUIHelper.getColor(new RGB(255, 242, 228));
	private static final Color HIGHTLIGHT_COLOR = GUIHelper.getColor(new RGB(255, 204, 153));
	public static final String FILTEREDSELECTED = "filteredAndSelected";
	public static final String FILTEREDNOTSELECTED = "filteredNotSelected";
	public static final String NOMATCHLABEL = "No Match";
	public static final String FILTEREDSELECTEDLABEL = "Match";
	public static final String FILTEREDNOTSELECTEDLABEL = "Hidden Match";
	public static final String NOTFILTERED = "notfiltered";
	// log4J Logger
	private static final Logger logger = Logger.getLogger(GRITSTable.class);
	protected DataLayer dataLayer = null;
	protected DataLayer columnHeaderDataLayer = null;
	protected SortedList sortedList = null;
	protected ColumnHideShowLayer columnHideShowLayer = null;
	protected RowHideShowLayer rowHideShowLayer = null;
	protected SelectionLayer selectionLayer = null;
	protected ConfigRegistry configRegistry = null;
	protected ViewportLayer viewportLayer = null;
	protected DefaultRowHeaderDataProvider rowHeaderDataProvider = null;
	protected RowHeaderLayer rowHeaderLayer = null;
	protected ColumnHeaderLayer columnHeaderLayer = null;
	protected ColumnGroupModel columnGroupModel = null;
	protected ColumnGroupHeaderLayer columnGroupHeaderLayer = null;
	protected GridLayer gridLayer = null;
	protected CornerLayer cornerLayer = null;
	protected EventList eventList = null;
	protected SortHeaderLayer sortHeaderLayer = null;
	protected ISortModel sortModel = null;

	protected static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	protected GRITSColumnHeaderDataProvider columnHeaderDataProvider = null;
	protected GRITSListDataProvider bodyDataProvider = null;
	protected GRITSTableDataObject simDataObject = null;
	protected TableDataProcessor tableDataProcessor = null; // keeping a reference
	private boolean bTableNeedsResize = true;

	protected HashMap<Integer, Integer> natToSourceIndexMap = null;
	protected HashMap<Integer, Integer> sourceToNatIndexMap = null;
	protected HashMap<Integer, Integer> sourceIdToSourceIndexMap = null;
	private int iPrevMaxWidth = -1;
    protected int iLastMouseDownRow = -1;
    
	// DBW 10/20/14:  I don't like putting this here, 
	// but it was most straightforward way to turn off
	// persistence from column chooser dialog.
	public static UpdatePreferencesFromColumnChooser updatePreferencesFromColumnChooser = new UpdatePreferencesFromColumnChooser();

	protected boolean bResetColumnPreferences = false;
	protected DefaultRowHeaderDataLayer rowHeaderDataLayer;
	
	public GRITSTable(Composite parent, TableDataProcessor tableDataExtractor) {
		super(parent, false);
		this.tableDataProcessor = tableDataExtractor;
	}

	/**
	 * <b>createMainTable: Step I<b><br>
	 * Performs all of the necessary initialization of a GRITSTable by calling individual init metehods.
	 * This method or any of the individual ones can be sub-classed for particular implementations.
	 */
	public void initCommonTableComponents() {		
		initEventList();  // Step 1
		initSortedList();  // Step 2
		initDataProvider();  // Step 3
		initColumnHeaderDataLayer(); // Step 4
		initConfigRegistry(); // Step 5
		initSortComparator(); // Step 6
		initColumnHideShowLayer(); // Step 7
		initRowHideShowLayer(); // Step 8
		initSelectionLayer(); // Step 9
		initViewportLayer(); // Step 10
		initRowHeaderLayer(); // Step 11
		initColumnHeaderLayer(); // Step 12
		initColumnGroupHeaderLayer(); // Step 13
		setColumnGroupIndices(); // Step 14
		initViewPreferences(); // Step 15
		initSortHeaderLayer(); // Step 16
		initCornerLayer(); // Step 17
		initGridLayer(); // Step 18
		initCellAccumulator(); // Step 19
	}
	
	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getTableDataProcessor()
	 */
	public TableDataProcessor getTableDataProcessor() {
		return tableDataProcessor;
	}


	/**
	 * @throws Exception if tableDataExtractor is null
	 */
	public void loadData() throws Exception {
		if (this.tableDataProcessor == null) {
			throw new Exception("Null XML Data Extractor. Cannot create table!");
		}
	}
	
	/**
	 * This is the main method to be called to create a GRITSTable.
	 * @throws Exception
	 */
	public void createMainTable() throws Exception {
		initCommonTableComponents();  // createMainTable: Step I
		initColumnChooserLayer(); // createMainTable: Step II
		finishNatTable(); // createMainTable: Step III
		initialSort(); // createMainTable: Step IV
	}
	
	/**
	 * <b>createMainTable: Step IV</b><br>
	 * sort the table by a certain column initially.
	 * no generic implementation provided but subclasses can override to provide their 
	 * preferred initial sort column
	 */
	protected void initialSort() {
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getGRITSTableDataObject()
	 */
	@Override
	public GRITSTableDataObject getGRITSTableDataObject() {
		return simDataObject;
	}

	/**
	 * @param simDataObject 
	 * 		The GRITSTableDataObject read from the project archive. This is the core underlying data.
	 */
	public void setSimDataObject(GRITSTableDataObject simDataObject) {
		this.simDataObject = simDataObject;
	}

	/**
	 * If during initialization of table, an error occurs between preferences and the columns in the 
	 * table, then the preferences probably need to be reset.
	 */
	public boolean getResetColumnPreferences() {
		return bResetColumnPreferences;
	}
	
	/**
	 * @param _iSourcePos
	 * 		Index in the source table (getGRITSTableDataObject().getTableData()) to be converted to currently visible GRITSTable
	 * @return index in the GRITSTable of the source table if found. Otherwise returns -1.
	 */
	public int getNatIndexFromSourceIndex( int _iSourcePos ) {
		if( sourceToNatIndexMap.containsKey(_iSourcePos) ) {
			return sourceToNatIndexMap.get(_iSourcePos);
		}
		return -1;
	}

	/**
	 * @param _iNatIndex
	 * 		Index in the currently visible GRITSTable to be converted to the source table (getGRITSTableDataObject().getTableData())
	 * @return index in the source data layer of the GRITSTable index, if found. Otherwise returns -1.
	 */
	public int getSourceIndexFromNatIndex( int _iNatIndex ) {
		if( natToSourceIndexMap.containsKey(_iNatIndex) ) {
			return natToSourceIndexMap.get(_iNatIndex);
		}
		return -1;
	}

	/**
	 * @param _iRowId
	 * 		The ID assigned to each row.
	 * @return index in the source data layer of the Row Id, if found. Otherwise returns -1.
	 */
	public int getSourceIndexFromRowId( int _iRowId ) {
		if( sourceIdToSourceIndexMap.containsKey(_iRowId) ) {
			return sourceIdToSourceIndexMap.get(_iRowId);
		}
		return -1;
	}

	/**
	 * <b>Step 1</b><br>
	 * Initializes the data structures that will map the underlying dataset (getGRITSTableDataObject().getTableData()) 
	 * to those used by the GRITSTable.<br> 
	 */
	public void initEventList() {
		eventList = new BasicEventList();
		natToSourceIndexMap = new HashMap<>();
		sourceToNatIndexMap = new HashMap<>();
		sourceIdToSourceIndexMap = new HashMap<>();
		showAllRows();
	}

	/**
	 * Resets the core mapping of the underlying dataset (getGRITSTableDataObject().getTableData()) to the 
	 * indices in visible table and row id list. 
	 * 
	 * Note that the sourceIdToSourceIndexMap (row id to source index) is only initialized once, so row
	 * ids never change!
	 */
	protected void showAllRows() {
		// I don't like having to do this, but we have to clone each row to keep
		// a local copy in the event list. The backend list must never change
		// based on visibility
		eventList.clear();
		natToSourceIndexMap.clear();
		boolean bAddRowIds = sourceIdToSourceIndexMap.isEmpty();  // we only need to initialize this once!
		for (int i = 0; i < getGRITSTableDataObject().getTableData().size(); i++) {
			GRITSListDataRow row = getGRITSTableDataObject().getTableData().get(i);
			eventList.add(row.clone());
			natToSourceIndexMap.put(i, i);
			sourceToNatIndexMap.put(i, i);
			if( bAddRowIds ) {
				sourceIdToSourceIndexMap.put(row.getId(), i);
			}
		}
	}

	/**
	 * Resets the core mapping of the underlying dataset (getGRITSTableDataObject().getTableData()) to the 
	 * indices in visible table and row id list but skips the rows specified in the list "alHiddenRows"
	 * 
	 * @param lHiddenRows
	 * 		List of Integers representing the indices in the underlying dataset (getGRITSTableDataObject().getTableData())
	 * 	 	that should be skipped (hidden)
	 * @see EventList
	 */
	protected void hideRows(List<Integer> lHiddenRows) {
		eventList.clear();
		natToSourceIndexMap.clear();
		sourceToNatIndexMap.clear();
		int j = 0;
		for (int i = 0; i < getGRITSTableDataObject().getTableData().size(); i++) {
			if( ! lHiddenRows.contains(i) ) {
				GRITSListDataRow row = getGRITSTableDataObject().getTableData().get(i);
				eventList.add(row.clone());
				natToSourceIndexMap.put(j, i);
				sourceToNatIndexMap.put(i, j);
				j++;
			}
		}
	}

	/**
	 * @return true if the GRITSTable eventList is empty
	 * @see EventList
	 */
	protected boolean isEmpty() {
		return eventList.isEmpty();
	}
	
	/**
	 * <b>Step 2</b><br>
	 * Initializes a SortedList of the core eventList.
	 * 
	 * @see SortedList
	 * @see EventList
	 */
	public void initSortedList() {
		sortedList = new SortedList(eventList, null);
	}

	/**
	 * <b>Step 3</b><br>
	 * Initializes the body data provider of the table.
	 * @see GRITSListDataProvider
	 */
	public void initDataProvider() {
		bodyDataProvider = new GRITSListDataProvider(sortedList);
		dataLayer = new DataLayer(bodyDataProvider);
	}

	/**
	 * <b>Step 4</b><br>
	 * Initializes the column header data layer. 
	 * @see GRITSColumnHeaderDataProvider
	 * @see DefaultColumnHeaderDataLayer
	 */
	public void initColumnHeaderDataLayer() {
		columnHeaderDataProvider = new GRITSColumnHeaderDataProvider(getGRITSTableDataObject().getLastHeader());
		columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
	}

	/**
	 * <b>Step 5</b><br>
	 * Initializes the configuration registry.
	 * @see ConfigRegistry
	 */
	protected void initConfigRegistry() {
		configRegistry = new ConfigRegistry();
	}

	/**
	 * <b>Step 6</b><br>
	 * Initializes the sort comparator for the table.
	 * @see GRITSSortModel
	 */
	protected void initSortComparator() {
		// register sort comparator
		// List<String> sortHeader = getLastHeader();

		// IColumnPropertyAccessor columnPropertyAccessor = new
		// GRITSReflectiveColumnPropertyAccessor(sortHeader);
		configRegistry.registerConfigAttribute(
				SortConfigAttributes.SORT_COMPARATOR, new DefaultComparator());

		// initiates GlazedListSortModel
		sortModel = new GRITSSortModel(sortedList, configRegistry, this);
	}

	/**
	 * @return the current sort model
	 * @see ISortModel
	 */
	public ISortModel getSortModel() {
		return sortModel;
	}
	
	/**
	 * <b>Step 7</b><br>
	 * Initializes the column hide/show layer
	 * @see ColumnReorderLayer
	 * @see ColumnHideShowLayer
	 */
	public void initColumnHideShowLayer() {
		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(dataLayer);
		columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
	}

	/**
	 * @return the current  row hide/show layer
	 */
	public RowHideShowLayer getRowHideShowLayer() {
		return rowHideShowLayer;
	}

	/**
	 * <b>Step 8</b><br>
	 * Initializes the row hide/show layer
	 * @see RowHideShowLayer
	 */
	public void initRowHideShowLayer() {
		rowHideShowLayer = new RowHideShowLayer(columnHideShowLayer);
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getSelectionLayer()
	 */
	public SelectionLayer getSelectionLayer() {
		return selectionLayer;
	}

	/**
	 * <b>Step 9</b><br>
	 * Initializes the selection layer
	 * @see SelectionLayer
	 */
	public void initSelectionLayer() {
		selectionLayer = new SelectionLayer(rowHideShowLayer);
	}

	/**
	 * <b>Step 10</b><br>
	 * Initializeds the viewport layer
	 * @see ViewportLayer
	 */
	public void initViewportLayer() {
		viewportLayer = new ViewportLayer(selectionLayer);
	}

	/**
	 * @return the current viewport layer
	 */
	public ViewportLayer getViewportLayer() {
		return viewportLayer;
	}
	
	/**
	 * @return the current grid layer
	 */
	public GridLayer getGridLayer() {
		return gridLayer;
	}

	/**
	 * <b>Step 11</b><br>
	 * Initializes the row header providers and layer
	 * @see DefaultRowHeaderDataProvider
	 * @see DefaultRowHeaderDataLayer
	 * @see RowHeaderLayer
	 */
	protected void initRowHeaderLayer() {
		// Row header layer
		rowHeaderDataProvider = new DefaultRowHeaderDataProvider(
				bodyDataProvider);
		rowHeaderDataLayer = new DefaultRowHeaderDataLayer(
				rowHeaderDataProvider);
		rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, viewportLayer,
				selectionLayer);
	}

	protected ICellPainter getHeaderCellPainter() {
		return new BeveledBorderDecorator(new TextPainter(true, true, true));
	}
	
	/**
	 * <b>Step 12</b><br>
	 * Initializes the column header
	 * @see ColumnHeaderLayer
	 */
	public void initColumnHeaderLayer() {
		// columnHeaderLayer
		columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer,
				viewportLayer, selectionLayer);
		
		columnHeaderLayer.addConfiguration(new DefaultColumnHeaderLayerConfiguration() {
            @Override
            protected void addColumnHeaderStyleConfig() {
                addConfiguration(new DefaultColumnHeaderStyleConfiguration() {
                    {
                        this.cellPainter = getHeaderCellPainter();
                    }
                });
            }
        });
        
	}

	/**
	 * <b>Step 13</b><br>
	 * Initializes the column group header layer and related fields
	 * @see ColumnGroupModel
	 * @see ColumnGroupBodyLayerStack
	 * @see ColumnGroupHeaderLayer
	 */
	protected void initColumnGroupHeaderLayer() {
		columnGroupModel = new ColumnGroupModel();
		ColumnGroupBodyLayerStack bodyLayer = new ColumnGroupBodyLayerStack(
				this.dataLayer, columnGroupModel);
		columnGroupHeaderLayer = new ColumnGroupHeaderLayer(columnHeaderLayer,
				bodyLayer.getSelectionLayer(), columnGroupModel);
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getColumnHeaderLayer()
	 */
	public ColumnHeaderLayer getColumnHeaderLayer() {
		return columnHeaderLayer;
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getColumnGroupModel()
	 */
	public ColumnGroupModel getColumnGroupModel() {
		return this.columnGroupModel;
	}

	/**
	 * @return the current column group header layer
	 */
	public ColumnGroupHeaderLayer getColumnGroupHeaderLayer() {
		return this.columnGroupHeaderLayer;
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#hasColumnGroupHeader()
	 */
	public boolean hasColumnGroupHeader() {
		return getGRITSTableDataObject().getTableHeader().size() > 1;
	}

	/**
	 * @param _sKey
	 * 		The key value of a GRITSColumnHeader to be found in the source data (GRITS table data object)
	 * @return the index in the GRITS table data object or -1 if not found
	 * @see GRITSColumnHeader
	 * @see GRITSTableDataObject
	 */
	public int getColumnIndexForKey( String _sKey ) {
		List<GRITSColumnHeader> alRow = getGRITSTableDataObject().getLastHeader();
		for( int i = 0; i < alRow.size(); i++ ) {
			GRITSColumnHeader header = alRow.get(i);
			if( header.getKeyValue().equals(_sKey) ) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Instead of searching for an exact match for the column header key (equals) this uses contains.
	 * 
	 * @param _sKey
	 * 		The key value of a GRITSColumnHeader to be found in the source data (GRITS table data object)
	 * @return the index in the GRITS table data object or -1 if not found
	 * @see GRITSColumnHeader
	 * @see GRITSTableDataObject
	 */
	public int getColumnIndexForKeyByContains( String _sKey ) {
		List<GRITSColumnHeader> alRow = getGRITSTableDataObject().getLastHeader();
		for( int i = 0; i < alRow.size(); i++ ) {
			GRITSColumnHeader header = alRow.get(i);
			if( header.getKeyValue().contains(_sKey) ) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * <b>Step 14</b><br>
	 * Initializes the column header groups by grouping together headers in the first header row
	 * that have the same value. Once it locates the indices of the different groups, it sets these
	 * groups as unbreakable.
	 * @see ColumnGroupHeaderLayer
	 */
	protected void setColumnGroupIndices() {
		if (getGRITSTableDataObject().getTableHeader().size() <= 1)
			return;
		String sLastExp = null;
		List<Integer> alGroupIndices = new ArrayList<Integer>();
		for (int i = 0; i < getGRITSTableDataObject().getTableHeader().get(0).size(); i++) {
			Object oVal = getGRITSTableDataObject().getTableHeader().get(0).get(i);
			if (oVal == null && sLastExp == null)
				continue;
			boolean bChanged = (oVal != null && (sLastExp == null || !oVal
					.toString().equals(sLastExp)));
			if (bChanged) {
				sLastExp = oVal.toString();
				alGroupIndices.add(i);
			}
			columnGroupHeaderLayer.addColumnsIndexesToGroup(sLastExp, i);
			// if ( bChanged )
			// columnGroupHeaderLayer.setGroupUnbreakable(i);
		}
		for (int i = 0; i < alGroupIndices.size(); i++) {
			columnGroupHeaderLayer.setGroupUnbreakable(alGroupIndices.get(i));
		}
	}

	/**
	 * <b>Step 15</b><br>
	 * Attempts to load the preferences by calling updateViewFromPreferenceSettings(). If not successful,
	 * sets a variable to indicate that the user should reset the preferences to default.
	 */
	protected void initViewPreferences() {
		bResetColumnPreferences = false;
		try {
			boolean bRes = updateViewFromPreferenceSettings();
			if( ! bRes ) {
				bResetColumnPreferences = true;
			}
		} catch( Exception e ) {
			logger.error(e.getMessage());
			bResetColumnPreferences = true;
		}		
	}
	
	/**
	 * <b>Step 16</b><br>
	 * Initializes the sort header layer, comprised of 
	 * group header layer and sort model
	 * @see ColumnGroupHeaderLayer
	 * @see GRITSSortModel
	 */
	protected void initSortHeaderLayer() {
		sortHeaderLayer = new SortHeaderLayer(columnGroupHeaderLayer, sortModel, false);
	}

	/**
	 * <b>Step 17</b><br>
	 * Initializes the corner provider and corner layer, comprised of 
	 * corner data layer, row header layer, and sort header layer
	 * @see DefaultCornerDataProvider
	 * @see CornerLayer
	 */
	protected void initCornerLayer() {
		DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(
				columnHeaderDataProvider, rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, sortHeaderLayer);
	}

	/**
	 * <b>Step 18</b><br>
	 * Initializes the grid layer (top-most layer), comprised of
	 * viewport layer, sort header layer, row header layer, and corner layer
	 * @see GridLayer
	 */
	protected void initGridLayer() {
		gridLayer = new GridLayer(viewportLayer, sortHeaderLayer,
				rowHeaderLayer, cornerLayer);
	}

	/**
	 * <b>Step 19</b><br>
	 * Included for extending classes that need a cell accumulator to add formatting to the table
	 * based on values
	 */
	public void initCellAccumulator() {
		// nothing to do here
	}
	
	/**
	 * <b>reateMainTable: Step II</b><br>
	 * Initializes the column chooser layer to allow users to change visibility and order of the column headers
	 * using the built-in NatTable column chooser dialog. Associates a handler to the column group header layer.
	 * @see GRITSTableDisplayColumnChooserCommandHandler
	 */
	protected void initColumnChooserLayer() {
		GRITSTableDisplayColumnChooserCommandHandler columnChooserCommandHandler = new GRITSTableDisplayColumnChooserCommandHandler(this);
		columnGroupHeaderLayer.registerCommandHandler(columnChooserCommandHandler);
	}

	protected void configureColumnCategoriesInChooser() {
		ColumnCategoriesModel model = new ColumnCategoriesModel();

		columnHeaderDataLayer.registerCommandHandler(new ChooseColumnsFromCategoriesCommandHandler(
				this.columnHideShowLayer, this.columnHeaderLayer,
				this.dataLayer, model));
	}

	/**
	 * <b>createMainTable: Step III</b><br>
	 * Almost always the last step in creating a GRITSTable. Sets the default layer (grid layer),
	 * sets the configuration registry, default formatting, adds listeners, and lays out the window to display.
	 * @see getGRITSNatTableStyleConfiguration
	 * @see GRITSHeaderMenuConfiguration
	 * @see GRITSHeaderMenuConfiguration
	 * @see GRITSSingleClickConfiguration
	 * @see AbstractUiBindingConfiguration
	 */
	protected void finishNatTable() {
		this.setLayer(gridLayer);
		this.setConfigRegistry(configRegistry);
		this.addConfigurations();
		this.addMouseListener(this);
		this.configure();
	}
	
	/**
	 * adds all the required configurations to config registry
	 * subclasses may override to add their specific configurations, if necessary
	 */
	protected void addConfigurations () {
		// do not change the order of configurations here!
		this.addConfiguration(getGRITSNatTableStyleConfiguration());
		if (addHeaderListeners()) { // only add row header listeners to main table
			this.addConfiguration(getNewHeaderMenuConfiguration());
		}
		this.addConfiguration(getBodyMenuConfiguration());
		this.addConfiguration(new GRITSSingleClickConfiguration( getGRITSTableDataObject().getTableHeader().size() > 1));
		this.addConfiguration(getUIBindingConfiguration());
	}
	
	/** 
	 * need this configuration to allow row header column to be resizable by the user
	 * 
	 * @return a user interface configuration to allow row header column (first column in the table) to be resizable
	 */
	protected AbstractUiBindingConfiguration getUIBindingConfiguration () {
		return new AbstractUiBindingConfiguration() {		
			@Override
			public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
				uiBindingRegistry.registerMouseMoveBinding(new MouseEventMatcher(), new ClearCursorAction());
				uiBindingRegistry.registerFirstMouseMoveBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.CORNER, 0), new ColumnResizeCursorAction());
				// Column resize
				uiBindingRegistry.registerFirstMouseDragMode(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.CORNER, 1), new ColumnResizeDragMode());
			}
		};
	}
	
	/**
	 * @return a new style configuration object
	 */
	protected GRITSNatTableStyleConfiguration getGRITSNatTableStyleConfiguration() {
		return new GRITSNatTableStyleConfiguration();
	}

	/**
	 * @return a new header menu configuration object
	 */
	protected GRITSHeaderMenuConfiguration getNewHeaderMenuConfiguration() {
		return new GRITSHeaderMenuConfiguration(this);
	}

	/**
	 * @return a new header body menu configuration object
	 */
	protected GRITSHeaderMenuConfiguration getBodyMenuConfiguration() {
		return new GRITSHeaderMenuConfiguration(this);
	}

	/**
	 * Added here for extending classes that will implement their own header listeners
	 * @return true
	 */
	protected boolean addHeaderListeners() {
		return true;
	}

	/**
	 * Attempts to resize the last column of the table to ensure that it fits.
	 * NOTE: needs improvement!
	 */
	protected void hackResizeLastRowsAndCols() {
		int iColCnt = selectionLayer.getColumnCount();
		int iWidth = viewportLayer.getWidth();
		int iMaxWidth = viewportLayer.getClientAreaWidth();
		int iColWidthAdd = 0;
		iColWidthAdd = (int) ((double) (iMaxWidth - iWidth) / (double) (iColCnt-1));
		if (iMaxWidth > iWidth && iMaxWidth > iPrevMaxWidth) { // have some leftover and not already expanded

			for (int i = 1; i < iColCnt-1; i++ ) {
				viewportLayer.moveColumnPositionIntoViewport(i);
				int colPosition = LayerUtil.convertColumnPosition(
						selectionLayer, i, viewportLayer);
				if (colPosition < 0)
					continue;
				int iNewWidth = viewportLayer.getColumnWidthByPosition(i);
				if( i < (iColCnt - 1) ) {
					iNewWidth += iColWidthAdd;
				} else {
					iNewWidth = iNewWidth;
				}
				ColumnResizeCommand colResizeCommand = new ColumnResizeCommand(
						selectionLayer, colPosition,
						iNewWidth);
				this.doCommand(colResizeCommand);
			}
		}
		//		InitializeAutoResizeColumnsCommand columnCommand = new InitializeAutoResizeColumnsCommand(
		//				this, iColCnt-1, this.getConfigRegistry(), new GCFactory(this));
		//		boolean bRes = this.doCommand(columnCommand);
		//		iWidth = viewportLayer.getWidth();
		//		iMaxWidth = viewportLayer.getClientAreaWidth();
		iPrevMaxWidth = iMaxWidth;
		iColWidthAdd = 0;
	}

	/**
	 * Resizes rows that are visible. 
	 * NOTE: row resize needs improvement for complex formatting. Currently, resizing happens only when rows
	 * become visible, so scrolling rows into the viewport is necessary. This is not optimal.
	 */
	public void performAutoResize() {
		InitializeAutoResizeRowsCommand rowCommand1 = new InitializeAutoResizeRowsCommand(
				this, 2, this.getConfigRegistry(), new GCFactory(
						this));
		boolean bRes1 = this.doCommand(rowCommand1);
		boolean bGo = true;
		int iLastRowCnt = viewportLayer.getRowCount();
		while( bGo ) {
			for (int i = 0; i < iLastRowCnt; i++) {
				InitializeAutoResizeRowsCommand rowCommand = new InitializeAutoResizeRowsCommand(
						this, i, this.getConfigRegistry(), new GCFactory(this));
				boolean bRes = this.doCommand(rowCommand);		
			}
			int iRowCnt = viewportLayer.getRowCount();
			if( iRowCnt != iLastRowCnt ) {
				iLastRowCnt = iRowCnt;
			} else {
				bGo = false;
			}
			
		}
		hackResizeLastRowsAndCols();
		viewportLayer.moveCellPositionIntoViewport(0, 0);		
	}

	/*
	 * NOTE:  This is old code for attempts to improve row resizing. Leaving here for reference if anyone re-attempts
	 * optimization and wants to see what has been attempted previously.
	 * 
	 * 
	public void performAutoResize() {
		if( false ) return;
		InitializeAutoResizeRowsCommand rowCommand1 = new InitializeAutoResizeRowsCommand(
				this, 2, this.getConfigRegistry(), new GCFactory(
						this));
		boolean bRes1 = this.doCommand(rowCommand1);
		if( true ) return;
		int iColCnt = selectionLayer.getColumnCount();		
		boolean bChanged = false;
		for (int i = 0; i <= iColCnt; i++) {
			viewportLayer.moveColumnPositionIntoViewport(i, true);
			int colPosition = LayerUtil.convertColumnPosition(selectionLayer,
					i, viewportLayer);
			if (colPosition < 0)
				continue;

			InitializeAutoResizeColumnsCommand columnCommand = new InitializeAutoResizeColumnsCommand(
					this, colPosition, this.getConfigRegistry(), new GCFactory(this));
			boolean bRes = this.doCommand(columnCommand);
			bChanged |= bRes;
		}
		if( bChanged ) {
			iPrevMaxWidth = -1;
		}
//		selectionLayer.selectAll();
//		Object[] oRows = selectionLayer.getSelectedRowPositions().toArray();
		int[] iRows = new int[selectionLayer.getRowCount()];
		for( int i = 0; i < selectionLayer.getRowCount(); i++ ) {
			iRows[i] = i;
		}
		MultiRowResizeCommand mrrc = new MultiRowResizeCommand(selectionLayer, iRows, 1);
		boolean bRes2 = selectionLayer.doCommand(mrrc);
		if( false ) {
			int iRowCnt = selectionLayer.getRowCount();
			for (int i = 0; i < iRowCnt; i++) {
				viewportLayer.moveRowPositionIntoViewport(i, false);
				int rowPosition = LayerUtil.convertRowPosition(selectionLayer, i,
						viewportLayer);
				if (rowPosition < 0)
					continue;
				if( ! rowNeedsResize(rowPosition) ) {
					continue;
				}
				InitializeAutoResizeRowsCommand rowCommand = new InitializeAutoResizeRowsCommand(
						this, rowPosition, this.getConfigRegistry(), new GCFactory(
								this));
				boolean bRes = this.doCommand(rowCommand);
			}
		}
		hackResizeLastRowsAndCols();
		viewportLayer.moveCellPositionIntoViewport(0, 0, true);		
	}
	 */
	
	/**
	 * @return true if the window size has changed and therefore the table needs resizing.
	 */
	protected boolean tableNeedsResize() {
		return this.bTableNeedsResize;
	}
	
	protected void setTableNeedsResize( boolean _bVal ) {
		this.bTableNeedsResize = _bVal;
	}

	/**
	 * This is a bit of a hack but is here to improve performance. To iterate over all rows and resize them could 
	 * be very time consuming, so this method restricts this for the initial display of the table. After the first 200 or so
	 * rows are resized, the rest will be resized when they become visible as the user scrolls.
	 * 
	 * @param iRowNum
	 * 		row number in the table
	 * @return true if the row number is < 200 in the grid layer or < 500 in the source layer
	 */
	protected boolean rowNeedsResize( int iRowNum ) {
		return (iRowNum < 200 || getBottomDataLayer().getRowCount() < 500);
	}

	/**
	 * Re-applys the sort of the table based on the previously user-specified columns by the user.
	 * This is useful if you have to change visibility of the rows or filter and you want to return
	 * the table back to how the user was viewing it. Uses the column indices stored in the sort model's 
	 * sorted column indexes.
	 * @see GRITSSortModel
	 */
	public void reSort() {
		if( getSortModel().getSortedColumnIndexes() != null ) {
			for( Integer iCol : getSortModel().getSortedColumnIndexes() ) {
				getSortModel().sort(iCol, getSortModel().getSortDirection(iCol), true);
			}
		}		
	}

	/**
	 * Moves the desired data row from the bottom layer into view (the viewport layer)
	 * 
	 * @param _iRowNumInBottomLayer 
	 * 		the index of a row in the bottom-most data layer
	 */
	public void moveRowIntoViewport(int _iRowNumInBottomLayer) {
		viewportLayer.moveRowPositionIntoViewport(_iRowNumInBottomLayer);
	}

	/**
	 * Tells the current GRITSTable to resize itself after it gets painted. Once resized, removes the listener
	 * so the resize only happens once!
	 */
	public void performAutoResizeAfterPaint() {
		this.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if( tableNeedsResize() ) {
					performAutoResize();
					setTableNeedsResize(false);
				}
				removeListener(SWT.Paint, this);
			}
		});
	}

	/**
	 * @return the bottom (source) data layer
	 */
	public DataLayer getBottomDataLayer() {
		return dataLayer;
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getColumnHideShowLayer()
	 */
	public ColumnHideShowLayer getColumnHideShowLayer() {
		return columnHideShowLayer;
	}

	/* (non-Javadoc)
	 * @see org.grits.toolbox.display.control.table.tablecore.IGritsTable#getColumnHeaderDataLayer()
	 */
	public DataLayer getColumnHeaderDataLayer() {
		return columnHeaderDataLayer;
	}
	
	protected void registerDoubleStyles (IConfigRegistry configRegistry) {
		registerDoubleStyle(configRegistry, DoubleFormat.SCIENTIFIC_NOTATION);
		registerDoubleStyle(configRegistry, DoubleFormat.DECIMAL2DIGIT);
		registerDoubleStyle(configRegistry, DoubleFormat.DECIMAL4DIGIT);
	}
	
	protected void registerDoubleStyle (IConfigRegistry configRegistry, DoubleFormat label) {
		NumberFormat dec2 = new DecimalFormat("0.00");
		NumberFormat dec4 = new DecimalFormat("0.0000");
		NumberFormat scientific = new DecimalFormat("0.0E0");

		DisplayConverter doubleConverter = new DisplayConverter() {
	        @Override
	        public Object canonicalToDisplayValue(Object canonicalValue) {
	            if (canonicalValue == null) {
	                return "";
	            }
	            switch (label) {
		            case DECIMAL2DIGIT:
						return dec2.format(Double.valueOf(canonicalValue.toString()));
					case DECIMAL4DIGIT:
						return dec4.format(Double.valueOf(canonicalValue.toString()));
					case SCIENTIFIC_NOTATION:
						return scientific.format(Double.valueOf(canonicalValue.toString()));
					default:
						return dec2.format(Double.valueOf(canonicalValue.toString()));
	            }
	        }
	
	        @Override
	        public Object displayToCanonicalValue(Object displayValue) {
	            try {
	            	switch (label) {
		            case DECIMAL2DIGIT:
						return dec2.parse((String) displayValue);
					case DECIMAL4DIGIT:
						return dec4.parse((String) displayValue);
					case SCIENTIFIC_NOTATION:
						return scientific.parse((String) displayValue);
					default:
						return dec2.parse((String) displayValue);
	            	}
	            } catch (ParseException e) {
	                return null;
	            }
	        }
		};
		
		configRegistry.registerConfigAttribute(
                CellConfigAttributes.DISPLAY_CONVERTER,
                doubleConverter, DisplayMode.NORMAL,
                label.name());
	}
	
	/**
	 * registers filter styling for the table
	 * if addHightLight is true, adds background highlighting to the table based on the filter results
	 * it also adds display converter to display meaningful values for filter results such as "No Match", "Match", "Hidden Match"
	 * instead of displaying integer filter results (-1, 0, 1, 2 etc.)
	 * 
	 * @param configRegistry the registry to add the configurations
	 * @param addHighlight whether highlight style should be added or not
	 */
	protected void registerFilterStyle (IConfigRegistry configRegistry, boolean addHighlight) {	
		if (addHighlight) {
			Style filterSelectedStyle = new Style();
			filterSelectedStyle.setAttributeValue(
	                CellStyleAttributes.BACKGROUND_COLOR, HIGHTLIGHT_COLOR);
	
			configRegistry.registerConfigAttribute(
	                CellConfigAttributes.CELL_STYLE, filterSelectedStyle,
	                DisplayMode.NORMAL, FILTEREDSELECTED);
			
			Style filterNotSelectedStyle = new Style();
			filterNotSelectedStyle.setAttributeValue(
	                CellStyleAttributes.BACKGROUND_COLOR, HIGLIGHT_LIGHT);
	
			configRegistry.registerConfigAttribute(
	                CellConfigAttributes.CELL_STYLE, filterNotSelectedStyle,
	                DisplayMode.NORMAL, FILTEREDNOTSELECTED);
		}
		
		DisplayConverter filterConverter = new DisplayConverter() {
			
			@Override
			public Object displayToCanonicalValue(Object displayValue) {
				if (displayValue == null) {
					return null;
				}
				if (displayValue instanceof String) {
					if (((String) displayValue).isEmpty())
						return -1;
					if (((String)displayValue).equals(NOMATCHLABEL))
						return 0;
					else if (((String)displayValue).equals(FILTEREDNOTSELECTEDLABEL))
						return 1;
					else if (((String)displayValue).equals(FILTEREDSELECTEDLABEL))
						return 11;
					
				}
				return null;
			}
			
			@Override
			public Object canonicalToDisplayValue(Object canonicalValue) {
				if (canonicalValue == null)
					return "";
				if (canonicalValue instanceof Integer) {
					if (((Integer)canonicalValue).intValue() < 0)
						return "";
					else if (((Integer)canonicalValue).equals(0))
						return NOMATCHLABEL;
					else if (((Integer)canonicalValue).equals(1))
						return FILTEREDNOTSELECTEDLABEL;
					else if (((Integer)canonicalValue).equals(11))
						return FILTEREDSELECTEDLABEL;
				}
				return null;
			}
		};
		
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, filterConverter, DisplayMode.NORMAL, FILTEREDNOTSELECTEDLABEL);
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, filterConverter, DisplayMode.NORMAL, FILTEREDSELECTEDLABEL);
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, filterConverter, DisplayMode.NORMAL, NOMATCHLABEL);
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, filterConverter, DisplayMode.NORMAL, NOTFILTERED);
	}

	/**
	 * Adds the facility for displaying a checkbox on a table. Utilizes the override of the
	 * cell value in static GRITSColumnHeader "selColHeader" in TableDataProcessor
	 * @param configRegistry
	 * @see ConfigRegistry
	 * @see TableDataProcessor
	 */
	protected void registerSelectedCheckbox(IConfigRegistry configRegistry) {
		IEditableRule rule = new IEditableRule() {

			@Override
			public boolean isEditable(int arg0, int arg1) {
				if (arg0 == arg1) {
					return true;
				}
				return false;
			}

			@Override
			public boolean isEditable(ILayerCell arg0, IConfigRegistry arg1) {
				if (getBottomDataLayer().getDataValueByPosition(
						arg0.getColumnIndex(), arg0.getRowIndex()) != null)
					return true;
				return false;
			}
		};
		registerSelectedCheckbox(configRegistry, rule);
		
	}
	
	/**
	 * Adds the facility for displaying a checkbox on a table with the given rule for being editable. 
	 * Utilizes the override of the cell value in static GRITSColumnHeader "selColHeader" in TableDataProcessor
	 * @param configRegistry configuration registry to register the checkbox cell editor
	 * @param rule editable rule
	 */
	protected void registerSelectedCheckbox (IConfigRegistry configRegistry, IEditableRule rule) {
		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITABLE_RULE, rule,
				DisplayMode.EDIT, TableDataProcessor.selColHeader.getLabel());

		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(),
				DisplayMode.EDIT, TableDataProcessor.selColHeader.getLabel());

		// if you want to use the CheckBoxCellEditor, you should also consider
		// using the corresponding CheckBoxPainter to show the content like a
		// checkbox in your NatTable
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
				DisplayMode.NORMAL, TableDataProcessor.selColHeader.getLabel());

		// using a CheckBoxCellEditor also needs a Boolean conversion to work
		// correctly
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultBooleanDisplayConverter(), DisplayMode.NORMAL,
				TableDataProcessor.selColHeader.getLabel());
	}

	@Override
	public void configure() {
		super.configure();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

	/**
	 * Writes the current view's column setting to to workspace preference.
	 * NOTE: Is this ever used?
	 * @param _bForceRebuild
	 * 		whether or not to force writing 
	 */
	public void loadViewerSettings(boolean _bForceRebuild) {
		// loadViewerSettings();
		if (getGRITSTableDataObject().getTablePreferences().settingsNeedInitialization() || _bForceRebuild) {
			// then create a new object with empty value and save it in
			// preference.xml
			getGRITSTableDataObject().getTablePreferences().setPreferenceSettings(getPreferenceSettingsFromCurrentView());
			getGRITSTableDataObject().getTablePreferences().writePreference();
		}
	}
	
	/**
	 * @return the index of the row on which the user last hit mouse-down
	 */
	public int getLastMouseDownRow() {
		return iLastMouseDownRow;
	}

	/**
	 * @param iLastMouseDownRow
	 * 		the index of the row on which the user last hit mouse-down
	 */
	public void setLastMouseDownRow(int iLastMouseDownRow) {
		this.iLastMouseDownRow = iLastMouseDownRow;
	}
	
	/**
	 * @return a new instance of column settings representing to the currently visible columns in their order
	 */
	public TableViewerColumnSettings getPreferenceSettingsFromCurrentView() {
		TableViewerColumnSettings newEntity = new TableViewerColumnSettings();
		if (this.columnHeaderDataLayer == null
				|| this.columnHeaderDataLayer.getColumnCount() == 0)
			return null;
		int iNumCols = this.columnHeaderDataLayer.getColumnCount();
		int iPos = 0;
		// just set the order as found in the columnlayer
		for (int i = 0; i < iNumCols; i++) {
			String headerLabel = (String) this.columnHeaderDataLayer.getDataValueByPosition(i, 0);
			GRITSColumnHeader header = new GRITSColumnHeader(headerLabel, this.columnHeaderDataProvider.getDataKey(i, 0));
			newEntity.setVisColInx(header, iPos++);
		}
		return newEntity;
	}

	/**
	 * Updates the positions in the GRITStable based on current preferences in the GRITS table data object,
	 * which read them from persistent storage.
	 * 
	 * @return true if no errors encountered
	 */
	public boolean updateViewFromPreferenceSettings() {
		if (this.columnHeaderDataLayer == null || this.columnHeaderDataLayer.getColumnCount() == 0)
			return false;
		int iNumCols = this.columnHeaderDataLayer.getColumnCount();
		int iNumNonHidden = 0;
		if (iNumCols == 0)
			return false;
		
		// Step 1: show all columns
		this.columnHideShowLayer.showAllColumns(); // first show all columns
		
		// Step 2: determine which columns should be hidden based on what the preferences say
		ArrayList<Integer> alHiddenCols = new ArrayList<Integer>();
		for (int iColLayerPos = 0; iColLayerPos < iNumCols; iColLayerPos++) { 
			String sHeaderKey = this.columnHeaderDataProvider.getDataKey(iColLayerPos, 0);
			int iColShowLayerPos = LayerUtil.convertColumnPosition(
					this.columnHeaderDataLayer, iColLayerPos,
					this.columnHideShowLayer);
			
			// here is where we check whether a column is supposed to be hidden
			if (getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().hasColumn(sHeaderKey)) {
				int iPrefColPos = getGRITSTableDataObject().getTablePreferences()
						.getPreferenceSettings()
						.getVisColInx(sHeaderKey);
				logger.debug("From column header: key: " + sHeaderKey + ", position: " + iColLayerPos + ", pref col: " + iPrefColPos);
				if (iPrefColPos == -1) {
					alHiddenCols.add(iColShowLayerPos); // column is hidden. add to lis
				} else {
					iNumNonHidden++;
				}
			}
		}
		// Step 3: Go ahead and hide the desired columns
		this.columnHideShowLayer.hideColumnPositions(alHiddenCols);

		// Step 4: Check to see if we need to add the "Selection" column. If so, then all column positions will
		// be offset by 1 (Selection column always placed at position 0
		Object selCell = getGRITSTableDataObject().getLastHeader().get(0);
		boolean bAddSelect = selCell.equals(TableDataProcessor.selColHeader);
		
		// Step 5: No move columns around to match the preferred order 
		for (int iPrefColPos = 0; iPrefColPos < iNumNonHidden; iPrefColPos++) { // going in position order of the new PREFERENCES
			GRITSColumnHeader prefHeader = getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().getColumnAtVisColInx(iPrefColPos);
			if( prefHeader == null ) {
				continue;
			}
			for (int iFromPos = 0; iFromPos < this.columnHideShowLayer.getColumnCount(); iFromPos++) { // column position based
				int iColPos = LayerUtil.convertColumnPosition(this.columnHideShowLayer, iFromPos, this.columnHeaderDataLayer);
				String sThisHeaderKey = this.columnHeaderDataProvider.getDataKey(iColPos, 0);
				if (prefHeader.getKeyValue().equals(sThisHeaderKey)) {
					int iToPos = iPrefColPos;
					logger.debug("From pref: key: " + prefHeader.getKeyValue() + ", position: " + iPrefColPos);
					if (bAddSelect)
						iToPos++;
					if (iFromPos != iToPos) { // Preferred position does not match current position, so reorder
						ColumnReorderCommand command = new ColumnReorderCommand(this.columnHideShowLayer, iFromPos, iToPos);
						// logger.debug("Moving " + sThisHeaderKey + " from "
						// + iFromPos + " to " + iToPos );
						this.columnHideShowLayer.doCommand(command);
					} else {
						// logger.debug("Staying put: " + sThisHeaderKey +
						// " from " + iFromPos + " to " + iToPos );
					}
					break;
				}
			}

		}
		return true;
	}

	/**
	 * Updates the preferences in the GRITS table data object based on current column settings
	 * in the GRITSTable.
	 */
	public void updatePreferenceSettingsFromCurrentView() {
		if (this.columnHeaderDataLayer == null|| this.columnHeaderDataLayer.getColumnCount() == 0)
			return;
		int iNumCols = this.columnHeaderDataLayer.getColumnCount();
		int iNewNumCols = 0;
		// first iterate over columns in the base column layer to count the
		// number of visible columns
		// note that "special peaks" can only be counted once, so if they are
		// all visible, skip after seeing first instance
		for (int iColInx = 0; iColInx < iNumCols; iColInx++) {
			boolean bHidden = this.columnHideShowLayer.isColumnIndexHidden(iColInx);
			if (bHidden)
				continue;
			iNewNumCols++;
		}

		// now iterate over the visible columns using the columnshowlayer and
		// set the preference value
		int iToPos = 0;
		for (int iVisPos = 0; iVisPos < iNewNumCols; iVisPos++) { // position
			// based on the column show  header layer
			int iColPos = LayerUtil.convertColumnPosition(
					this.columnHideShowLayer, iVisPos,
					this.columnHeaderDataLayer);
			String sHeaderKey = this.columnHeaderDataProvider.getDataKey(iColPos, 0);
			if (getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().hasColumn(sHeaderKey)) {
				GRITSColumnHeader header = getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().getColumnHeader(sHeaderKey);
				getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().setVisColInx(header, iToPos++);
			}
		}

		for (int iColInx = 0; iColInx < iNumCols; iColInx++) { // index based
			// off of column layer (all data)
			boolean bHidden = this.columnHideShowLayer.isColumnIndexHidden(iColInx);
			if (!bHidden)
				continue;
			int iColPos = this.columnHeaderDataLayer.getColumnPositionByIndex(iColInx);
			String sHeaderKey = this.columnHeaderDataProvider.getDataKey(iColPos, 0);
			if (getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().hasColumn(sHeaderKey)) {
				GRITSColumnHeader header = getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().getColumnHeader(sHeaderKey);
				getGRITSTableDataObject().getTablePreferences().getPreferenceSettings().setVisColInx(header, -1);
			}
		} 
	}

	/**
	 * Creates and executes a command to open the GRITSTable column chooser.
	 * @see GRITSTableDisplayColumnChooserCommand
	 */
	public void showColumnDialog() {
		GRITSTableDisplayColumnChooserCommand command = new GRITSTableDisplayColumnChooserCommand(this);
		this.doCommand(command);
	}
}
