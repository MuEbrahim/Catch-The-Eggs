package project;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Project extends JFrame {

    public static Animator animator = null;
    public static GLCanvas glcanvas;
    public static int speed = 15;

   
   public static void main(String[] args)  {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Project.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        new Project();

    }

    public Project() {

        AnimListener listener = new projectEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        glcanvas.addMouseListener((MouseListener) listener);

        animator = new Animator(glcanvas);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(speed);
        animator.add(glcanvas);

        setTitle("Anim Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
        animator.start();

    }

}
