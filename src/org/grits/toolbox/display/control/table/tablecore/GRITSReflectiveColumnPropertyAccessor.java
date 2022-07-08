package org.grits.toolbox.display.control.table.tablecore;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

public class GRITSReflectiveColumnPropertyAccessor<R> implements IColumnPropertyAccessor<R> {

	//log4J Logger
	private static final Logger logger = Logger.getLogger(GRITSReflectiveColumnPropertyAccessor.class);
	
	private final List<String> propertyNames;

	private Map<String, PropertyDescriptor> propertyDescriptorMap;

	/**
	 * @param propertyNames of the members of the row bean
	 */
	public GRITSReflectiveColumnPropertyAccessor(List<String> propertyNames) {
		this.propertyNames = propertyNames;
	}
	
	public int getColumnCount() {
		return propertyNames.size();
	}

	public Object getDataValue(R rowObj, int columnIndex) {
		try {
			PropertyDescriptor propertyDesc = getPropertyDescriptor(rowObj, columnIndex);
			Method readMethod = propertyDesc.getReadMethod();
			return readMethod.invoke(rowObj);
		} catch (Exception e) {
			logger.warn(e);
			throw new RuntimeException(e);
		}
	}

	public void setDataValue(R rowObj, int columnIndex, Object newValue) {
		try {
			PropertyDescriptor propertyDesc = getPropertyDescriptor(rowObj, columnIndex);
			Method writeMethod = propertyDesc.getWriteMethod();
			if (writeMethod == null) {
				throw new RuntimeException("Setter method not found in backing bean for value at column index: " + columnIndex); //$NON-NLS-1$
			}
			writeMethod.invoke(rowObj, newValue);
		} catch (IllegalArgumentException ex) {
			System.err.println("Data type being set does not match the data type of the setter method in the backing bean"); //$NON-NLS-1$
		} catch (Exception e) {
			logger.warn(e);
			throw new RuntimeException("Error while setting data value"); //$NON-NLS-1$
		}
	};

	public String getColumnProperty(int columnIndex) {
		return propertyNames.get(columnIndex);
	}

	public int getColumnIndex(String propertyName) {
		return propertyNames.indexOf(propertyName);
	}

	private PropertyDescriptor getPropertyDescriptor(R rowObj, int columnIndex) throws IntrospectionException {
		if (propertyDescriptorMap == null) {
			propertyDescriptorMap = new HashMap<String, PropertyDescriptor>();
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(rowObj.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String someString = propertyDescriptor.getName();
				propertyDescriptorMap.put(someString.substring(0,1).toLowerCase() + someString.substring(1), propertyDescriptor);
			}
		}
		final String propertyName = propertyNames.get(columnIndex);
		return propertyDescriptorMap.get(propertyName);
	}

}
