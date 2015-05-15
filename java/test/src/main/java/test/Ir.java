package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mimis.device.lirc.LircButton;
import mimis.device.lirc.LircButtonListener;
import mimis.device.lirc.LircService;
import mimis.device.lirc.remote.PhiliphsRCLE011Button;
import mimis.device.wiimote.WiimoteDevice;
import mimis.device.wiimote.WiimoteService;
import mimis.exception.device.DeviceNotFoundException;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.IREvent;

public class Ir extends WiimoteDevice implements LircButtonListener {
    protected Graph graph;
    protected Point tl, tr, br, bl, raw, point;
    protected Line t, r, b, l;

    public Ir() {
        tl = tr = br = bl = new Point(0, 0);
        graph = new Graph();
        /*try {
            while (true) {
                graph.draw(new Point(Math.random(), Math.random()));
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        LircService lircService = new LircService();
        lircService.put(PhiliphsRCLE011Button.NAME,
                PhiliphsRCLE011Button.values());
        lircService.add(this);
        lircService.start();
        WiimoteService wiimoteService = new WiimoteService();
        try {
            Wiimote wiimote = wiimoteService.getDevice(this);
            wiimote.activateIRTRacking();
            Thread.sleep(10000000);
        } catch (DeviceNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(LircButton lircButton) {
        if (lircButton instanceof PhiliphsRCLE011Button) {
            boolean t = false, r = false, b = false, l = false;
            switch ((PhiliphsRCLE011Button) lircButton) {
                case RED:
                    tl = raw;
                    l = bl != null;
                    t = tr != null;
                    break;
                case GREEN:
                    tr = raw;
                    t = tl != null;
                    r = br != null;
                    break;
                case YELLOW:
                    br = raw;
                    r = tr != null;
                    b = bl != null;
                    break;
                case BLUE:
                    bl = raw;
                    b = br != null;
                    l = tl != null;
                    break;
            }
            if (t) {
                this.t = new Line(tl, tr);
            }
            if (r) {
                this.r = new Line(tr, br);
            }
            if (b) {
                this.b = new Line(br, bl);
            }
            if (l) {
                this.l = new Line(bl, tl);
            }
        }
    }

    public void onIrEvent(IREvent event) {
        // log.debug(event);
        raw = new Point(event.getAx(), event.getAy());
        logger.debug(String.format("%d %d", raw.x, raw.y));
        if (t != null && r != null && b != null && l != null) {
            double w = r.getX(raw.y) - l.getX(raw.y);
            double h = t.getY(raw.x) - b.getY(raw.x);
            point = new Point((w - l.getX(raw.y)) / w, (h - b.getY(raw.x)) / h);
            graph.draw(point);
        }
    }

    public static void main(String[] args) {
        new Ir();
    }

    protected class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    protected class Line {
        protected double a, b;

        public Line(Point p1, Point p2) {
            a = (p2.y - p1.y) / (p2.x - p1.x);
            b = p2.y - a * p2.x;
        }

        public double getY(double x) {
            return a * x + b;
        }

        public double getX(double y) {
            return (y - b) / a;
        }
    }

    public class Graph extends JPanel {
        protected static final long serialVersionUID = 1L;

        final int PAD = 20;
        final double X = 1;
        final double Y = 2;
        JFrame f;
        Graphics2D g2;
        double w, h;

        public Graph() {
            f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(this);
            f.setSize(400, 400);
            f.setLocation(200, 200);
            f.setVisible(true);
            g2 = (Graphics2D) f.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            w = getWidth();
            h = getHeight();
            axes();
        }

        protected void draw(Point point) {
            g2.clearRect(0, 0, 400, 400);
            axes();
            double xInc = (double) (w - 2 * PAD) / X;
            double scale = (double) (h - 2 * PAD) / Y;
            // Mark data points.
            g2.setPaint(Color.blue);
            double x = PAD + point.x * xInc;
            double y = h - PAD - scale * point.y;
            g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
        }
    
        protected void axes() {
            // Draw ordinate.
            g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
            // Draw abcissa.
            g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
        }
    }
}
