import java.io.File;

public class fd {
    public final String name;
    public final File f1, f2;
    private final long f1size, f2size;
    public fd(String name, File f1, File f2) {
        this.name = name;
        this.f1 = f1;
        this.f2 = f2;
        this.f1size = f1 != null ? f1.length() : 0;
        this.f2size = f2 != null ? f2.length() : 0;
    }
    public boolean matches(boolean ok, boolean left, boolean right, boolean conflict) {
        if (f1 != null && f2 != null) {
            return f1size == f2size ? ok : conflict;
        } else if (f1 != null) {
            return left;
        } else if (f2 != null) {
            return right;
        } else {
            throw new RuntimeException();
        }
    }
    public long size() {
        return Math.max(f1size, f2size);
    }
    public String difference() {
        if (f1 != null && f2 != null) {
            return f1size == f2size ? "ok" : "conflict " + (f1size-f2size);
        } else if (f1 != null) {
            return "left only";
        } else if (f2 != null) {
            return "right only";
        } else {
            throw new RuntimeException();
        }
    }
}
