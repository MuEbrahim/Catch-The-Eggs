package project;

import java.util.*;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import static project.Project.animator;
import static project.Project.speed;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class projectEventListener extends AnimListener implements MouseListener {

    AudioStream audios;
    InputStream music;

    int highscore = 0;
    boolean HowToPlayPage = false;
    boolean HomePage = true;
    boolean PlayPage = false;
    int animationIndex = 0;
    TextRenderer renderer = new TextRenderer(new Font("SanasSerif", Font.BOLD, 36));
    TextRenderer renderer1 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 60));
    TextRenderer renderer2 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 25));
    int maxWidth = 100;
    int maxHeight = 100;
    int yy = 70;
    int yy2 = 70;
    int yy3 = 70;
    int speed = 1;
    int lastspeed = 0;

    boolean playing = true;
    int x = maxWidth / 2, y = maxHeight / 2;
    int xsab = x;
    int arrX[] = {15, 50, 80};
    int arrY[] = {70, 70, 70};
    int rand1 = (int) (Math.random() * 3);
    int rand2 = (int) (Math.random() * 3);
    int rand3 = (int) (Math.random() * 3);
    int sec = 0;
    boolean s1, s2, s3;
    String textureNames[] = {"egg.png",
        "hin.png",
        "sabat.png",
        "bunny2.png",
        "bunny3.png",
        "Arrow.png",
        "start.jpg",
        "instructions.jpg",
        "exit.jpg",
        "easy.jpg",
        "normal.jpg",
        "hard.jpg",
        "back2.png",
        "background.jpg"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    int score;
    public static int lives = 5;
    double arrowY[] = {0, .45, .9};
    int ArrowChecker = 0;
    boolean LevelsPage = false;
    boolean PausePage = false;
    boolean gameover = false;
    boolean GameOverPage = false;
    boolean RChecked = false;

    @Override
    public void init(GLAutoDrawable gld) {

        try {
            music = new FileInputStream(new File("mr_clown.wav"));
            audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        DrawHomePage(gl);
        DrawLevelsPage(gl);
        DrawHowToPlayPage(gl, gld);
        DrawPlayPage(gl, gld);
        DrawPausePage(gl);
    }

    void DrawLevelsPage(GL gl) {
        if (LevelsPage) {
            DrawBackground1(gl);
            DrawArrow(gl);
            DrawPausePage(gl);
            DrawFirstButton(gl, 9);
            DrawSecondButton(gl, 10);
            DrawThirdButton(gl, 11);
        }
    }

    void DrawPausePage(GL gl) {
        if (PausePage) {
            DrawBackground(gl);
            DrawArrow(gl);
            DrawFirstButton(gl, 3);
            DrawSecondButton(gl, 3);
            DrawThirdButton(gl, 3);
        }

    }

    void DrawPlayPage(GL gl, GLAutoDrawable gld) {
        if (PlayPage) {
            DrawBackground(gl);
            DrawChicken1(gl, 15, 80);
            DrawChicken2(gl, 50, 80);
            DrawChicken3(gl, 80, 80);
            Drawsabat(gl);
            renderer.beginRendering(gld.getWidth(), gld.getHeight());
            renderer.setColor(Color.BLACK);
            renderer.draw("score : " + score, 45, 45);
            renderer.draw("lives : " + lives, 500, 45);
            renderer.setColor(Color.WHITE);
            renderer.endRendering();
            if (s1) {
                DrawEgg(gl, arrX[rand1] - 2, yy -= speed);
            }
            if (s2) {
                DrawEgg(gl, arrX[rand2] - 2, yy2 -= speed);
            }
            if (s3) {
                DrawEgg(gl, arrX[rand3] - 2, yy3 -= speed);
            }

            TochedSabt();
            Timer();
            TochedGround();
            GameOver(gld);
        }
    }

    void DrawHowToPlayPage(GL gl, GLAutoDrawable gld) {
        if (HowToPlayPage) {
            DrawBackground1(gl);
            renderer2.beginRendering(gld.getWidth(), gld.getHeight());
            renderer2.setColor(Color.BLACK);
            renderer2.draw("Welcome to Chicken Eggs Game", 100, 500);
            renderer2.draw("the objective in this game is to collect as many eggs as possible", 20, 450);
            renderer2.draw(" you die if you missed 5 eggs the instructions as follows:", 20, 400);
            renderer2.draw("press the arrow buttons left and right to navigate the nest ", 20, 350);
            renderer2.draw(" the p button to toggle pauseing and resuming ", 20, 300);
            renderer2.draw(" press esc button to go back to the prevoius page ", 20, 250);
            renderer2.setColor(Color.WHITE);
            renderer2.endRendering();
        }
    }

    void DrawHomePage(GL gl) {
        if (HomePage) {
            DrawBackground1(gl);
            DrawFirstButton(gl, 6);
            DrawSecondButton(gl, 7);
            DrawThirdButton(gl, 8);
            DrawArrow(gl);
        }
    }

    void DrawArrow(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9 + .2, y / (maxHeight / 2.0) - 0.9 - arrowY[ArrowChecker] + .2, 0);
        gl.glScaled(0.1, 0.1, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    void TochedSabt() {
        if (xsab - 3 < arrX[rand1] && arrX[rand1] < xsab + 15 && yy < 15 + 10 && yy > 15 - 10) {
            yy = 70;
            rand1 = (int) (Math.random() * 3);
            score++;
        }
        if (xsab - 3 < arrX[rand2] && arrX[rand2] < xsab + 15 && yy2 < 15 + 10 && yy2 > 15 - 10) {
            yy2 = 70;
            rand2 = (int) (Math.random() * 3);
            score++;
        }
        if (xsab - 3 < arrX[rand3] && arrX[rand3] < xsab + 15 && yy3 < 15 + 10 && yy3 > 15 - 10) {
            yy3 = 70;
            rand3 = (int) (Math.random() * 3);
            score++;
        }
    }

    void Timer() {
        if (sec == 0) {
            s1 = true;
        }
        if (sec == 20 * 30) {
            s2 = true;
        }
        if (sec == 20 * 50) {
            s3 = true;
        }
    }

    void TochedGround() {
        if (yy <= -3) {
            yy = 70;
            lives--;
            rand1 = (int) (Math.random() * 3);
        }
        if (yy2 <= -3) {
            yy2 = 70;
            lives--;
            rand2 = (int) (Math.random() * 3);
        }
        if (yy3 <= -3) {
            yy3 = 70;
            lives--;
            rand3 = (int) (Math.random() * 3);
        }
        sec += 15;
    }

    void GameOver(GLAutoDrawable gld) {

        if (lives <= 0) {
            if (score > highscore) {
                highscore = score;
            }
            renderer1.setColor(Color.BLACK);
            renderer1.beginRendering(gld.getWidth(), gld.getHeight());
            renderer1.draw("High Score: " + highscore, 250, 500);
            renderer1.draw("game over", 250, 350);
            renderer1.draw("cilck R to play again", 150, 200);
            renderer1.endRendering();
            renderer1.setColor(Color.WHITE);

            speed = 0;
            gameover = true;
            lives = 0;
        }
    }

    void DrawFirstButton(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9 - .1, y / (maxHeight / 2.0) - 0.9 + .2, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    void DrawThirdButton(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9 - .1, y / (maxHeight / 2.0) - 0.9 - .9 + .2, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    void DrawSecondButton(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9 - .1, y / (maxHeight / 2.0) - 0.9 - .45 + .2, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 1]);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground1(GL gl) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 2]);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawChicken1(GL gl, int x, int y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawChicken2(GL gl, int x, int y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawChicken3(GL gl, int x, int y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * 2, 0.1 * 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void Drawsabat(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(xsab / (maxWidth / 2.0) - 0.9, 15 / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * 16, 0.1 * 16, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawEgg(GL gl, int x, int y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * .3, 0.1 * .5, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void reset() {
        score = 0;
        s1 = false;
        s2 = false;
        s3 = false;
        yy = 70;
        yy2 = 70;
        yy3 = 70;
        lives = 5;
        sec = -30;
        speed = lastspeed;
        if (gameover) {
            gameover = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 0 && HomePage == true) {
            ke.setKeyChar('s');
        }
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 1 && HomePage == true) {
            ke.setKeyChar('i');
        }
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 2 && HomePage == true) {
            ke.setKeyChar('q');
        }
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 0 && LevelsPage == true) {
            ke.setKeyChar('e');
        }
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 1 && LevelsPage == true) {
            ke.setKeyChar('m');
        }
        if (ke.getKeyChar() == KeyEvent.VK_ENTER && ArrowChecker == 2 && LevelsPage == true) {
            ke.setKeyChar('h');
        }
        if (ke.getKeyChar() == 'r') {
            speed = lastspeed;
            RChecked = true;
            this.reset();
        }
        if (ke.getKeyChar() == 'p' && HomePage == false && HowToPlayPage == false && PlayPage == true && LevelsPage == false) {
            if (playing == true) {
                speed = 0;
                playing = false;
            } else if (playing == false) {
                speed = lastspeed;
                playing = true;
            }
        }
        if (ke.getKeyChar() == 's' && this.ArrowChecker == 0 && (HomePage == true || HowToPlayPage == true)) {
            HomePage = false;
            PlayPage = false;
            HowToPlayPage = false;
            LevelsPage = true;
        }
        if (ke.getKeyChar() == 'i' && HowToPlayPage == false && HomePage == true && PlayPage == false) {
            HowToPlayPage = true;
            HomePage = false;
        } else if (ke.getKeyChar() == 'i' && HomePage == false && HowToPlayPage == true && PlayPage == false) { //das I wa howa fe el HowtoPlay
            this.HowToPlayPage = false;
            this.HomePage = true;
        }
        if (ke.getKeyChar() == 'h' && HomePage == false && PlayPage == HowToPlayPage && false == false && LevelsPage == true) {
            HomePage = false;
            PlayPage = true;
            HowToPlayPage = false;
            LevelsPage = false;
            speed = 4;
            lastspeed = speed;
        }
        if (ke.getKeyChar() == 'm' && HomePage == false && PlayPage == HowToPlayPage && false == false && LevelsPage == true) {
            HomePage = false;
            PlayPage = true;
            HowToPlayPage = false;
            LevelsPage = false;
            speed = 2;
            lastspeed = speed;
        }
        if (ke.getKeyChar() == 'e' && HomePage == false && PlayPage == HowToPlayPage && false == false && LevelsPage == true) {
            HomePage = false;
            PlayPage = true;
            HowToPlayPage = false;
            LevelsPage = false;
            speed = 1;
            lastspeed = speed;
        }
        if (ke.getKeyChar() == 'q') {
            System.exit(0);
        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (PausePage == true) {
            } else if (this.gameover == false) {
                xsab += 5;
                if (xsab >= 90) {
                    xsab = 90;
                }
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            if (PausePage == true) {
            } else if (this.gameover == false) {
                xsab -= 5;
                if (xsab <= 0) {
                    xsab = 0;
                }
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            if (ArrowChecker < 2) {
                ArrowChecker++;
                if (ArrowChecker == 2) {
                    ArrowChecker = 2;
                }
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (this.HomePage == false && this.HowToPlayPage == true) {
                this.HomePage = true;
                this.HowToPlayPage = false;
            }
            if (this.HomePage == false && this.LevelsPage == true) {
                this.HomePage = true;
                this.LevelsPage = false;
            }
            if ((this.HomePage == false && this.PlayPage == true) || (this.HomePage == false && this.PausePage == true)) {
                //this.HomePage = true;
                this.PlayPage = false;
                this.PausePage = false;
                this.LevelsPage = true;
                this.reset();
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) {

            if (ArrowChecker > 0) {
                ArrowChecker--;
            }
            if (ArrowChecker == 0) {
                ArrowChecker = 0;
            }
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
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

}
