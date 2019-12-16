package au.edu.staylor.citybuilder;

import android.database.Cursor;
import android.database.CursorWrapper;
import au.edu.staylor.citybuilder.Schema.MapElementSchema.MapTable;


public class ElementCursor extends CursorWrapper {
    public ElementCursor(Cursor cursor) {
        super(cursor);
    }

    public MapElement getMapElement() {
        Structure structure = getStructure();

        boolean buildable = getInt(getColumnIndex(MapTable.Cols.BUILDABLE)) == 1;
        int northWest = getInt(getColumnIndex(MapTable.Cols.NORTH_WEST));
        int northEast = getInt(getColumnIndex(MapTable.Cols.NORTH_EAST));
        int southWest = getInt(getColumnIndex(MapTable.Cols.SOUTH_WEST));
        int southEast = getInt(getColumnIndex(MapTable.Cols.SOUTH_EAST));

        MapElement element = new MapElement(buildable, northWest, northEast, southWest, southEast, structure);
        if (structure != null) {
            element.setName(getString(getColumnIndex(MapTable.Cols.STRUCTURE_NAME)));
        }

        return element;
    }

    public Structure getStructure() {
        Structure structure = null;
        String label = getString(getColumnIndex(MapTable.Cols.STRUCTURE_LABEL));
        if ( !label.equals(DbHelper.NO_STRUCTURE) ) {
            // If there is a structure
            int drawableId = getInt(getColumnIndex(MapTable.Cols.STRUCTURE_DRAWABLE));
            Type type = Type.fromInteger(getInt(getColumnIndex(MapTable.Cols.STRUCTURE_TYPE)));
            structure =  new Structure(drawableId, label, type);
        }
        return structure;
    }
}
