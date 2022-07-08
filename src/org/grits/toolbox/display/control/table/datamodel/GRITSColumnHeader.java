package org.grits.toolbox.display.control.table.datamodel;

@SuppressWarnings("rawtypes")
public class GRITSColumnHeader implements Comparable {
	private String sLabel = null;
	private String sKeyValue = null;
	private boolean bIsGrouped = true;
	public GRITSColumnHeader( String sLabel, String sKeyValue ) {
		this.sLabel = sLabel;
		this.sKeyValue = sKeyValue;
	}
			
	public boolean isGrouped() {
		return bIsGrouped;
	}
	
	public void setIsGrouped( boolean _bVal ) {
		this.bIsGrouped = _bVal;
	}
	
	public String getKeyValue() {
		return sKeyValue;
	}	
	
	public String getLabel() {
		return sLabel;
	}
	
	@Override
	public int hashCode() {
		return sKeyValue.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof GRITSColumnHeader) )
			return false;
		return this.getKeyValue().equals( ((GRITSColumnHeader) obj).getKeyValue() );
	}

	@Override
	public int compareTo(Object arg0) {
		if ( ! (arg0 instanceof String) )
			return -1;
		return sLabel.compareTo( (String) arg0 );
	}
	
	@Override
	public String toString() {
//		return getKeyValue() + ":" + getLabel();
		return getLabel();
	}

}