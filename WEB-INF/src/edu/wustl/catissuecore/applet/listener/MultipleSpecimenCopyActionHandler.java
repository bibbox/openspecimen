package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class handles multiple specimen copy operation.This handler gets called
 * when user clicks on Copy button of multiple specimen.</p>
 * @author Ashwin Gupta
 * @author mandar_deshmukh
 * @version 1.1
 */
public class MultipleSpecimenCopyActionHandler extends AbstractCopyActionHandler 
{
	private JButton paste;
	
	/**
	 * Empty constructor
	 */
	public MultipleSpecimenCopyActionHandler()
	{
		super();
	}
	/**
	 * @param table
	 */
	public MultipleSpecimenCopyActionHandler(JTable table, JButton paste)
	{
		super(table);
		this.paste = paste;
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent event) 
	{
		CommonAppletUtil.getSelectedData(table);
		paste.setEnabled(true);
		super.doActionPerformed(event);
		/*
		 Commented as code move to common abstractcopy action handler -- Ashwin 
		//to set selected data in model.
		//super.handleAction(event);
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("Inside MultipleSpecimenCopyActionHandler");
		
		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();
		int[] selectedColumns = table.getSelectedColumns();
		int[] selectedRows = table.getSelectedRows();
		System.out.println("selectedRows : ");
		CommonAppletUtil.printArray(selectedRows );
		System.out.println(" \n selectedColumns : ");
		CommonAppletUtil.printArray(selectedColumns );		  
		
		validatorModel.setSelectedCopiedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedCopiedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setCopiedData(CommonAppletUtil.getSelectedData(table) );
//for validator
		validatorModel.setOperation(AppletConstants.COPY_OPERATION);
		validatorModel.setColumnCount(CommonAppletUtil.getMultipleSpecimenTableModel(table).getTotalColumnCount());
		validatorModel.setRowCount(table.getRowCount());

		// for validation
		MultipleSpecimenCopyPasteValidator copyPasteValidator = new MultipleSpecimenCopyPasteValidator(table,validatorModel);
		String errorMessage = copyPasteValidator.validateForCopy();
		System.out.println("Message from copyPasteValidator.validateForCopy() : "+ errorMessage);
		if(errorMessage.trim().length()>0)
		{
		    Object[] parameters = new Object[]{errorMessage }; 
		    CommonAppletUtil.callJavaScriptFunction((JButton) e.getSource(),getJSMethodName(), parameters);
		}
		// for validation end
		CommonAppletUtil.getMultipleSpecimenTableModel(table).setCopyPasteOperationValidatorModel( validatorModel);
		System.out.println("\n >>>>>>>>>>>>>>   Copy Data Set.    >>>>>>>>>>>>");
		*/
		CommonAppletUtil.getMultipleSpecimenTableModel(table).showMapData(); 
				
		CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getMultipleSpecimenTableModel(table).getCopyPasteOperationValidatorModel();
		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
	
		
		/**
		 *  check if button(s) also copied
		 */
	      boolean isButtonCopied = false;
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				if(copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					System.out.println("copiedRow-->"+copiedRow);
					isButtonCopied = true;
					break;
				}
			}

	if(isButtonCopied)
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		System.out.println("inside -->" + isButtonCopied);
		Map validatorDataMap = new HashMap();
		validatorDataMap.put(AppletConstants.VALIDATOR_MODEL,validatorModel);
	//	validatorDataMap.put(AppletConstants.VALIDATOR_MODEL,"String");
		appletModel.setData(validatorDataMap);
		try
		{
			MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil
					.getBaseApplet(table);
			String url = applet.getServerURL()
					+ "/MultipleSpecimenCopyPasteAction.do?method=copy";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception");
		}
	}	
		
		System.out.println("\n >>>>>>>>>>>>>>  DONE >>>>>>>>>>>>");
	}
	
//	/*
//	 * This method returns the map holding the selected data.
//	 * The key is represented by the row@column format.
//	 */
//	private HashMap getSelectedData(int[] selectedRows, int[] selectedColumns)
//	{
//		System.out.println("\n/////////// inside getSelectedData ///////////////////\n");
//		HashMap map = new HashMap();
//		for(int rowIndex=0;rowIndex<selectedRows.length; rowIndex++  )
//		{
//			for(int columnIndex=0; columnIndex<selectedColumns.length; columnIndex++)
//			{
//				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex], selectedColumns[columnIndex]);
//				//commented to check the values from cell editor
////				Object value = table.getValueAt(selectedRows[rowIndex],selectedColumns[columnIndex] );
//				//--------
//				TableColumnModel columnModel = table.getColumnModel();
//				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(selectedColumns[columnIndex]).getCellEditor();
//				JComponent component = ((JComponent)scm.getTableCellEditorComponent(table,null,true,selectedRows[rowIndex],selectedColumns[columnIndex]));
//				Object value =scm.getCellEditorValue(); 	
//				// -------
//				map.put(key,value );
//			}
//		}
//		System.out.println("Returning Map -------------------------\n");
//		System.out.println(map);
//		return map;
//	}
	


/**
* @return JS method name for this button.
*/
protected String getJSMethodName()
{
	return "showErrorMessage";
}

/**
 * @return total coumn count
 */
protected int getColumnCount()
{
	return CommonAppletUtil.getMultipleSpecimenTableModel(table).getTotalColumnCount();		
}
	
}
