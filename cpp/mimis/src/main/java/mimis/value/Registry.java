package mimis.value;

public enum Registry {
    CLASSES_ROOT       (0x80000000),
    CURRENT_USER       (0x80000001),
    LOCAL_MACHINE      (0x80000002),
    USERS              (0x80000003),
    PERFORMANCE_DATA   (0x80000004),
    CURRENT_CONFIG     (0x80000005),
    DYN_DATA           (0x80000006);

    protected int code;

    private Registry(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
