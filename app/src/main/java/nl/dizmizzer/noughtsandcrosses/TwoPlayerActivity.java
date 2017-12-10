package nl.dizmizzer.noughtsandcrosses;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class TwoPlayerActivity extends Activity {

    private int currentPlayer = 0;
    private String prefName = "NACdiz";
    private int winC = 0;
    private int tiesC = 0;
    private int lossesC = 0;
    private int[][] matrix = {{0,0,0},{0,0,0},{0,0,0}};
    private boolean hasWon = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twoplayer);

        TextView wins = findViewById(R.id.player1);
        TextView ties = findViewById(R.id.ties);
        TextView losses = findViewById(R.id.player2);

        winC = getSharedPreferences(prefName, 0).getInt("playerone", 0);
        tiesC = getSharedPreferences(prefName, 0).getInt("tiestwoplayer", 0);
        lossesC = getSharedPreferences(prefName, 0).getInt("playertwo", 0);

        wins.setText(String.valueOf(winC));
        ties.setText(String.valueOf(tiesC));
        losses.setText(String.valueOf(lossesC));

    }

    public void restartGame() {

    }
    public void onWin(int currentPlayer) {

        TextView tv = findViewById(R.id.win_message);
        switch (currentPlayer) {
            case -1:
                tv.setText(getString(R.string.tied));
                getSharedPreferences(prefName, 0).edit().putInt("tiestwoplayer", getSharedPreferences(prefName, 0).getInt("tiestwoplayer", 0) + 1).commit();
                TextView ties = findViewById(R.id.ties);

                tiesC = getSharedPreferences(prefName, 0).getInt("tiestwoplayer", 0);
                ties.setText(String.valueOf(tiesC));

                break;
            case 0:
                tv.setText(getString(R.string.has_won).replace("%player%", "Player 1"));
                getSharedPreferences(prefName, 0).edit().putInt("playerone", getSharedPreferences(prefName, 0).getInt("playerone", 0) + 1).commit();
                TextView wins = findViewById(R.id.player1);

                winC = getSharedPreferences(prefName, 0).getInt("playerone", 0);

                wins.setText(String.valueOf(winC));

                break;
            case 1:
                tv.setText(getString(R.string.has_won).replace("%player%", "Player 2"));
                getSharedPreferences(prefName, 0).edit().putInt("playertwo", getSharedPreferences(prefName, 0).getInt("playertwo", 0) + 1).commit();

                TextView losses = findViewById(R.id.player2);

                lossesC = getSharedPreferences(prefName, 0).getInt("playertwo", 0);

                losses.setText(String.valueOf(lossesC));

                break;
        }
        View view = findViewById(R.id.overlay);
        view.setVisibility(View.VISIBLE);
        hasWon = true;
    }

    public void onClick(View v) {
        if (String.valueOf(v.getTag()).equalsIgnoreCase(String.valueOf(R.drawable.circle)) ||
                String.valueOf(v.getTag()).equalsIgnoreCase(String.valueOf(R.drawable.cross))) return;
        if (hasWon) return;

        int[] getMatrixSpot = getMatrix(v);
        switch (currentPlayer) {
            case 0:
                ((ImageButton)v).setImageResource(R.drawable.circle);
                v.setTag(R.drawable.circle);
                matrix[getMatrixSpot[0]][getMatrixSpot[1]] = 1;
                if (CheckWin(currentPlayer)) {
                    onWin(currentPlayer);
                    return;
                }
                if (CheckTie()) onWin(-1);
                currentPlayer = 1;
                break;

            case 1:
                ((ImageButton)v).setImageResource(R.drawable.cross);
                v.setTag(R.drawable.cross);
                matrix[getMatrixSpot[0]][getMatrixSpot[1]] = -1;
                if (CheckWin(currentPlayer)) {
                    onWin(currentPlayer);
                    return;
                }

                if (CheckTie()) onWin(-1);
                currentPlayer = 0;

                break;
        }

    }

    private boolean CheckTie() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (matrix[i][j] == 0) return false;
            }

        return true;
    }

    private int[] getMatrix(View v) {
        if (v.getId() == R.id.imageButton)
            return new int[]{0,0};

        if (v.getId() == R.id.imageButton2)
            return new int[]{0,1};
        if (v.getId() == R.id.imageButton3)
            return new int[]{0,2};
        if (v.getId() == R.id.imageButton4)
            return new int[]{1,0};
        if (v.getId() == R.id.imageButton5)
            return new int[]{1,1};
        if (v.getId() == R.id.imageButton6)
            return new int[]{1,2};
        if (v.getId() == R.id.imageButton7)
            return new int[]{2,0};
        if (v.getId() == R.id.imageButton8)
            return new int[]{2,1};
        if (v.getId() == R.id.imageButton9)
            return new int[]{2,2};


        return new int[]{0,0};
    }

    private boolean CheckWin(int currentPlayer) {
        for (int i = 0; i < 3; i++) {
            //Vertical Check
            if (matrix[0][i] == matrix[1][i] && matrix[0][i] == matrix[2][i] && matrix[0][i] != 0) return true;

            //Horizontal Check
            if (matrix[i][0] == matrix[i][1] && matrix[i][0] == matrix[i][2]  && matrix[i][0] != 0) return true;
        }
        //Vertical 1
        if (matrix[0][0] == matrix[1][1] && matrix[0][0] == matrix[2][2] && matrix[0][0] != 0) return true;

        //Vertical 2
        if (matrix[0][2] == matrix[1][1] && matrix[0][2] == matrix[2][0] && matrix[0][2] != 0) return true;

        return false;
    }
}
