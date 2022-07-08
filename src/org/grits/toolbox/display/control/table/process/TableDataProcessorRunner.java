package org.grits.toolbox.display.control.table.process;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.widgets.progress.CancelableThread;
import org.grits.toolbox.widgets.progress.IProgressThreadHandler;
import org.grits.toolbox.widgets.tools.GRITSProcessStatus;
/*
 * @author Brent Weatherly
 */
public class TableDataProcessorRunner  {
	private static final Logger logger = Logger.getLogger(TableDataProcessorRunner.class);
	protected TableDataProcessor extractor;
	protected Shell shell;
	protected Display display;

	public TableDataProcessorRunner(TableDataProcessor extractor) {
		this.extractor = extractor;
	}    

	public TableDataProcessor getExtractor() {
		return extractor;
	}

	public int startJob() {
		try {
			CancelableThread t = new CancelableThread() {
				@Override
				public boolean threadStart(IProgressThreadHandler a_progressThreadHandler) throws Exception {
					logger.debug("Starting job: " + extractor.getProcessType() );
					try {
						if( extractor.getProcessType() == TableDataProcessor.OPEN ) {
							setName("TableProcessor process type: OPEN");
							boolean bRes = extractor.readDataFromFile();
							return true;
						} else if( extractor.getProcessType() == TableDataProcessor.READ ) {
							setName("TableProcessor process type: READ");
							extractor.createTable();
						} else if (  extractor.getProcessType() == TableDataProcessor.WRITE ) {
							setName("TableProcessor process type: WRITE");
							extractor.saveChanges();
						} else {
							setName("TableProcess proces type: OPEN");
							throw new Exception("Unrecognized TableProcessor process.");
						}		
						return true;
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						return false;
					}
				}
			};
			t.setProgressThreadHandler(extractor.getProgressBarDialog());
			extractor.getProgressBarDialog().setThread(t);
			t.start();	
			while ( ! t.isCanceled() && ! t.isFinished() && t.isAlive() )  {
				Display.getDefault().readAndDispatch();
			}
			if( t.isCanceled() ) {
				extractor.cancelWork();
				t.interrupt();
				return GRITSProcessStatus.CANCEL;
			} else {
				return GRITSProcessStatus.OK;
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return GRITSProcessStatus.ERROR;
	}

	public void cancelWork() {
		extractor.cancelWork();	
	}

	public void close() throws IOException
	{
		// TODO: implement this?
	}

	protected ArrayList<Object> getNewRow( int iMaxNumCols ) {
		ArrayList<Object> alRow = new ArrayList<Object>(iMaxNumCols);
		for( int i = 0; i < iMaxNumCols; i++ ) {
			alRow.add(null);
		}
		return alRow;
	}

}