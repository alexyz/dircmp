import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class fdjtable extends JTable {

    @Override
    public TableCellRenderer getCellRenderer(int r, int c) {
        if (c == 1) {
            return new sizecr();
        } else {
            return super.getCellRenderer(r, c);
        }
    }
}
