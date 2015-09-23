package org.dimansel.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Window extends JFrame implements ComponentListener, MouseListener, KeyListener {
    public static Random rnd = new Random();
    private Field field;

    public Window() {
        //Configurate window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - 500) / 2, (d.height - 500) / 2);
        addComponentListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setTitle("Minesweeper");
        setVisible(true);

        //Initializing game field
        field = new Field(8, 8, 500-16, 500-39, 10);
        add(field);
        field.newGame();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (field == null) return;
        field.updateSize(getWidth() - 16, getHeight() - 39);
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (field.isGameOver) return;

        Point p = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(p, field);
        field.onClick(p.x, p.y, e.getButton());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_N) {
            field.newGame();
            repaint();
        }
    }

    public static void main(String[] args) {
        new Window();
    }
}
