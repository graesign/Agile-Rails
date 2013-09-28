import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 * @author dareal
 * Deze klasse is een abstracte klasse die alle methodes van de AbstractListModel implmenenteert
 * Dit wordt voor de paneel klasse gebruikt om een array van de stations te maken. 
 */
public class ArrayListComboBoxModel extends AbstractListModel implements ComboBoxModel {
  private Object selectedItem;

  private ArrayList anArrayList;

  public ArrayListComboBoxModel(ArrayList arrayList) {
    anArrayList = arrayList;
  }

  public Object getSelectedItem() {
    return selectedItem;
  }

  public void setSelectedItem(Object newValue) {
    selectedItem = newValue;
  }

  public int getSize() {
    return anArrayList.size();
  }

  public Object getElementAt(int i) {
    return anArrayList.get(i);
  }
}
