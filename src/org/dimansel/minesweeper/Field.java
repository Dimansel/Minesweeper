package org.dimansel.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Field extends JPanel {
    public static final int LMB = 1;
    public static final int RMB = 3;
    private double cw; //width of a cell
    private double ch; //height of a cell
    private int r; //number of rows
    private int c; //number of columns
    private int m; //number of mines
    private final int sx = 1+2; //start coordinates
    private final int sy = 1+2;
    public boolean isGameOver = false;
    private int[][] data; //array of numbers
    private boolean[][] opened; //array of states of cells
    private boolean[][] flags; //array of flags of cells
    private int numOpened = 0; //number of opened cells except mines

    public Field(int rows, int columns, int ww, int wh, int mines) {
        r = rows;
        c = columns;
        cw = (double)ww/c;
        ch = (double)wh/r;
        m = mines;
        data = new int[r][c];
        opened = new boolean[r][c];
        flags = new boolean[r][c];
    }

    public void newGame() {
        data = new int[r][c];
        opened = new boolean[r][c];
        flags = new boolean[r][c];
        isGameOver = false;
        numOpened = 0;
        generate();
    }

    private void gameOver(boolean win) {
        repaint();
        if (win) {
            isGameOver = true;
            JOptionPane.showMessageDialog(getParent(), "You win!");
            return;
        }

        isGameOver = true;
        JOptionPane.showMessageDialog(getParent(), "You lose");
        for (int a=0; a<r; a++) {
            for (int b=0; b<c; b++) {
                opened[a][b] = true;
                flags[a][b] = false;
            }
        }
    }

    public void generate() {
        ArrayList<Point> cells = new ArrayList<>();
        data = new int[r][c];
        opened = new boolean[r][c];

        for (int a=0; a<r; a++)
            for (int b=0; b<c; b++)
                cells.add(new Point(a, b));

        for (int a=0; a<m; a++) {
            int index = Window.rnd.nextInt(cells.size());
            Point c = cells.get(index);
            data[c.x][c.y] = -1;
            cells.remove(index);
        }

        for (int a=0; a<r; a++) {
            for (int b=0; b<c; b++) {
                if (data[a][b] != -1)
                    data[a][b] = getMinesAroundCell(a, b);
            }
        }
    }

    private int getMinesAroundCell(int a, int b) {
        int count = 0;
        for (int j=-1; j<=1; j++) {
            for (int i=-1; i<=1; i++) {
                if (a+j<0 || a+j>=r || b+i<0 || b+i>=c) continue;
                count += data[a+j][b+i]==-1 ? 1 : 0;
            }
        }

        return count;
    }

    public void onClick(int x, int y, int button) {
        double preC = x/cw;
        double preR = y/ch;
        if (preC < 0 || preC >= c || preR < 0 || preR >= r) return;

        int col = (int)preC;
        int row = (int)preR;

        if (button == LMB) {
            openCell(col, row);
            if (numOpened == r*c-m)
                gameOver(true);
        }
        else if (button == RMB && !opened[row][col])
            flags[row][col] = !flags[row][col];
    }

    private void openCell(int col, int row) {
        if (flags[row][col]) return;
        if (opened[row][col]) return;

        opened[row][col] = true;
        if (data[row][col] == -1) {
            gameOver(false);
            return;
        }
        else if (data[row][col] != 0) {
            numOpened++;
            return;
        }

        numOpened++;
        for (int j=-1; j<=1; j++) {
            for (int i=-1; i<=1; i++) {
                if (row+j<0 || row+j>=r || col+i<0 || col+i>=c) continue;

                if (data[row+j][col+i] >= 0)
                    openCell(col + i, row + j);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int a=0; a<r; a++) {
            for (int b=0; b<c; b++) {
                if (!opened[a][b]) {
                    g.setColor(Color.BLUE);
                    g.fillRect((int) (b * cw + sx), (int) (a * ch + sy), (int) cw - 1, (int) ch - 1);
                }
                else {
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (b * cw + sx), (int) (a * ch + sy), (int) cw - 1, (int) ch - 1);
                    if (data[a][b] > 0) {
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(data[a][b]), (int) (cw * (b + 0.5) + sx), (int) (ch * (a + 0.5) + sy));
                    }
                    else if (data[a][b] == -1) {
                        g.setColor(Color.RED);
                        g.fillOval((int) (cw * (b + 0.25) + sx), (int) (ch * (a + 0.25) + sy), (int) (cw * 0.5) - 1, (int) (ch * 0.5) - 1);
                    }
                }

                if (flags[a][b]) {
                    g.setColor(Color.BLACK);
                    g.fillOval((int) (cw*(b+0.25) + sx), (int) (ch*(a+0.25) + sy), (int) (cw*0.5) - 1, (int) (ch*0.5) - 1);
                }
            }
        }
    }

    public void updateSize(int ww, int wh) {
        cw = (double)ww/c;
        ch = (double)wh/r;
    }
}
