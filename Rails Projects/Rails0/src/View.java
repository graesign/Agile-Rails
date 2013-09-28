
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Font;

public abstract class View extends JPanel {

    public View() {
    }

    protected abstract void maakGUIItems();
    
    protected void setGUIItemsLokaties( JComponent[] items, Dimension[] lokaties ) {
        for( int i = 0; i < items.length; i++ ) {
            items[i].setLocation( lokaties[i].width, lokaties[i].height );
        }
    }
    protected void setGUIItemsLokaties( JComponent[] items, Dimension lokatie ) {
        for (JComponent item : items) {
            item.setLocation( lokatie.width, lokatie.height );
        }
    }
    protected void setGUIItemsAfmetingen( JComponent[] items, Dimension[] afmetingen ) {
        for( int i = 0; i < items.length; i++ ) {
            items[i].setSize( afmetingen[i] );
        }
    }
    protected void setGUIItemsAfmetingen( JComponent[] items, Dimension afmeting ) {
        for (JComponent item : items) {
            item.setSize( afmeting );
        }
    }
    protected void setGUIItemsFont( JComponent[] items, Font font ) {
        for( JComponent item: items ) {
            item.setFont(font);
        }
    }
    protected void setGUIItemsFont( JComponent[] items, Font[] font ) {
        for( int i = 0; i < items.length; i++ ) {
            items[i].setFont( font[i] );
        }
    }
    
    protected void setGUIItemsZichtbaarheid(JComponent[] items, boolean zichtbaarheid) {
        for (JComponent item : items) {
            item.setVisible(zichtbaarheid);
        }
    }
    protected void setGUIItemsZichtbaarheid(JComponent[] items, boolean[] zichtbaarheid) {
        for( int i = 0; i < items.length; i++ ) {
            items[i].setVisible(zichtbaarheid[i]);
        }
    }

    protected void setGUIItemsActieLuisteraars( JComponent[] items, ActionListener[] actieLuisteraars ) {
        for( int i = 0; i < items.length; i++ ) {
            ((javax.swing.JButton)items[i]).addActionListener( actieLuisteraars[i] );
        }
    }
    
    protected void voegGUIItemsToe(JComponent[] items) {
        for (JComponent item : items) {
            this.add(item);
        }
    }
    protected void voegGUIItemsToe(Container paneel, JComponent[] items) {
        for (JComponent item : items) {
            paneel.add(item);
        }
    }
}
