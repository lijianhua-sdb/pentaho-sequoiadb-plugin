package org.pentaho.di.trans.steps.sequoiadbinput;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class SequoiaDBInputData extends BaseStepData implements StepDataInterface {

   public RowMetaInterface outputRowMeta;
}