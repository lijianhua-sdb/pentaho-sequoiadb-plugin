package org.pentaho.di.trans.steps.sequoiadbinput;

import org.bson.BSONObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.sequoiadb.base.CollectionSpace;
import com.sequoiadb.base.DBCollection;
import com.sequoiadb.base.DBCursor;
import com.sequoiadb.base.Sequoiadb;

public class SequoiaDBInput extends BaseStep implements StepInterface {
   
   private SequoiaDBInputMeta m_meta;
   private SequoiaDBInputData m_data;
   private DBCursor           m_cursor;

	public SequoiaDBInput(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean init(StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface) {
	   if(super.init(stepMetaInterface, stepDataInterface)){
	      m_meta = (SequoiaDBInputMeta)stepMetaInterface;
	      m_data = (SequoiaDBInputData)stepDataInterface;
	      
	      String connString = environmentSubstitute(m_meta.getHostname())
	            + ":" + environmentSubstitute(m_meta.getPort());
	      Sequoiadb sdb = null;
	      sdb = new Sequoiadb(connString, "", "");
	      if(!sdb.isCollectionSpaceExist(m_meta.getCSName())){
	         return false;
	      }
	      CollectionSpace cs = sdb.getCollectionSpace(m_meta.getCSName());
	      if(!cs.isCollectionExist(m_meta.getCLName())){
	         return false;
	      }
	      DBCollection cl = cs.getCollection(m_meta.getCLName());
	      m_cursor = cl.query();
	   }
	   return true;
	}

	@Override
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
	   // TODO close connection
	   super.dispose(smi, sdi);
	   m_cursor.close();
	}

	@Override
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException{
      // safely cast the step settings (meta) and runtime info (data) to specific implementations 
      SequoiaDBInputMeta meta = (SequoiaDBInputMeta) smi;
      SequoiaDBInputData data = (SequoiaDBInputData) sdi;

      // get incoming row, getRow() potentially blocks waiting for more rows, returns null if no more rows expected
      //Object[] r = getRow(); 
      
      // if no more rows are expected, indicate step is finished and processRow() should not be called again
      //if (r == null){
      //   setOutputDone();
      //   return false;
      //}

      // the "first" flag is inherited from the base step implementation
      // it is used to guard some processing tasks, like figuring out field indexes
      // in the row structure that only need to be done once
      if (first) {
         first = false;
         // clone the input row structure and place it in our data object
         data.outputRowMeta = new RowMeta();
         // use meta.getFields() to change it, so it reflects the output row structure 
         meta.getFields(data.outputRowMeta, getStepname(), null, null, SequoiaDBInput.this);
      }

      // read the record from SDB
      if(m_cursor.hasNext())
      {
         BSONObject obj = m_cursor.getNext();
         String json = obj.toString();
         Object row[] = RowDataUtil.allocateRowData(m_data.outputRowMeta.size());;
         row[0]=json;
         putRow(data.outputRowMeta, row);
      }
      else{
         setOutputDone();
         return false;
      }
      
      // safely add the string "Hello World!" at the end of the output row
      // the row array will be resized if necessary 
      //Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Hello World!");

      // put the row to the output row stream
      //putRow(data.outputRowMeta, outputRow); 

      // log progress if it is time to to so
      if (checkFeedback(getLinesRead())) {
         logBasic("Linenr " + getLinesRead()); // Some basic logging
      }

      // indicate that processRow() should be called again
      return true;
	}
}