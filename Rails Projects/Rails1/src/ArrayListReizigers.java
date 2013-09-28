import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;


public class ArrayListReizigers extends AbstractListModel implements ComboBoxModel {
	
	private Object selectedItem;
	
	private ArrayList lijst_;
	
	public ArrayListReizigers(ArrayList lijst_){
		this.lijst_ = lijst_;
		
	}
	
	public int getSize(){
		return lijst_.size();
	}
	
	public Object getSelectedItem() {
	    return selectedItem;
	}
	
	 public void setSelectedItem(Object newValue) {
		 selectedItem = newValue;
		 }
	 
	 public Object getElementAt(int i) {
		 return lijst_.get(i);
		 }
}
