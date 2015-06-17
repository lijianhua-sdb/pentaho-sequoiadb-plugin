package org.pentaho.di.trans.steps.sequoiadbinput;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.sequoiadb.SequoiaDBField;
import org.pentaho.di.trans.steps.sequoiadb.SequoiaDBMeta;
import org.pentaho.di.ui.trans.steps.sequoiadbinput.SequoiaDBInputDialog;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(	
		id = "SequoiaDBInput",
		image = "org/pentaho/di/trans/steps/SequoiaDB.png",
		i18nPackageName="org.pentaho.di.TRANS.steps.sequoiadbinput",
		name="SequoiaDBInput.Name",
		description = "SequoiaDBInput.TooltipDesc",
		categoryDescription = "SequoiaDBInput.categoryDescription"
)

public class SequoiaDBInputMeta extends SequoiaDBMeta {

   private static Class<?> PKG = SequoiaDBInputMeta.class;// for i18n purposes

   private List<SequoiaDBField> m_fields;

   public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
      return new SequoiaDBInputDialog(shell, meta, transMeta, name);
   }

   @Override
   public Object clone() {
      SequoiaDBInputMeta retval =(SequoiaDBInputMeta) super.clone();
      return retval;
   }

   @Override
   public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface,
         int cnr, TransMeta transMeta, Trans disp) {
      return new SequoiaDBInput(stepMeta, stepDataInterface, cnr, transMeta, disp);
   }

   @Override
   public StepDataInterface getStepData() {
      return new SequoiaDBInputData();
   }

   @Override
   public void setDefault() {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step ) throws KettleException{
      rep.saveStepAttribute( id_transformation, id_step, "hostname", getHostname() );
      rep.saveStepAttribute( id_transformation, id_step, "port", getPort() );
      rep.saveStepAttribute( id_transformation, id_step, "CSName", getCSName() );
      rep.saveStepAttribute( id_transformation, id_step, "CLName", getCLName() );
   }

   @Override
   public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
     throws KettleException {
      setHostname( rep.getStepAttributeString( id_step, "hostname" ) );
      setPort( rep.getStepAttributeString( id_step, "port" ) );
      setCSName( rep.getStepAttributeString( id_step, "CSName" ) );
      setCLName( rep.getStepAttributeString( id_step, "CLName" ) );
   }

   @Override
   public String getXML() {
     StringBuffer retval = new StringBuffer( 300 );
     retval.append( "    " ).append( XMLHandler.addTagValue( "hostname", getHostname() ) );
     retval.append( "    " ).append( XMLHandler.addTagValue( "port", getPort() ) );
     retval.append( "    " ).append( XMLHandler.addTagValue( "CSName", getCSName() ) );
     retval.append( "    " ).append( XMLHandler.addTagValue( "CLName", getCLName() ) );
     return retval.toString();
   }

   @Override
   public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
     String strTmp;
     strTmp = XMLHandler.getTagValue( stepnode, "hostname" );
     if ( !strTmp.isEmpty())
     {
        setHostname( strTmp );
     }
     strTmp = XMLHandler.getTagValue( stepnode, "port" );
     if ( !strTmp.isEmpty())
     {
        setPort( strTmp );
     }
     strTmp = XMLHandler.getTagValue( stepnode, "CSName" );
     if ( !strTmp.isEmpty())
     {
        setCSName( strTmp );
     }
     strTmp = XMLHandler.getTagValue( stepnode, "CLName" );
     if ( !strTmp.isEmpty())
     {
        setCLName( strTmp );
     }
   }

   @SuppressWarnings( "deprecation" )
   @Override
   public void getFields( RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep,
         VariableSpace space ) throws KettleStepException {
      if ( m_fields == null || m_fields.size() == 0 ){
         // TODO: get the name "json" from dialog
         ValueMetaInterface jsonValueMeta = new ValueMeta( "JSON", ValueMetaInterface.TYPE_STRING);
         jsonValueMeta.setOrigin( origin );
         rowMeta.addValueMeta( jsonValueMeta );
      }else{
         // get the selected fields
         for ( SequoiaDBField f : m_fields ){
            ValueMetaInterface vm = new ValueMeta();
            vm.setName( f.m_fieldName );
            vm.setOrigin( origin );
            vm.setType( ValueMeta.getType( f.m_kettleType ));
            rowMeta.addValueMeta( vm );
         }
      }
   }
}