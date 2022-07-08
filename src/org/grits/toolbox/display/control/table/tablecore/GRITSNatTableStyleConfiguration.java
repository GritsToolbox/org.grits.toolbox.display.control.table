package org.grits.toolbox.display.control.table.tablecore;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDisplayConverter;
import org.eclipse.nebula.widgets.nattable.export.ExportConfigAttributes;
import org.eclipse.nebula.widgets.nattable.export.excel.DefaultExportFormatter;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.cell.AlternatingRowConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.LineBorderDecorator;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class GRITSNatTableStyleConfiguration extends
		AbstractRegistryConfiguration {

	public final static Color oddColor = GUIHelper.COLOR_WHITE;
	public final static Color evenColor = GUIHelper.getColor(203,203,203);
	public final static Color headerColor = GUIHelper.getColor(203, 203, 203);
	
	public final static Color fgColor = GUIHelper.COLOR_BLACK;
	public final static Color gradientBgColor = GUIHelper.COLOR_WHITE;
	public final static Color gradientFgColor = GUIHelper.getColor(136, 212, 215);
	public final static Font font = GUIHelper.DEFAULT_FONT;
	public final static HorizontalAlignmentEnum hAlign = HorizontalAlignmentEnum.CENTER;
	public final static VerticalAlignmentEnum vAlign = VerticalAlignmentEnum.MIDDLE;
	public final static BorderStyle borderStyle = new BorderStyle(1, GUIHelper.COLOR_BLACK, LineStyleEnum.SOLID);

	public final static ICellPainter cellPainter = new LineBorderDecorator(new TextPainter(true, true, 0, true));
	
	public void configureRegistry(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, cellPainter);

		Style cellStyle = getNewStyle();
		
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultDisplayConverter());		
		configRegistry.registerConfigAttribute(ExportConfigAttributes.EXPORT_FORMATTER, new DefaultExportFormatter());
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, GridRegion.COLUMN_HEADER);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, GridRegion.CORNER);
//		configureOddRowStyle(configRegistry);
//		configureEvenRowStyle(configRegistry);
	}

	public static Style getNewStyle() {
		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, headerColor);
		cellStyle.setAttributeValue(CellStyleAttributes.FONT, font);
		cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, hAlign);
		cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, vAlign);
		cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, borderStyle);
		return cellStyle;
	}
//	cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, fgColor);
//	cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, gradientBgColor);
//	cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, gradientFgColor);
//	cellStyle.setAttributeValue(CellStyleAttributes.FONT, new Font(Display.getCurrent(), "Courier", 10, SWT.NORMAL));
	
	protected void configureOddRowStyle(IConfigRegistry configRegistry) {
		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, oddColor);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, AlternatingRowConfigLabelAccumulator.EVEN_ROW_CONFIG_TYPE);
	}

	protected void configureEvenRowStyle(IConfigRegistry configRegistry) {
		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, evenColor);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, AlternatingRowConfigLabelAccumulator.ODD_ROW_CONFIG_TYPE);
	}


}
