package au.edu.staylor.citybuilder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapActivity extends AppCompatActivity {
    public static final int REQUEST_DETAILS = 1;
    private static final String EXTRA_RESET_FLAG = "au.edu.staylor.resetFlag";

    private GameData gameData;
    private SelectorFragment sFrag;
    private MapFragment mFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        gameData = GameData.get(this);
        if(intent.getBooleanExtra(EXTRA_RESET_FLAG, false)) {
            gameData.createNewGame();
        }
        setUpFragments();
    }

    public static Intent getIntent(Context c, boolean resetGame) {
        Intent intent = new Intent(c, MapActivity.class);
        intent.putExtra(EXTRA_RESET_FLAG, resetGame);
        return intent;
    }

    protected void setUpFragments() {
        FragmentManager fm = getSupportFragmentManager();
        sFrag = (SelectorFragment) fm.findFragmentById(R.id.selector);
        mFrag = (MapFragment) fm.findFragmentById(R.id.map);
        UserDetailsFragment userFrag = (UserDetailsFragment) fm.findFragmentById(R.id.userDetails);

        if ((sFrag == null) || (mFrag == null) || (userFrag == null)) {
            FragmentTransaction ft = fm.beginTransaction();
            if (sFrag == null) {
                sFrag = new SelectorFragment();
                ft.add(R.id.selector, sFrag);
            }
            if (mFrag == null) {
                mFrag = new MapFragment();
                ft.add(R.id.map, mFrag);
            }
            if (userFrag == null) {
                userFrag = new UserDetailsFragment();
                ft.add(R.id.userDetails, userFrag);
            }
            ft.commit();
        }

        mFrag.setSFrag(sFrag);
        mFrag.setUserFrag(userFrag);
    }

    @Override
    protected void onStop() {
        gameData.saveGame();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_DETAILS) {
            sFrag.resetSelected();

            // Update element name in case it has changed
            // When the user saves their details for a specific map element
            int row = DetailsActivity.getRow(returnData);
            int col = DetailsActivity.getCol(returnData);
            MapElement element = gameData.get(row, col);

            String type = DetailsActivity.getType(returnData);
            element.setName(DetailsActivity.getElementName(returnData));

            element.setImage(DetailsActivity.getBitMap(returnData));
            mFrag.updateAdapter();
        }
    }

    public static Intent getIntent(Context c){
        return new Intent(c, MapActivity.class);
    }
}
