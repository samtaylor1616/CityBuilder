package au.edu.staylor.citybuilder;

import java.util.Arrays;
import java.util.List;

public class StructureData {
    public static final int[] DRAWABLES = {
            0, // No structure
            R.drawable.ic_building1, R.drawable.ic_building2, R.drawable.ic_building3,
            R.drawable.ic_building4, R.drawable.ic_building5, R.drawable.ic_building6,
            R.drawable.ic_building7, R.drawable.ic_building8,
            R.drawable.ic_road_ns, R.drawable.ic_road_ew, R.drawable.ic_road_nsew,
            R.drawable.ic_road_ne, R.drawable.ic_road_nw, R.drawable.ic_road_se, R.drawable.ic_road_sw,
            R.drawable.ic_road_n, R.drawable.ic_road_e, R.drawable.ic_road_s, R.drawable.ic_road_w,
            R.drawable.ic_road_nse, R.drawable.ic_road_nsw, R.drawable.ic_road_new, R.drawable.ic_road_sew,
            R.drawable.ic_tree1, R.drawable.ic_tree2, R.drawable.ic_tree3, R.drawable.ic_tree4};

    private List<Structure> structureList = Arrays.asList(new Structure(R.drawable.ic_building1, "House", Type.RESIDENTIAL),
            new Structure(R.drawable.ic_building2, "House", Type.RESIDENTIAL),
            new Structure(R.drawable.ic_building3, "House", Type.RESIDENTIAL),
            new Structure(R.drawable.ic_building4, "House", Type.RESIDENTIAL),
            new Structure(R.drawable.ic_building5, "Commercial", Type.COMMERCIAL),
            new Structure(R.drawable.ic_building6, "Commercial", Type.COMMERCIAL),
            new Structure(R.drawable.ic_building7, "Commercial", Type.COMMERCIAL),
            new Structure(R.drawable.ic_building8, "Commercial", Type.COMMERCIAL),

            new Structure(R.drawable.ic_road_ns, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_ew, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_nsew, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_ne, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_nw, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_se, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_sw, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_n, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_e, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_s, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_w, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_nse, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_nsw, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_new, "Road", Type.ROAD),
            new Structure(R.drawable.ic_road_sew, "Road", Type.ROAD),

            new Structure(R.drawable.ic_tree1, "Tree", Type.NATURE),
            new Structure(R.drawable.ic_tree2, "Tree", Type.NATURE),
            new Structure(R.drawable.ic_tree3, "Tree", Type.NATURE),
            new Structure(R.drawable.ic_tree4, "Tree", Type.NATURE),
            new Structure(R.drawable.red, "Delete", Type.DELETE),
            new Structure(R.drawable.details, "Details", Type.DETAILS),
            new Structure(R.drawable.reset, "Reset", Type.RESET));

    private static StructureData instance = null;

    public static StructureData get() {
        if (instance == null) {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public Structure get(int i) {
        return structureList.get(i);
    }

    public int size() {
        return structureList.size();
    }
}
