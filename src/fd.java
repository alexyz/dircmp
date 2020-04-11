import java.io.File;

public class fd {
    public final String k;
    public final File f1, f2;
    public fd(String k, File f1, File f2) {
        this.k = k;
        this.f1 = f1;
        this.f2 = f2;
    }
    public boolean ok() {
        return f1 != null && f2 != null && f1.length()==f2.length();
    }
    public String d() {
        if (f1 != null && f2 != null) {
            long v = f1.length() - f2.length();
            return v == 0 ? "ok" : "" + v;
        } else if (f1 != null) {
            return "left only";
        } else if (f2 != null) {
            return "right only";
        } else {
            throw new RuntimeException();
        }
    }
}
