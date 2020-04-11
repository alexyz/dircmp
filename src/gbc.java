import java.awt.*;

public class gbc extends GridBagConstraints {
    /** x/y=Rel, gw/gh = 1, wx/wy = 0, a = C, f = N */
    public gbc() { }
    /** position (-1=rel) */
    public gbc p(int x, int y) {
        insets = new Insets(5,5,5,5);
        gridx = x;
        gridy = y;
        return this;
    }
    /** size (-1=rel, 0=rem, 1..n) */
    public gbc size(int x, int y) {
        gridwidth = x;
        gridheight = y;
        return this;
    }
    /** size remainder, 1 */
    public gbc sx() {
        return size(REMAINDER,1);
    }
    /** size 1, remainder */
    public gbc sy() {
        return size(1,REMAINDER);
    }
    /** size remainder, remainder */
    public gbc sboth() {
        return size(REMAINDER,REMAINDER);
    }
    /** weight */
    public gbc weight(double x, double y) {
        weightx = x;
        weighty = y;
        return this;
    }
    /** weight 1, 0 */
    public gbc wx() {
        return weight(1,0);
    }
    /** weight 0, 1 */
    public gbc wy() {
        return weight(0,1);
    }
    /** weight 1, 1 */
    public gbc wboth() {
        return weight(1,1);
    }
    /** anchor */
    public gbc anchor(int v) {
        anchor = v;
        return this;
    }
    /** fill horizontal */
    public gbc fx() {
        fill = HORIZONTAL;
        return this;
    }
    /** fill vertical */
    public gbc fy() {
        fill = VERTICAL;
        return this;
    }
    /** fill both */
    public gbc fboth() {
        fill = BOTH;
        return this;
    }
}
