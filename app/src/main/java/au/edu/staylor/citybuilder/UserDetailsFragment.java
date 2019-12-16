package au.edu.staylor.citybuilder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetailsFragment extends Fragment {

    private static final int TIME_INCREMENT = 10;
    private GameData game;
    private TextView money;
    private TextView newMoney;
    private TextView currPop;
    private TextView employRate;
    private TextView time;
    private ImageView timeIcon;
    private ImageView sunIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_user_details, container, false);

        setReferences(view);
        setOnClicks();
        return view;
    }

    protected void updateTime() {
        game.addGameTime(TIME_INCREMENT);

        // Update money
        updateMoney();

        updateTextFields();
    }

    protected void updateMoney() {
        String currentMoney = String.valueOf(game.getMoney());
        String newMoneyStr = "$ " + game.getMostRecentIncome();

        if (newMoneyStr.contains("-")) {
            newMoney.setTextColor(Color.parseColor("#ff0000"));
        } else {
            newMoneyStr = "+" + newMoneyStr;
            newMoney.setTextColor(Color.parseColor("#00ff00"));
        }

        money.setText("Money: " + currentMoney);
        newMoney.setText(newMoneyStr);
    }

    protected void updateTextFields() {
        time.setText("Time: " + game.getGameTime());
        currPop.setText("Population: " + game.getPopulation());
        employRate.setText("Employ rate: " + game.getEmploymentRate() * 100 + "%");
    }

    protected void setReferences(View view) {
        sunIcon = view.findViewById(R.id.sunIcon);
        sunIcon.setImageResource(R.drawable.sun);

        game = GameData.get(getActivity());
        money = view.findViewById(R.id.gameMoney);
        newMoney = view.findViewById(R.id.newMoney);
        time = view.findViewById(R.id.gameTime);
        timeIcon = view.findViewById(R.id.timeImage);
        currPop = view.findViewById(R.id.currPop);
        employRate = view.findViewById(R.id.employRate);

        updateTextFields();
        money.append(" " + game.getMoney());
        timeIcon.setImageResource(R.drawable.fast_time);
    }

    protected void setOnClicks() {
        timeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });
        sunIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });
    }
}

