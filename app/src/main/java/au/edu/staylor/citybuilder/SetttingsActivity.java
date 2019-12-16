package au.edu.staylor.citybuilder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetttingsActivity extends AppCompatActivity {

    private Button saveBtn;
    private Button resetBtn;
    private GameData gameData;
    private Settings s;

    private EditText mapWidth;
    private EditText mapHeight;
    private EditText initialMoney;
    private EditText familySize;
    private EditText shopSize;
    private EditText salary;
    private EditText taxRate;
    private EditText serviceCost;
    private EditText houseBuildingCost;
    private EditText commBuildingCost;
    private EditText roadBuildingCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);

        gameData = GameData.get(this);
        s = gameData.getSettings();
        setUpSettings();
        saveBtn = findViewById(R.id.saveSettingsBtn);
        resetBtn = findViewById(R.id.resetSettingBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameData.resetSettings();
                setUpText();
            }
        });
    }

    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }

    private void setUpSettings() {
        setUpReferences();
        setUpText();
    }
    private void setUpReferences() {
        mapWidth = findViewById(R.id.mapWidth);
        mapHeight = findViewById(R.id.mapHeight);
        initialMoney = findViewById(R.id.initialMoney);
        familySize = findViewById(R.id.familySize);
        shopSize = findViewById(R.id.shopSize);
        salary = findViewById(R.id.salary);
        taxRate = findViewById(R.id.taxRate);
        serviceCost = findViewById(R.id.serviceCost);
        houseBuildingCost = findViewById(R.id.houseCost);
        commBuildingCost = findViewById(R.id.commCost);
        roadBuildingCost = findViewById(R.id.roadCost);
    }
    private void setUpText() {
        mapWidth.setText( String.valueOf(s.getMapWidth()) );
        mapHeight.setText( String.valueOf(s.getMapHeight()) );
        initialMoney.setText( String.valueOf(s.getInitialMoney()) );
        familySize.setText( String.valueOf(s.getFamilySize()) );
        shopSize.setText( String.valueOf(s.getShopSize()) );
        salary.setText( String.valueOf(s.getSalary()) );
        taxRate.setText( String.valueOf(s.getTaxRate()) );
        serviceCost.setText( String.valueOf(s.getServiceCost()) );
        houseBuildingCost.setText( String.valueOf(s.getHouseBuildingCost()) );
        commBuildingCost.setText( String.valueOf(s.getCommBuildingCost()) );
        roadBuildingCost.setText( String.valueOf(s.getRoadBuildingCost()) );
    }
    private void saveSettings() {
        s.setMapWidth( convertToInt(mapWidth) );
        s.setMapHeight( convertToInt(mapHeight) );
        s.setInitialMoney( convertToInt(initialMoney) );
        s.setFamilySize( convertToInt(familySize) );
        s.setShopSize( convertToInt(shopSize) );
        s.setSalary( convertToInt(salary) );
        s.setTaxRate( convertToDouble(taxRate) );
        s.setServiceCost( convertToInt(serviceCost) );
        s.setHouseBuildingCost( convertToInt(houseBuildingCost) );
        s.setCommBuildingCost( convertToInt(commBuildingCost) );
        s.setRoadBuildingCost( convertToInt(roadBuildingCost) );

        gameData.updateSettings();
    }

    private int convertToInt(EditText x) {
        return Integer.parseInt(x.getText().toString());
    }
    private double convertToDouble(EditText x) {
        return Double.parseDouble((x.getText().toString()));
    }
    public static Intent getIntent(Context c){
        return new Intent(c, SetttingsActivity.class);
    }
}
