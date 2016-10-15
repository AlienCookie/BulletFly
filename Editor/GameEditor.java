package Editor;

import GUI.Demo;
import GUI.InputManager;
import GUI.NuklearGUI;
import GameObjects.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import openGL.*;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by andri on 02-Aug-16.
 */
public class GameEditor {

    private static final float PI = 3.141529f;
    private Renderer gameRenderer;
    private NuklearGUI nuklearGUI;
    private Demo demo;
    private int[] WIDTH = new int[1];
    private int[] HEIGHT = new int[1];
    private Cannon cannon;
    private Floor floor;
    private Camera camera = new Camera();
    private Aim aim;
    private SkyBox box;

    public GameEditor(long win) {
        GLFW.glfwGetWindowSize(win, WIDTH, HEIGHT);
        gameRenderer = new Renderer(0, 150, WIDTH[0], HEIGHT[0]);
        nuklearGUI = new NuklearGUI(win);
        try {
            Map<String, GLObject.GLObjectBuilder> builderMap
                    = GLObjectsCreator.glObjectBuilderMap(this.getClass().getResourceAsStream("/res/cannon.obj"));
            floor = new Floor(builderMap.get("Floor"));
            cannon = new Cannon("/res/gun.obj");
            cannon.setBullet(new Bullet(builderMap.get("Bullet")));
            aim = new Aim(builderMap.get("Aim"), builderMap.get("Hit"));
            box = new SkyBox(builderMap.get("Plate"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        demo = new Demo(nuklearGUI.getCtx(), cannon);
    }

    public void draw() {

        nuklearGUI.prepareToRender();
        demo.layout(0, HEIGHT[0] - 150);
        nuklearGUI.render();

        gameRenderer.onPrepareDraw();
        // Objects render
        floor.draw(8, camera.getPosition());
        box.draw(camera.getPosition());

        cannon.draw();
        aim.drawAim();
        shot();

        gameRenderer.onPostDraw();
        rotate();

    }

    public void shot() {
        if (InputManager.onSpace) {
            cannon.shot();
            aim.setWasHit(false);
        }

        if(Math.abs(cannon.getBulletPosition()[0] - aim.getPosition()[0]) < Math.abs(cannon.getBulletSpeed()[0]) && !aim.wasHit()) {
            float distance = (float) Math.sqrt(Math.pow((cannon.getBulletPosition()[1] - aim.getPosition()[1]), 2) +
                    Math.pow((cannon.getBulletPosition()[2] - aim.getPosition()[2]), 2));
            if(distance < 9.5f)
                aim.onHit(new float[]{aim.getPosition()[0], cannon.getBulletPosition()[1], cannon.getBulletPosition()[2]});
        }
    }


    public void rotate() {

        float dx = 0.0f;
        float dy = 0.0f;
        float dz = 0.0f;

        float angleX = camera.getYaw();
        float angleY = camera.getPitch();


        float speed = 0.25f;
        //player movement
        if (InputManager.onShift)
            speed *= 5f;

        if (InputManager.onW) {
            dx = (float) -Math.sin(angleX / 180 * PI) * speed;
            dy = (float) Math.tan(angleY / 180 * PI) * speed;
            dz = (float) -Math.cos(angleX / 180 * PI) * speed;
        }

        if (InputManager.onS) {
            dx = (float) Math.sin(angleX / 180 * PI) * speed;
            dy = (float) -Math.tan(angleY / 180 * PI) * speed;
            dz = (float) Math.cos(angleX / 180 * PI) * speed;
        }
        if (InputManager.onD) {
            dx = (float) -Math.sin((angleX + 90) / 180 * PI) * speed;
            dz = (float) -Math.cos((angleX + 90) / 180 * PI) * speed;
        }
        if (InputManager.onA) {
            dx = (float) -Math.sin((angleX - 90) / 180 * PI) * speed;
            dz = (float) -Math.cos((angleX - 90) / 180 * PI) * speed;
        }
        if (InputManager.onUp) {
            //cannon.addPitch(-0.7f);
            cannon.move(0.6f);
        }
        if (InputManager.onDown) {
            //cannon.addPitch(-0.7f);
            cannon.move(-0.6f);
        }

        if (InputManager.onRight) {
            cannon.rotate(new float[]{0.f, 1.f, 0.f});
        }
        if (InputManager.onLeft) {
            cannon.rotate(new float[]{0.f, -1.f, 0.f});
        }

        if (InputManager.onNum2) {
            cannon.addPitch(0.7f);
        }
        if (InputManager.onNum8) {
            cannon.addPitch(-0.7f);
        }

        if (InputManager.onAdd) {
            cannon.addPower(0.01f);
        }
        if (InputManager.onSubtract) {
            cannon.addPower(-0.01f);
        }

        //apply move
        camera.getPosition()[0] -= dx;
        camera.getPosition()[1] -= dy;
        camera.getPosition()[2] += dz;

        if (InputManager.onClick && InputManager.onMove) {
            if (InputManager.position[1] < 750) {
                camera.setPitch(camera.getPitch() + InputManager.shift[1] / 2f);
                camera.setYaw(camera.getYaw() + InputManager.shift[0] / 2f);
                if (camera.getPitch() < -70.0) camera.setPitch(-70.0f);
                if (camera.getPitch() > 70.0) camera.setPitch(70.0f);
                InputManager.shift[0] = 0;
                InputManager.shift[1] = 0;
            }
        }
        camera.updateProjectMatrix();
    }


    public void cleanUp() {
        nuklearGUI.nk_glfw3_shutdown();
        // gameObject.forEach(GLObject::cleanUp);
    }

    public void inputManaging() {
        nuklearGUI.inputManaging();
    }

    public void prepareInput() {
        nuklearGUI.prepareInput();
    }
}
