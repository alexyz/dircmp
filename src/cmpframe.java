import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private final JTextField leftTextField = new JTextField();
    private final JTextField rightTextField = new JTextField();
    private final JTable table = new fdjtable();
    private final JButton compareButton = new JButton("Compare");
    private final JCheckBox okCheckBox = new JCheckBox("OK");
    private final JCheckBox leftCheckBox = new JCheckBox("Left");
    private final JCheckBox rightCheckBox = new JCheckBox("Right");
    private final JCheckBox conflictCheckBox = new JCheckBox("Conflict");
    private final JLabel summaryLabel = new JLabel("summary");

    public cmpframe() {
        setTitle("Dir Compare");

        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);

        createmodel(Collections.emptyList());

        compareButton.addActionListener(e -> cmp());
        okCheckBox.addItemListener(e -> updatemodel());
        leftCheckBox.addItemListener(e -> updatemodel());
        leftCheckBox.setSelected(true);
        rightCheckBox.addItemListener(e -> updatemodel());
        rightCheckBox.setSelected(true);
        conflictCheckBox.addItemListener(e -> updatemodel());
        conflictCheckBox.setSelected(true);

        JScrollPane sp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel p2 = new JPanel();
        p2.add(compareButton);
        p2.add(okCheckBox);
        p2.add(leftCheckBox);
        p2.add(rightCheckBox);
        p2.add(conflictCheckBox);

        JPanel p1 = new JPanel(new GridBagLayout());
        p1.setBorder(new EmptyBorder(5,5,5,5));

        p1.add(new JLabel("Left"), new gbc().iboth(5).p(1, 1));
        p1.add(leftTextField, new gbc().iboth(5).p(2, 1).wx().fx());
        p1.add(fbutton(leftTextField), new gbc().iboth(5).p(3, 1));

        p1.add(new JLabel("Right"), new gbc().iboth(5).p(1, 2));
        p1.add(rightTextField, new gbc().iboth(5).p(2, 2).wx().fx());
        p1.add(fbutton(rightTextField), new gbc().iboth(5).p(3, 2));

        p1.add(p2, new gbc().iboth(5).p(1, 3).sx().wx());

        p1.add(sp, new gbc().iboth(5).p(1, 4).sx().wboth().fboth());

        p1.add(summaryLabel, new gbc().ix(5).p(1,5).sx().wx().fx());

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

    private void createmodel(Collection<fd> values) {
        table.setModel(new fdtm(values));
        table.getColumnModel().getColumn(0).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
    }

    private void updatemodel() {
        TableModel m = table.getModel();
        if (m instanceof fdtm) {
            long v = ((fdtm) m).update(okCheckBox.isSelected(), leftCheckBox.isSelected(), rightCheckBox.isSelected(), conflictCheckBox.isSelected());
            summaryLabel.setText("size: " + sizeStr(v));
        }
    }

    public static String sizeStr (long v) {
        double g = 1_000_000_000, m = 1_000_000, k = 1_000;
        if (v >= g) {
            return String.format("%.1fG", v / g);
        } else if (v >= m) {
            return String.format("%.1fM", v / m);
        } else if (v >= k) {
            return String.format("%.1fK", v / k);
        } else {
            return String.valueOf(v);
        }
    }

    private void savePref() {
        Preferences p = Preferences.userNodeForPackage(getClass());
        p.put("dir1", leftTextField.getText());
        p.put("dir2", rightTextField.getText());
        try {
            p.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private void loadPref() {
        Preferences p = Preferences.userNodeForPackage(getClass());
        leftTextField.setText(p.get("dir1", ""));
        rightTextField.setText(p.get("dir2", ""));
    }

    private void cmp() {
        File dir1 = new File(leftTextField.getText());
        File dir2 = new File(rightTextField.getText());
        if (dir1.isDirectory() && dir2.isDirectory()) {
            Map<String, File> m1 = find(dir1.getAbsolutePath(), dir1, new TreeMap<>());
            Map<String, File> m2 = find(dir2.getAbsolutePath(), dir2, new TreeMap<>());
            Map<String, fd> dest = new TreeMap<>();
            addto(dest, m1, m2, false);
            addto(dest, m2, m1, true);
            createmodel(dest.values());
            updatemodel();
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
