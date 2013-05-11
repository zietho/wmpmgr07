package com.util.models;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.database.bean.Sotivity;

  
public class SotivityDataModel extends ListDataModel<Sotivity> implements SelectableDataModel<Sotivity>,Serializable {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SotivityDataModel() {  
    }  
  
    public SotivityDataModel(List<Sotivity> data) {  
        super(data);  
    }  
      
    @Override  
    public Sotivity getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Sotivity> sotivities = (List<Sotivity>) getWrappedData();  
          
        for(Sotivity sotivity : sotivities) {  
            if(sotivity.getId().toString().equals(rowKey))  
              return sotivity;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Sotivity sotivity) {  
        return sotivity.getId();  
    	
    }

	
}  