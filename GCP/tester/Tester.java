import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.security.*;
import javax.swing.*;
import javax.imageio.*;

public class Tester {

    /********************************************************************/

    JFrame jf;
    Visualizer v;
    InputStream is;
    OutputStream os;
    BufferedReader br;

    static Process proc;
    static String fileName, exec;
    static boolean vis;

    final int MAXN = 200, MINN = 20;
    int VIS_SIZE = 1000;
    int N,M;
    int Score;
    int a[],b[],col[];
    int posX[],posY[];

    /********************************************************************/

    public class Visualizer extends JPanel implements WindowListener {
        
        public void paint(Graphics g) {
            try {
                BufferedImage bi = new BufferedImage(VIS_SIZE + 100, VIS_SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = (Graphics2D)bi.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD3D3D3));
                g2.fillRect(0, 0, VIS_SIZE + 100, VIS_SIZE);
                g2.setColor(new Color(0xFFFFFF));
                g2.fillRect(10, 10, VIS_SIZE - 20, VIS_SIZE - 20);
                for (int i = 0; i < M; i++) {
                    g2.setColor(new Color(0x000000));
                    g2.drawLine(posX[a[i]], posY[a[i]], posX[b[i]], posY[b[i]]);
                }
                for (int i = 0; i < N; i++) {
                    Color c = Color.getHSBColor((float)i / (float)N, 1.0f, 0.95f);
                    g2.setColor(c);
                    g2.fillOval(posX[i] - 6, posY[i] - 6, 12, 12);
                    g2.setColor(new Color(0x000000));
                    g2.drawOval(posX[i] - 6, posY[i] - 6, 12, 12);
                }
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                char[] chs1 = ("Score").toCharArray();
                char[] chs2 = ("" + Score).toCharArray();
                char[] chn = ("N = " + N).toCharArray();
                char[] chm = ("M = " + M).toCharArray();
                g2.drawChars(chs1, 0, chs1.length, VIS_SIZE, 100);
                g2.drawChars(chs2, 0, chs2.length, VIS_SIZE, 120);
                g2.drawChars(chn, 0, chn.length, VIS_SIZE, 160);
                g2.drawChars(chm, 0, chm.length, VIS_SIZE, 180);
                g.drawImage(bi, 0, 0, VIS_SIZE + 100, VIS_SIZE, null);
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }

        public Visualizer () {
            jf.addWindowListener(this);
        }

        public void windowClosing(WindowEvent e) {
            if (proc != null) {
                try { 
                    proc.destroy();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.exit(0);
        }

        public void windowActivated(WindowEvent e) { }
        public void windowDeactivated(WindowEvent e) { }
        public void windowOpened(WindowEvent e) { }
        public void windowClosed(WindowEvent e) { }
        public void windowIconified(WindowEvent e) { }
        public void windowDeiconified(WindowEvent e) { }


        public void mousePressed(MouseEvent e) {
        /*// Treat "plain" button
            int x = e.getX() - SZ * W - 10, y = e.getY() - 10;
            if (x >= 0 && x <= 100 && y >= 40 && y <= 70) {
                plain = !plain;
                repaint();
                return;
            }

            // for manual play
            if (!manual || manualReady) return;

            // "ready" button submits current state of the board
            if (x >= 0 && x <= 100 && y >= 0 && y <= 30) {
                manualReady = true;
                repaint();
                return;
            }

            int row = e.getY()/SZ, col = e.getX()/SZ;
            // convert to args only clicks with valid coordinates
            if (row < 0 || row >= H || col < 0 || col >= W)
                return;

            // ignore clicks on pawns
            if (board[row][col] == 'P')
                return;

            // a click toggles the state of the knight
            if (board[row][col] == '.') {
                board[row][col] = 'K';
                K++;
            }
            else {
                board[row][col] = '.';
                K--;
            }
            repaint();
            */

            /*
            if (save button) {
                    ImageIO.write(bi, "png", new File(fileName +".png"));
                }
                */

        }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }

    }

    /********************************************************************/

    public void generate (String seedStr) {
        try {
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            long seed = Long.parseLong(seedStr);
            rnd.setSeed(seed);
            N = rnd.nextInt(MAXN - MINN + 1) + MINN;
            M = rnd.nextInt(N * (N - 1) / 10 - 20) + 20;
            a = new int[M];
            b = new int[M];
            boolean used[][] = new boolean[N][N];
            int esum = 0;
            while (esum < M) {
                int at = rnd.nextInt(N);
                int bt = rnd.nextInt(N);
                if (at == bt || used[at][bt]) continue;
                used[at][bt] = true;
                used[bt][at] = true;
                a[esum] = at;
                b[esum] = bt;
                esum++;
            }
            posX = new int[N];
            posY = new int[N];
            for (int i = 0; i < N; i++) {
                double theta = 2.0 * Math.PI * (double)i / (double)N;
                double xt = Math.cos(theta) * (double)VIS_SIZE * 0.45 + (double)VIS_SIZE / 2;
                double yt = Math.sin(theta) * (double)VIS_SIZE * 0.45 + (double)VIS_SIZE / 2;
                posX[i] = (int)xt;
                posY[i] = (int)yt;
            }
        } catch (Exception e) {
            System.err.println("An exception occurred while generating test case.");
            e.printStackTrace();
        }
    }

    public double runTest (String seed) {

        try {
            generate(seed);
            if (proc != null) try {
                col = getColor();
            } catch (Exception e) {
                System.err.println("Failed to get result from output.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        if (vis) {
            jf.setSize(VIS_SIZE + 100, VIS_SIZE);
            jf.setVisible(true);
        }

        Score = 100000;

        return (double)Score;
    }

    private int [] getColor () throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(N).append(' ');
        sb.append(M).append('\n');
        for (int i = 0; i < M; ++i) {
            sb.append(a[i]).append(' ');
            sb.append(b[i]).append('\n');
        }
        os.write(sb.toString().getBytes());
        os.flush();

        int [] ret = new int[N];
        for (int i = 0; i < N; ++i) {
            ret[i] = Integer.parseInt(br.readLine());
        }
        return ret;
    }

    public Tester (String seed) {
        if (vis) {
            jf = new JFrame();
            v = new Visualizer();
            jf.getContentPane().add(v);
        }
        if (exec != null) {
            try {
                Runtime rt = Runtime.getRuntime();
                proc = rt.exec(exec);
                os = proc.getOutputStream();
                is = proc.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Score = " + runTest(seed));
        if (proc != null) {
            try { 
                proc.destroy(); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        }
    }

    public static void main (String[] args) {
        String seed = "1";
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-seed")) {
                seed = args[++i];
            } else if (args[i].equals("-exec")) {
                exec = args[++i];
            } else if (args[i].equals("-vis")) {
                vis = true;
            }
        }
        fileName = seed;
        Tester test = new Tester(seed);
    }

}
