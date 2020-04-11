import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class fdtm extends AbstractTableModel {

    private final Collection<fd> values;
    private List<fd> list;

    public fdtm(Collection<fd> values, boolean ok) {
        this.values = values;
        update(ok);
    }

    public void update(boolean ok) {
        System.out.println("update " + ok);
        list = values.stream().filter(v -> ok || !v.ok()).collect(Collectors.toList());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0: return "file";
            case 1: return "diff";
            default: throw new RuntimeException();
        }
    }

    @Override
    public Object getValueAt(int r, int c) {
        fd v = list.get(r);
        switch (c) {
            case 0: return v.k;
            case 1: return v.d();
            default: throw new RuntimeException();
        }
    }
}
