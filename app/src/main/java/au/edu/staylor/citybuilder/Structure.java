package au.edu.staylor.citybuilder;

public class Structure {

    private final int imageId; // drawable id
    private String label;
    private Type type;

    public Structure(int drawableId, String label, Type structureType) {
        this.imageId = drawableId;
        this.label = label;
        this.type = structureType;
    }

    public int getDrawableId() { return imageId; }
    public String getLabel() { return label; }
    public Type getType() { return type; }
    public boolean equals(Structure s) {
        if (s == null) {
            return false;
        }
        return (type == s.getType()) && (label.equals(s.getLabel())) && (imageId == s.getDrawableId());
    }
}