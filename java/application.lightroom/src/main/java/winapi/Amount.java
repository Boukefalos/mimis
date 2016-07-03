package winapi;

public enum Amount {
    DECREASE_MUCH ("--"),
    DECREASE_LITTLE ("-"),
    INCREASE_LITTLE ("+"),
    INCREASE_MUCH ("++");
    
    public String value;

    Amount(String value) {
        this.value = value;
    }
}
