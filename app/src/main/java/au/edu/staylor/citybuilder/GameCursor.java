package au.edu.staylor.citybuilder;

import android.database.Cursor;
import android.database.CursorWrapper;
import au.edu.staylor.citybuilder.Schema.GameSchema.GameTable;

public class GameCursor extends CursorWrapper {
    public GameCursor(Cursor cursor) {
        super(cursor);
    }

    public int getMoney() {
        int id = getInt(getColumnIndex(GameTable.Cols.MONEY));
        return id;
    }

    public int getTime() {
        int time = getInt(getColumnIndex(GameTable.Cols.TIME));
        return time;
    }

    public int getPopulation() {
        int pop = getInt(getColumnIndex(GameTable.Cols.POPULATION));
        return pop;
    }

    public int getResidentCount() {
        int count = getInt(getColumnIndex(GameTable.Cols.RESIDENT_COUNT));
        return count;
    }

    public int getCommercialCount() {
        int count = getInt(getColumnIndex(GameTable.Cols.COMMERCIAL_COUNT));
        return count;
    }

    public int getEmploymentRate() {
        int rate = getInt(getColumnIndex(GameTable.Cols.EMPLOYMENT_RATE));
        return rate;
    }
}