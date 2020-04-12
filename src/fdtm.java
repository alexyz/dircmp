import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class fdtm extends AbstractTableModel {

    private final Collection<fd> values;
    private List<fd> list;

    public fdtm(Collection<fd> values) {
        this.values = values;
        this.list = Collections.emptyList();
    }

    public long update(boolean ok, boolean left, boolean right, boolean conflict) {
        list = values.stream().filter(v -> v.matches(ok, left, right, conflict)).collect(Collectors.toList());
        long t = list.stream().mapToLong(v -> v.size()).sum();
        fireTableDataChanged();
        return t;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int c) {
        switch (c) {
            case 0: return String.class;
            case 1: return Long.class;
            case 2: return String.class;
            default: throw new RuntimeException();
        }
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0: return "file";
            case 1: return "size";
            case 2: return "diff";
            default: throw new RuntimeException();
        }
    }

    @Override
    public Object getValueAt(int r, int c) {
        fd v = list.get(r);
        switch (c) {
            case 0: return v.name;
            case 1: return Long.valueOf(v.size());
            case 2: return v.difference();
            default: throw new RuntimeException();
        }
    }
}
