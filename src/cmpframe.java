import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class cmpframe extends JFrame {

    public static void main(String[] args) {
        cmpframe f = new cmpframe();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.show();
    }

    private final JTextField dir1f = new JTextField();
    private final JTextField dir2f = new JTextField();
    private final JTable table = new JTable();
    private final JButton cmpb = new JButton("Compare");
    private final JCheckBox showokcb = new JCheckBox("Show OK");
    private final JButton copyb = new JButton("Copy");

    public cmpframe() {
        setTitle("Dir Compare");
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        cmpb.addActionListener(e -> cmp());
        showokcb.addItemListener(e -> setok());
        JScrollPane sp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel p2 = new JPanel();
        p2.add(cmpb);
        p2.add(showokcb);
        p2.add(copyb);
        JPanel p1 = new JPanel(new GridBagLayout());
        p1.add(new JLabel("Left"), new gbc().p(1, 1));
        p1.add(dir1f, new gbc().p(2, 1).wx().fx());
        p1.add(fbutton(dir1f), new gbc().p(3, 1));
        p1.add(new JLabel("Right"), new gbc().p(1, 2));
        p1.add(dir2f, new gbc().p(2, 2).wx().fx());
        p1.add(fbutton(dir2f), new gbc().p(3, 2));
        p1.add(p2, new gbc().p(1, 3).sx().wx());
        p1.add(sp, new gbc().p(1, 4).sboth().wboth().fboth());
        setContentPane(p1);
        setPreferredSize(new Dimension(800, 600));
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                savePref();
            }
        });
        loadPref();
    }

    private void setok() {
        TableModel m = table.getModel();
        if (m instanceof fdtm) {
            ((fdtm)m).update(showokcb.isSelected());
        }
    }

    private void savePref() {
        Preferences p = Preferences.userNodeForPackage(getClass());
        p.put("dir1", dir1f.getText());
        p.put("dir2", dir2f.getText());
        try {
            p.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private void loadPref() {
        Preferences p = Preferences.userNodeForPackage(getClass());
        dir1f.setText(p.get("dir1", ""));
        dir2f.setText(p.get("dir2", ""));
    }

    private void cmp() {
        File dir1 = new File(dir1f.getText());
        File dir2 = new File(dir2f.getText());
        if (dir1.isDirectory() && dir2.isDirectory()) {
            Map<String, File> m1 = find(dir1.getAbsolutePath(), dir1, new TreeMap<>());
            Map<String, File> m2 = find(dir2.getAbsolutePath(), dir2, new TreeMap<>());
            Map<String, fd> dest = new TreeMap<>();
            addto(dest, m1, m2, false);
            addto(dest, m2, m1, true);
            table.setModel(new fdtm(dest.values(), showokcb.isSelected()));
            table.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "f1/f2 not a dir");
        }
    }

    private static void addto(Map<String, fd> dest, Map<String, File> m1, Map<String, File> m2, boolean rev) {
        m1.entrySet().stream().forEach(e -> dest.computeIfAbsent(e.getKey(), k -> {
            File f1 = e.getValue(), f2 = m2.get(k);
            return rev ? new fd(k, f2, f1) : new fd(k, f1, f2);
        }));
    }

    private static Map<String, File> find(String base, File dir, Map<String, File> map) {
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                map.put(f.getAbsolutePath().substring(base.length()), f);
            } else if (f.isDirectory()) {
                find(base, f, map);
            }
        }
        return map;
    }

    private JButton fbutton(JTextField f) {
        JButton b = new JButton("...");
        b.addActionListener(e -> {
            String t = f.getText();
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(t.length() > 0 ? new File(t) : null);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(f.getParent()) == JFileChooser.APPROVE_OPTION) {
                f.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        return b;
    }

}
