package au.edu.staylor.citybuilder;

public enum Type {
    RESIDENTIAL(0),
    COMMERCIAL(1),
    ROAD(2),
    NATURE(3),
    DELETE(4),
    DETAILS(5),
    RESET(6);

    private final int value;
    Type(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    public static Type fromInteger(int x) {
        Type type = null;
        switch (x) {
            case 0:
                type = RESIDENTIAL;
                break;
            case 1:
                type = COMMERCIAL;
                break;
            case 2:
                type = ROAD;
                break;
            case 3:
                type = NATURE;
                break;
            case 4:
                type = DELETE;
                break;
            case 5:
                type = DETAILS;
                break;
            case 6:
                type = RESET;
                break;
        }
        return type;
    }
    public static String TypeToString(Type x) {
        String str = "";
        switch (x) {
            case RESIDENTIAL:
                str = "Residential";
                break;
            case COMMERCIAL:
                str = "Commercial";
                break;
            case ROAD:
                str = "Road";
                break;
            case NATURE:
                str = "Nature";
                break;
            case DELETE:
                str = "Delete";
                break;
            case DETAILS:
                str = "Details";
                break;
            case RESET:
                str = "Reset";
                break;
        }
        return str;
    }
}
