package au.edu.staylor.citybuilder;

import android.graphics.Bitmap;

import static au.edu.staylor.citybuilder.Type.TypeToString;

public class MapElement {
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;

    private Structure structure;
    private Bitmap image;
    private String name;

    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure) {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.name = structure != null ? TypeToString(structure.getType()) : "";
    }

    public boolean isBuildable() { return buildable; }
    public int getNorthWest() { return terrainNorthWest; }
    public int getSouthWest() { return terrainSouthWest; }
    public int getNorthEast() { return terrainNorthEast; }
    public int getSouthEast() { return terrainSouthEast; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void setImage(Bitmap thumbnail) { image = thumbnail; }
    public Bitmap getImage() { return image; }

    // Retrieves the structure built on this map element
    // Null if one is not present
    public Structure getStructure() { return structure; }
    public void setStructure(Structure structure) {
        this.structure = structure;
        this.name = structure != null ? TypeToString(structure.getType()) : "";
    }
}

