import java.awt.*;

public class gbc extends GridBagConstraints {
    /** x/y=Rel, gw/gh = 1, wx/wy = 0, a = C, f = N */
    public gbc() { }
    /** insets all sides */
    public gbc i(int x) {
        insets = new Insets(x,x,x,x);
        return this;
    }
    /** insets trailing x only */
    public gbc ix(int x) {
        insets = new Insets(0,0,0,x);
        return this;
    }
    /** insets trailing y only */
    public gbc iy(int x) {
        insets = new Insets(0,0,x,0);
        return this;
    }
    /** insets trailing x, y only */
    public gbc iboth(int x) {
        insets = new Insets(0,0,x,x);
        return this;
    }
    /** position (-1=rel) */
    public gbc p(int x, int y) {
        gridx = x;
        gridy = y;
        return this;
    }
    /** position relative */
    public gbc prel() {
        gridx = RELATIVE;
        gridy = RELATIVE;
        return this;
    }
    /** size (-1=rel, 0=rem, 1..n) */
    public gbc s(int x, int y) {
        gridwidth = x;
        gridheight = y;
        return this;
    }
    /** size remainder, 1 */
    public gbc sx() {
        return s(REMAINDER,1);
    }
    /** size 1, remainder */
    public gbc sy() {
        return s(1,REMAINDER);
    }
    /** size remainder, remainder */
    public gbc sboth() {
        return s(REMAINDER,REMAINDER);
    }
    /** weight */
    public gbc w(double x, double y) {
        weightx = x;
        weighty = y;
        return this;
    }
    /** weight 1, 0 */
    public gbc wx() {
        return w(1,0);
    }
    /** weight 0, 1 */
    public gbc wy() {
        return w(0,1);
    }
    /** weight 1, 1 */
    public gbc wboth() {
        return w(1,1);
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
