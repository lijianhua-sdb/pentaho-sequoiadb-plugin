package org.pentaho.di.trans.steps.sequoiadb;

import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

public abstract class SequoiaDBMeta extends BaseStepMeta implements StepMetaInterface {

   private static Class<?> PKG = SequoiaDBMeta.class;// for i18n purposes

   private String m_hostname = "localhost";
   private String m_port = "11810";
   private String m_CSName;
   private String m_CLName;

   @Override
   public Object clone() {
      SequoiaDBMeta retval =(SequoiaDBMeta) super.clone();
      retval.m_hostname = m_hostname;
      retval.m_port = m_port;
      retval.m_CSName = m_CSName;
      retval.m_CLName = m_CLName;
      return retval;
   }
   
   public String getHostname() {
      return m_hostname;
   }

   public void setHostname(String hostname) {
     this.m_hostname = hostname;
   }
   
   public String getPort() {
      return m_port;
    }

    public void setPort(String port) {
      this.m_port = port;
    }
    
    public String getCSName() {
       return m_CSName;
     }

     public void setCSName(String CSName) {
       this.m_CSName = CSName;
     }
     
     public String getCLName() {
        return m_CLName;
      }

      public void setCLName(String CLName) {
        this.m_CLName = CLName;
      }
}