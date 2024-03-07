package com.example.a2048_sergi_batle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameSenku extends AppCompatActivity {

    int[][] board = new int[9][9];
    int[][] lastMove = new int[9][9];
    ArrayList<int[][]> moves = new ArrayList<>();
    TextView pieceSelected = null;
    TextView positionSelected = null;
    private TextView undoButton;
    private TextView timeView;
    private CountDownTimer timer;

    GridLayout gridLayout;
    int mode, undo_tickets, time;
    TextView undotv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);
        gridLayout = findViewById(R.id.gridLayoutSenku);
        timeView = findViewById(R.id.timeView);
        undoButton = findViewById(R.id.undo);
        undotv = findViewById(R.id.undoTextview);

        makeUndoButtonInvisible();


        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);

        int extraData = intent.getIntExtra("mode", 0);

        Log.d("mode", String.valueOf(extraData));
        this.mode = extraData;
        this.time = sharedPreferences.getInt("timer", 0);
        this.undo_tickets = sharedPreferences.getInt("undo", 0);
        Log.d("undo_tikets", String.valueOf(this.undo_tickets));
        undotv.setText(String.valueOf(undo_tickets));

        createBaseBoard();
        startCountdownTimer();

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoLastMove();
            }
        });

        findViewById(R.id.newgame_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(view);
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToTitle();
            }
        });
    }

    private void undoLastMove() {
        if (undo_tickets > 0 && moves.size() != 0) {
            int[][] lastMove = moves.remove(moves.size() - 1);


            for (int row = 0; row < 9; row++) {
                System.arraycopy(lastMove[row], 0, board[row], 0, 9);
            }
            redrawBoard();
            undo_tickets--;
            undotv.setText(String.valueOf(undo_tickets));
            if (undo_tickets == 0) {
                makeUndoButtonInvisible();
            }
        }
    }


    private void redrawBoard() {
        // Eliminar todas las vistas del GridLayout
        gridLayout.removeAllViews();
        Position position;

        // Volver a agregar las vistas de las piezas basadas en el estado actual del tablero
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 2) {
                    TextView textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
                    position = new Position(row, column, "piece");
                    textView.setTag(position);
                }

                if (board[row][column] == 1) {
                    TextView textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.void_senku_piece);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    addClickListenerToVoid2(textView);
                    gridLayout.addView(textView);
                    position = new Position(row, column, "void");
                    textView.setTag(position);
                }
            }
        }
    }

    private void addClickListenerToVoid2(View voidPiece) {
        voidPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected != null) {
                    positionSelected = (TextView) view;

                    if (checkIfPieceCanMove()) {
                        pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                        //animacion
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Path path = new Path();
                            path.moveTo(pieceSelected.getX(), pieceSelected.getY());
                            path.lineTo(view.getX(), view.getY());

                            // Crear y ejecutar la animación
                            ObjectAnimator animator = ObjectAnimator.ofFloat(pieceSelected, View.X, View.Y, path);
                            animator.setDuration(200);
                            animator.start();
                        }
                        Position piecePosition = (Position) pieceSelected.getTag();
                        piecePosition.setRow(((Position) positionSelected.getTag()).getRow());
                        piecePosition.setColumn(((Position) positionSelected.getTag()).getColumn());
                        pieceSelected = null;
                        if (checkWin()) {
                            showWinDialog();
                        } else if (checkGameOver()) {
                            showGameOverDialog();
                        }
                        makeUndoButtonVisible();
                    } else {
                        System.out.println("No se puede mover");
                    }
                }
                redrawBoard();
            }
        });
    }


    private boolean checkIfPieceCanMove() {
        boolean canMove = false;

        Position piecePosition = (Position) pieceSelected.getTag();
        Position movePosition = (Position) positionSelected.getTag();

        if (piecePosition.getRow() == movePosition.getRow()) {
            if (piecePosition.getColumn() - movePosition.getColumn() == 2) {
                if (board[piecePosition.getRow()][piecePosition.getColumn() - 1] == 2) {
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow(), piecePosition.getColumn() - 1, "piece"));
                    canMove = true;
                }
            } else if (movePosition.getColumn() - piecePosition.getColumn() == 2) {
                if (board[piecePosition.getRow()][piecePosition.getColumn() + 1] == 2) {
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow(), piecePosition.getColumn() + 1, "piece"));
                    canMove = true;
                }
            }
        } else if (piecePosition.getColumn() == movePosition.getColumn()) {
            if (piecePosition.getRow() - movePosition.getRow() == 2) {
                if (board[piecePosition.getRow() - 1][piecePosition.getColumn()] == 2) {
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow() - 1, piecePosition.getColumn(), "piece"));
                    canMove = true;
                }
            } else if (movePosition.getRow() - piecePosition.getRow() == 2) {
                if (board[piecePosition.getRow() + 1][piecePosition.getColumn()] == 2) {
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow() + 1, piecePosition.getColumn(), "piece"));
                    canMove = true;
                }
            }
        }

        if (canMove) {
            board[piecePosition.getRow()][piecePosition.getColumn()] = 1;
            board[movePosition.getRow()][movePosition.getColumn()] = 2;
        }

        return canMove;
    }


    private void createPieces(int x, int y) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if (board[row][column] == 1 && (row != x || column != y)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
                    board[row][column] = 2;
                    Position position = new Position(row, column, "piece");
                    textView.setTag(position);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void backToTitle() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startCountdownTimer() {
        timer = new CountDownTimer(time * 1000, 1000) { // 60 segundos, actualizando cada segundo
            public void onTick(long millisUntilFinished) {
                timeView.setText("" + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                showGameOverDialog();
            }
        }.start();
    }

    private void saveLastMove() {
        int[][] currentMove = new int[9][9];
        // Copiar el estado actual del tablero al array currentMove
        for (int row = 0; row < 9; row++) {
            System.arraycopy(board[row], 0, currentMove[row], 0, 9);
        }
        // Guardar el estado actual en moves
        moves.add(currentMove);
    }

    private void printLastMoveArray() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                sb.append(lastMove[row][column]).append(" ");
            }
            sb.append("\n");
        }
        Log.d("LastMoveArray", sb.toString());
    }


    public void startNewGame(View view) {
        makeUndoButtonInvisible();
        resetBoard();
        restartTimer();
    }

    private void resetBoard() {
        gridLayout.removeAllViews();
        for (int row = 0; row < board.length; row++) {
            Arrays.fill(board[row], 0);
        }
        createBaseBoard();
    }

    private void restartTimer() {
        if (timer != null) {
            timer.cancel();
        }
        startCountdownTimer();
    }

    private boolean checkWin() {
        System.out.println("Valor del array: " + Arrays.deepToString(board));

        int pieces = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 2) {
                    pieces++;
                }
            }
        }
        return pieces == 1;
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations");
        builder.setMessage("You won the game");
        builder.show();
    }

    private void createBaseBoard() {
        switch (mode) {
            case 1:
                board1();
                createPieces2();
                break;

            case 2:
                board2();
                createPieces(0,2);
                break;

            case 3:
                board3();
                createPieces(5, 3);
                break;

            case 4:
                board4();
                createPieces(4, 4);
                break;
        }
    }


    private void board1() {
        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 8; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 3 || row > 5) && (column < 3 || column > 5)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    Position position = new Position(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }


    private void board2() {
        int[][] corners = { {1, 1}, {1, 5}, {5, 1}, {5, 5} };
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 2 || row > 4) && (column < 2 || column > 4)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    Position position = new Position(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }

                // Verificar si las coordenadas están en las esquinas
                boolean isCorner = false;
                for (int i = 0; i < corners.length; i++) {
                    if (row == corners[i][0] && column == corners[i][1]) {
                        isCorner = true;
                        break;
                    }
                }

                if (isCorner) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }



    private void board4() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 3 || row > 5) && (column < 3 || column > 5)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    Position position = new Position(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }



    private void board3() {
        for (int row = 1; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 4 || row > 6) && (column < 2 || column > 4)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    Position position = new Position(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }

    private void createPieces2() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if (board[row][column] == 1 && (row < 3 || column != 1 ) && (row < 3 || column != 7) && (row > 3 && row < 5 || column != 3 && column != 6 && column != 5 && column != 2) && (row > 1 || column != 4 ) && (row < 7 || column != 4 )) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
                    board[row][column] = 2;
                    Position position = new Position(row, column, "piece");
                    textView.setTag(position);
                }
            }
        }
    }


    private void createTableGame() {
        createBaseBoard();
        System.out.println("Valor del array: " + Arrays.deepToString(board));
    }

    private void addClickListenerToPiece(View piece) {
        piece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected == view) {
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                    pieceSelected = null;
                    return;
                }

                if (pieceSelected != null) {
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                }

                pieceSelected = (TextView) view;
                pieceSelected.setBackgroundResource(R.drawable.piece_senku_selected);
            }
        });
    }

    private void addClickListenerToVoid(View voidPiece) {
        voidPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected != null) {
                    positionSelected = (TextView) view;

                    if (checkIfPieceCanMove()) {
                        pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                        //animacion
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Path path = new Path();
                            path.moveTo(pieceSelected.getX(), pieceSelected.getY());
                            path.lineTo(view.getX(), view.getY());

                            // Crear y ejecutar la animación
                            ObjectAnimator animator = ObjectAnimator.ofFloat(pieceSelected, View.X, View.Y, path);
                            animator.setDuration(200);
                            animator.start();
                        }
                        Position piecePosition = (Position) pieceSelected.getTag();
                        piecePosition.setRow(((Position) positionSelected.getTag()).getRow());
                        piecePosition.setColumn(((Position) positionSelected.getTag()).getColumn());
                        pieceSelected = null;
                        if (checkWin()) {
                            showWinDialog();
                        } else if (checkGameOver()) {
                            showGameOverDialog();
                        }
                        makeUndoButtonVisible();
                    } else {
                        System.out.println("No se puede mover");
                    }
                }
            }
        });
    }



    public void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("You lost the game");
        builder.show();
    }


    private void deletePiece(Position position) {
        board[position.getRow()][position.getColumn()] = 1;

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            TextView piece = (TextView) gridLayout.getChildAt(i);
            Position piecePosition = (Position) piece.getTag();
            if (piecePosition.getRow() == position.getRow() && piecePosition.getColumn() == position.getColumn() && piecePosition.getType().equals("piece")) {
                gridLayout.removeView(piece);
                break;
            }
        }
    }

    private boolean checkGameOver() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 2) { // Si hay una pieza en la posición actual
                    // Verificar si hay algún movimiento posible para esta pieza
                    if (checkPossibleMoves(new Position(row, column, "piece"))) {
                        return false; // Hay al menos un movimiento posible, el juego no ha terminado
                    }
                }
            }
        }
        return true; // No hay movimientos posibles para ninguna pieza, el juego ha terminado
    }

    private boolean checkPossibleMoves(Position position) {
        // Verificar si hay un movimiento posible hacia arriba
        if (position.getRow() >= 2 && board[position.getRow() - 1][position.getColumn()] == 2 && board[position.getRow() - 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia abajo
        if (position.getRow() <= 4 && board[position.getRow() + 1][position.getColumn()] == 2 && board[position.getRow() + 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia la izquierda
        if (position.getColumn() >= 2 && board[position.getRow()][position.getColumn() - 1] == 2 && board[position.getRow()][position.getColumn() - 2] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia la derecha
        if (position.getColumn() <= 4 && board[position.getRow()][position.getColumn() + 1] == 2 && board[position.getRow()][position.getColumn() + 2] == 1) {
            return true;
        }
        return false;
    }

    private void makeUndoButtonVisible() {
        undoButton.setVisibility(View.VISIBLE);
    }

    private void makeUndoButtonInvisible() {
        undoButton.setVisibility(View.INVISIBLE);
    }

}