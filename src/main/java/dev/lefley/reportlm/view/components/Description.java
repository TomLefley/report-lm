package dev.lefley.reportlm.view.components;

import javax.swing.JTextArea;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import java.awt.Graphics;
import java.awt.Point;

public class Description extends JTextArea
{
    public Description(String text)
    {
        super(text);

        setLineWrap(true);
        setWrapStyleWord(true);

        setOpaque(false);
        setFocusable(false);
        setEditable(false);
        setCaret(new NullCaret());
    }

    private static class NullCaret implements Caret
    {
        @Override
        public void install(JTextComponent c)
        {
        }

        @Override
        public void deinstall(JTextComponent c)
        {
        }

        @Override
        public void paint(Graphics g)
        {
        }

        @Override
        public void addChangeListener(ChangeListener l)
        {
        }

        @Override
        public void removeChangeListener(ChangeListener l)
        {
        }

        @Override
        public boolean isVisible()
        {
            return false;
        }

        @Override
        public void setVisible(boolean v)
        {
        }

        @Override
        public boolean isSelectionVisible()
        {
            return false;
        }

        @Override
        public void setSelectionVisible(boolean v)
        {
        }

        @Override
        public void setMagicCaretPosition(Point p)
        {
        }

        @Override
        public Point getMagicCaretPosition()
        {
            return new Point(0, 0);
        }

        @Override
        public void setBlinkRate(int rate)
        {
        }

        @Override
        public int getBlinkRate()
        {
            return 0;
        }

        @Override
        public int getDot()
        {
            return 0;
        }

        @Override
        public int getMark()
        {
            return 0;
        }

        @Override
        public void setDot(int dot)
        {
        }

        @Override
        public void moveDot(int dot)
        {
        }
    }
}
