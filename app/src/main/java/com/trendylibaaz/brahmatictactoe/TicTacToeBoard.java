package com.trendylibaaz.brahmatictactoe;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;


/**
 * Created by pranaykx on 6/11/2016.
 */
public class TicTacToeBoard {

    public final int ROWS, COLUMNS, MIN_COMPLETED; /* MIN_COMPLETED is to check what accounts for
                                                    * minimum number of cells to declare the board
                                                    * as complete.
                                                    * */
    private STATE board[][];
    private boolean boardComplete;

    public enum STATE{
        STATE_FREE,
        STATE_ZERO,
        STATE_CROSS,
        STATE_COMPLETE /*This is only for cells that complete the game*/
    };
    public String logTag = "Brahma"; /*Change this to anything*/

    TicTacToeBoard(int ROWS, int COLUMNS, int MIN_COMPLETED)
            throws IllegalArgumentException
    {
        this.ROWS = ROWS;
        this.COLUMNS = COLUMNS;
        this.MIN_COMPLETED = MIN_COMPLETED;

        if( ROWS < 0 || COLUMNS < 0 || MIN_COMPLETED < 0)
            throw new IllegalArgumentException("Invalid row[" + ROWS+"]," +
                    " col[" + COLUMNS+"], MIN_COMPLETED[" + MIN_COMPLETED+"] triplet\n");

        board = new STATE[ROWS][];
        for (int i = 0; i < ROWS; i++) {
            board[i] = new STATE[COLUMNS];
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = STATE.STATE_FREE;
            }
        }
    }
    public STATE getCellState(int row, int col)
    {
        if(!checkSanity(row, col))
            return STATE.STATE_COMPLETE; /*Error condition but we put state in complete state.*/

        return board[row][col];
    }
    private boolean checkSanity(int row, int col)
    {
        return !(row >= ROWS || col >= COLUMNS || row < 0 || col < 0);
    }

    public boolean updateCell(int row, int col, STATE s)
    {
        if(boardComplete)
            return  false;

        if(!checkSanity(row, col))
            return false;

        board[row][col] = s;

        if(isComplete(row,col))
        {
            boardComplete = true;
        }
        return true;
    }

    public Pair[] getCompletedCells()
    {
        ArrayList<Pair <Integer , Integer> > res;
        if (!boardComplete)
            return null;

        res = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(board[i][j] == (STATE.STATE_COMPLETE)) {
                    Pair<Integer, Integer> cell = new Pair<>(i, j);
                    res.add(cell);
                }
            }
        }
        return res.toArray(new Pair[res.size()]);
    }

    private boolean checkRow(int row, int col)
    {
        STATE stateToCheck = board[row][col];
        int checkedCells = 0;

        if (!checkSanity(row, col))
            return  false;

        for (int i = 0; i < COLUMNS; i++) {

            if(board[row][i] ==(stateToCheck))
               checkedCells++;
        }

        if(checkedCells >= MIN_COMPLETED) {
            for (int i = 0; i < COLUMNS; i++) {
                if(board[row][i] == (stateToCheck))
                    board[row][i] = STATE.STATE_COMPLETE;
            }
            return true;
        }
        return false;
    }
    private boolean checkCol(int row, int col)
    {
        STATE stateToCheck = board[row][col];
        int checkedCells = 0;

        if (!checkSanity(row, col))
            return  false;

        for (int i = 0; i < ROWS; i++) {
            if(board[i][col] == (stateToCheck))
                checkedCells++;
        }

        if(checkedCells >= MIN_COMPLETED) {
            for (int i = 0; i < ROWS; i++) {
                if(board[i][col] == (stateToCheck))
                    board[i][col] = STATE.STATE_COMPLETE;
            }
            return true;
        }
        return false;
    }

    private boolean checkDiag(int row, int col)
    {
        int startRow, startCol;
        boolean hasLeftDiag = true;
        boolean hasRightDiag = true;
        STATE stateToCheck = board[row][col];
        int checkedCells = 0;

        /*
         * Left diagonal first.
         */
        startRow = 0 ;
        startCol = col - row;
        if (startCol < 0)
            hasLeftDiag = false;

        if (hasLeftDiag) {
            for (int i = startRow, j = startCol; i <ROWS && j <COLUMNS; i++, j++) {
                Log.d(logTag, "Left Diag Checking " + i + "," + j);
                if (board[i][j] == (stateToCheck))
                    checkedCells++;
            }

            if(checkedCells >= MIN_COMPLETED) {
                for (int i = startRow, j = startCol; i <ROWS && j <COLUMNS; i++, j++) {
                    if (board[i][j] == (stateToCheck))
                        board[i][j] = STATE.STATE_COMPLETE;
                }
                return true;
            }
            else
                checkedCells = 0;
        }

        //check right diag.
        startCol = col + row;

        if (startCol >=COLUMNS )
            hasRightDiag = false;

        if (hasRightDiag) {
            for (int i = startRow, j = startCol; (i < ROWS && j >= 0); i++, j--) {
                Log.d(logTag, "Right Diag Checking " + i + "," + j);
                if (board[i][j] == (stateToCheck))
                    checkedCells++;
            }
            if(checkedCells >= MIN_COMPLETED) {
                for (int i = startRow, j = startCol; (i < ROWS && j >= 0); i++, j--) {
                    if (board[i][j] == (stateToCheck))
                        board[i][j] = STATE.STATE_COMPLETE;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isComplete()
    {
        return  boardComplete;
    }

    private boolean isComplete(int row, int col)
    {
        return checkCol(row, col) || checkRow(row,col) || checkDiag(row, col);
    }

    public void resetBoard()
    {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = STATE.STATE_FREE;
            }
        }
        boardComplete = false;
    }
}
