package mimis.value;

public enum Windows {
    WM_CLOSE           (0x0010),
    WM_COMMAND         (0x0111),
    WM_SYSCOMMAND      (0x0112),
    WM_APPCOMMAND      (0x0319),
    WM_USER            (0x0400),
    MAPVK_VK_TO_VSC    (0);

    protected int code;

    private Windows(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
