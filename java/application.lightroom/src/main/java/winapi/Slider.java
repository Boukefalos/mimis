package winapi;
public enum Slider {
    EXPOSURE,
    CONTRAST,
    HIGHLIGHTS,
    SHADOWS,
    WHITES,
    BLACKS,
    CLARITY,
    VIBRANCE;

    public String getLabel() {
        String name = this.name();
        return String.format("%s%s", name.substring(0,  1), name.toLowerCase().substring(1));
    }

    public String getLabel(Amount amount) {
        return String.format("%s %s", getLabel(), amount.value);
    }
}
