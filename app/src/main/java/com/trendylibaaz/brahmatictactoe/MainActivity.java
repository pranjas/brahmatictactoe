package com.trendylibaaz.brahmatictactoe;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Space;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {

    private int ROWS = 3;
    private int COLUMNS = 5;
    private int COLUMNS_BOARD = 3;
    private TicTacToeBoard gameBoard;
    private final String dbgTag = "Brahma:";
    private int MIN_COMPLETED = 3;

    private TicTacToeBoard.STATE currentState = TicTacToeBoard.STATE.STATE_CROSS;
    private boolean toggle;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.trendylibaaz.brahmatictactoe/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.trendylibaaz.brahmatictactoe/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
        */
    }

    private class Location {
        int row, col;

        public Location(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout boardLayout = (GridLayout) findViewById(R.id.mainLayout);
        if (gameBoard == null) {
            try {
                gameBoard = new TicTacToeBoard(ROWS, COLUMNS_BOARD, MIN_COMPLETED);
            } catch (IllegalArgumentException ex) {
                Log.d(dbgTag, ex.getMessage());
                throw ex;
            }
        } else
            Log.d(dbgTag, "Not creating new board!!!");
        boardLayout.setColumnCount(COLUMNS);
        boardLayout.setRowCount(ROWS);
        ActionBar actionBar = getActionBar();
        Log.d(dbgTag, "Action bar is " + actionBar);
        if (actionBar != null)
            actionBar.show();
        createGrid(ROWS, COLUMNS, boardLayout);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void resetGame() {
        if (gameBoard != null)
            gameBoard.resetBoard();
        GridLayout boardLayout = (GridLayout) findViewById(R.id.mainLayout);
        for (int i = 0; i < boardLayout.getChildCount(); i++) {
            View v = boardLayout.getChildAt(i);
            if (v.getTag() != null)
                ((ImageButton) v).setImageResource(R.mipmap.blank);
            toggle = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d(dbgTag, "Calling function onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_help:
                break;
            case R.id.menu_reset:
                resetGame();
            case R.id.menu_board_config:
                break;
        }
        return true;
    }

    private void createGrid(int rows, int cols, GridLayout gridLayout) {
        for (int i = 0; i < rows; i++)
            createRow(i, cols, gridLayout, rows);
    }

    private View createButton(int row, int col, float rowWeight, float colWeight) {
        GridLayout.LayoutParams gLayout = new GridLayout.LayoutParams();
        int btnId;
        gLayout.setMargins(1, 1, 1, 1);
        gLayout.rowSpec = GridLayout.spec(row, rowWeight);
        gLayout.columnSpec = GridLayout.spec(col, colWeight);
        gLayout.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL);

        ImageButton button = new AppCompatImageButton(getApplicationContext());

        button.setTag(new Location(row, col / 2));
        switch (gameBoard.getCellState(row, col/2)) {
            case STATE_FREE:
                btnId = R.mipmap.blank;
                break;
            case STATE_CROSS:
                btnId = R.mipmap.cross;
                break;
            case STATE_COMPLETE:
                btnId = R.mipmap.cross;
                break;
            case STATE_ZERO:
                btnId = R.mipmap.zero;
                break;
            default:
                Log.d(dbgTag, "In default");
                btnId = R.mipmap.blank;
                break;
        }

        button.setImageResource(btnId);
        button.setLayoutParams(gLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle)
                    currentState = TicTacToeBoard.STATE.STATE_CROSS;
                else
                    currentState = TicTacToeBoard.STATE.STATE_ZERO;
                toggle = !toggle;
                Log.d(dbgTag, "Current state is " + currentState);

                ImageButton btn = (ImageButton) v;

                Location loc = (Location) btn.getTag();

                if (!gameBoard.isComplete()) {
                    Log.d(dbgTag, "This is ok...");
                    Log.d(dbgTag, "Clicked pos is " + loc.row + "," + loc.col);
                    Log.d(dbgTag, "Clicked state is " + gameBoard.getCellState(loc.row, loc.col));

                    if (!(gameBoard.getCellState(loc.row, loc.col) == TicTacToeBoard.STATE.STATE_FREE))
                        return;

                    gameBoard.updateCell(loc.row, loc.col, currentState);

                    if (currentState == TicTacToeBoard.STATE.STATE_CROSS) {
                        Log.d(dbgTag, "Setting image resource as cross");
                        btn.setImageResource(R.mipmap.cross);
                    } else {
                        Log.d(dbgTag, "Setting image resource as zero.");
                        btn.setImageResource(R.mipmap.zero);
                    }

                    if (gameBoard.isComplete())
                        Toast.makeText(getApplicationContext(),
                                "Game Over", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(),
                            "Game is already over", Toast.LENGTH_LONG).show();
            }
        });

        return button;
    }

    private View createSpace(int row, int col, float rowWeight, float colWeight) {
        GridLayout.LayoutParams gLayoutParams
                = new GridLayout.LayoutParams();
        gLayoutParams.columnSpec = GridLayout.spec(col, colWeight);
        gLayoutParams.rowSpec = GridLayout.spec(row, rowWeight);
        gLayoutParams.setGravity(Gravity.CENTER);
        gLayoutParams.setMargins(2, 2, 2, 2);

        Space spacer = new Space(getApplicationContext());
        spacer.setBackgroundColor(Color.BLACK);
        spacer.setLayoutParams(gLayoutParams);
        return spacer;
    }

    /*
     * Row number to create and number of columns in that row.
     */
    private void createRow(int row, int cols, GridLayout gridLayout, int rows) {
        boolean toggle = false;
        GridLayout.LayoutParams gLayout = new GridLayout.LayoutParams();
        gLayout.rowSpec = GridLayout.spec(row, GridLayout.VERTICAL);

        for (int i = 0; i < cols; i++) {
            if (toggle) {

                gLayout.columnSpec = GridLayout.spec(i, GridLayout.VERTICAL);
                gridLayout.addView(createSpace(row, i, 1.0f / rows, 1.0f / cols));
                toggle = false;
                Log.d("Brahma:", "Adding space at " + row + "," + i);
            } else {
                gLayout.columnSpec = GridLayout.spec(i, GridLayout.VERTICAL);
                gridLayout.addView(createButton(row, i, 1.0f / rows, 1.0f / cols));
                toggle = true;
                Log.d("Brahma:", "Adding button at " + row + "," + i);
            }
        }
    }
}
