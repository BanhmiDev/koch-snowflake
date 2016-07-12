import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KochSnowflakeGUI extends JFrame implements MouseListener, MouseWheelListener, MouseMotionListener {

    private ArrayList<Double> xs = new ArrayList<Double>();
    private ArrayList<Double> ys = new ArrayList<Double>();
    private int iterations = 0;
    private JPanel canvas;
    private JScrollPane canvasPane;
    private int mouseStartX, mouseStartY;
    private double canvasScale = 1;
    private DrawingUnit drawingUnit = new DrawingUnit() {
        public void drawLine(double xStart, double yStart, double xEnd, double yEnd) {
            xs.add(xStart);
            ys.add(yStart);
            xs.add(xEnd);
            ys.add(yEnd);
        }
    };

    private KochSnowflakeGUI(String[] args) {
        super("Koch Snowflake");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        KochSnowflake.draw(drawingUnit, 0, 0, 15, 25.980762114, iterations);
        KochSnowflake.draw(drawingUnit, 15, 25.980762114, 30, 0, iterations);
        KochSnowflake.draw(drawingUnit, 30, 0, 0, 0, iterations);

        canvas = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // Get minimum and maximum values
                double minX = 0, minY = 0, maxX = 0, maxY = 0;
                for (double x : xs) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                }
                for (double y : ys) {
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }

                // Calculate delta values and scale
                double deltaX = maxX - minX, deltaY = maxY - minY;
                double scale = 0;
                if ((getWidth() / deltaX) < (getHeight() / deltaY)) {
                    scale = getWidth() / deltaX;
                } else {
                    scale = getHeight() / deltaY;
                }

                // Set up polygon
                Polygon p = new Polygon();
                for (int i = 0; i < xs.size(); i++) {
                    int x = (int) ((xs.get(i) - minX) * scale);
                    int y = getHeight() - (int) ((ys.get(i) - minY) * scale) - 2;
                    p.addPoint(getWidth()/2 + x - ((int)(maxX*scale))/2, y); // Center drawing
                }
                g.fillPolygon(p);
                g.setColor(Color.BLACK);
                g.drawPolygon(p);

                if (g instanceof Graphics2D) {
                    ((Graphics2D) g).scale(canvasScale, canvasScale);
                }
            }
        };
        
        canvas.setPreferredSize(new Dimension(600, 600));
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);

        JPanel listPane = new JPanel();
        canvasPane = new JScrollPane(canvas);
        JPanel pane = new JPanel(new GridBagLayout());

        JButton button;
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(canvasPane, c);

        c.gridwidth = 1;
        button = new JButton("Reset Zoom");
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvasScale = 1;
                Dimension newSize = new Dimension((int)(600 * canvasScale), (int)(600 * canvasScale));
                canvas.setSize(newSize);
                canvas.setPreferredSize(newSize);
            }
        });

        button = new JButton("Increment");
        c.gridx = 1;
        c.gridy = 1;
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iterations++;
                updateCanvas();

            }
        });

        button = new JButton("Decrement");
        c.gridx = 2;
        c.gridy = 1;
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (iterations > 0) {
                    iterations--;
                    updateCanvas();
                }
            }
        });

        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        JLabel jLabel = new JLabel("Use mousewheel to zoom, drag via mouse to move around.", SwingConstants.CENTER);
        jLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pane.add(jLabel, c);

        getContentPane().add(pane);

        pack();
        setVisible(true);
    }

    public void updateCanvas() {
        xs.clear();
        ys.clear();
        KochSnowflake.draw(drawingUnit, 0, 0, 15, 25.980762114, iterations);
        KochSnowflake.draw(drawingUnit, 15, 25.980762114, 30, 0, iterations);
        KochSnowflake.draw(drawingUnit, 30, 0, 0, 0, iterations);
        canvas.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseStartX = e.getX();
        mouseStartY = e.getY();
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
    public void mouseWheelMoved(MouseWheelEvent e) {
        canvasScale -= e.getPreciseWheelRotation() / 5;
        if (canvasScale < 1) {
            canvasScale = 1;
        }
        Dimension newSize = new Dimension((int)(600 * canvasScale), (int)(600 * canvasScale));
        canvas.setSize(newSize);
        canvas.setPreferredSize(newSize);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Rectangle view = canvasPane.getViewport().getViewRect();
        view.x += mouseStartX - e.getX();
        view.y += mouseStartY - e.getY();
        canvas.scrollRectToVisible(view);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public static void main(String[] args) {
        new KochSnowflakeGUI(args);
    }
}
