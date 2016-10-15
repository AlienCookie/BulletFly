package GameObjects;

import Editor.GLEditorObject;
import GUI.InputManager;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import openGL.GLObject;
import openGL.GLObjectsCreator;
import openGL.GLTextureLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by andri on 13-Sep-16.
 */
public class Cannon{
    private GLEditorObject leftWheel;
    private GLEditorObject rightWheel;
    private GLEditorObject body;
    private GLEditorObject base;
    private GLEditorObject bulletHolder;
    private GLEditorObject axis;
    private Bullet bullet;
    private boolean onShot = false;
    public float pinch;
    public float power = 0.7f;
    public float yRotation;
    float[] bulletPosition;
    float[] shotPosition;

    public Cannon(String source){
        try {
            Map<String, GLObject.GLObjectBuilder> builderMap
                    = GLObjectsCreator.glObjectBuilderMap(this.getClass().getResourceAsStream(source));

            int bodyTexture =  GLTextureLoader.loadTexture(getClass().getResourceAsStream("/res/metaltexture2.png"));
            int wheelsTexture =  GLTextureLoader.loadTexture(getClass().getResourceAsStream("/res/blackbrushed.png"));
            int baseTexture =  GLTextureLoader.loadTexture(getClass().getResourceAsStream("/res/metalLarge.png"));
            int bulletHolderTexture =  GLTextureLoader.loadTexture(getClass().getResourceAsStream("/res/Metal-Dark-Texture.png"));
            int axesTexture =  GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/metalSmall.png"));

            leftWheel = new GLEditorObject(builderMap.get("LeftWheel").addTexture(wheelsTexture));
            rightWheel = new GLEditorObject(builderMap.get("RightWheel").addTexture(wheelsTexture));
            body = new GLEditorObject(builderMap.get("GunBody").addTexture(bodyTexture));
            base = new GLEditorObject(builderMap.get("Base").addTexture(baseTexture));
            bulletHolder = new GLEditorObject(builderMap.get("BulletHolder").addTexture(bulletHolderTexture));
            axis = new GLEditorObject(builderMap.get("Axis").addTexture(axesTexture));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //rotate(new float[]{0.f, 90.f, 0.f});
        scale(new float[]{1.5f, 1.5f, 1.5f});
        bulletPosition = new float[] {0.f, 1.55f, -0.4f, 1.f};
        shotPosition = new float[] {0.f, 1.51f, -2.4f, 1.f};
    }

    public void draw(){

        if(onShot)
            bullet.update();
        else
            bullet.updateProjectMatrix();

        bullet.drawAsArray();

        rightWheel.updateProjectMatrix();
        rightWheel.drawAsArray();

        leftWheel.updateProjectMatrix();
        leftWheel.drawAsArray();

        body.updateProjectMatrix();
        body.drawAsArray();

        base.updateProjectMatrix();
        base.drawAsArray();

        bulletHolder.updateProjectMatrix();
        bulletHolder.drawAsArray();

        axis.updateProjectMatrix();
        axis.drawAsArray();
    }

    public void scale(float[] factor){

        rightWheel.scale(factor);
        leftWheel.scale(factor);
        base.scale(factor);
        body.scale(factor);
        bulletHolder.scale(factor);
        axis.scale(factor);
    }

    public void rotate(float[] angles){
        //float[] center = {0.055f, 1.0f, -1.494f};

        /*rightWheel.rotateAroundXYZ(angles, center);
        leftWheel.rotateAroundXYZ(angles, center);
        base.rotateAroundXYZ(angles, center);
        body.rotateAroundXYZ(angles, center);
        bulletHolder.rotateAroundXYZ(angles, center);
        axis.rotateAroundXYZ(angles, center);*/

        yRotation += angles[1];

        rightWheel.rotateXYZ(angles);
        leftWheel.rotateXYZ(angles);
        base.rotateXYZ(angles);
        body.rotateXYZ(angles);
        bulletHolder.rotateXYZ(angles);
        axis.rotateXYZ(angles);

        if(bullet != null && !onShot)
            bullet.translate(body.getRelativePointPosition(bulletPosition));

        leftWheel.rotateAroundXYZ(new float[]{angles[1], 0.f, 0.f}, new float[]{1.207f, 1.216f, -1.469f});
        rightWheel.rotateAroundXYZ(new float[]{-angles[1], 0.f, 0.f}, new float[]{1.207f, 1.216f, -1.469f});
    }

    public void addPitch(float deltaPitch) {
        pinch += deltaPitch;
        if(pinch <= -61f) {
            pinch = -61f;
            return;
        }
        if(pinch >= 0f) {
            pinch = 0f;
            return;
        }

        if(bullet != null && !onShot)
            bullet.translate(body.getRelativePointPosition(bulletPosition));

        body.rotateAroundXYZ(new float[]{deltaPitch, 0.f, 0.f}, new float[]{1.f, 1.3f, -1.5f});
    }

    public void move(float speed) {

        float pitch = (speed * 180f)/ ((float) Math.PI * 2.42f);

        float[] deltaPos = new float[3];

        deltaPos[0] = (float) Math.sin(yRotation/ 180f * Math.PI) * speed;
        deltaPos[2] = (float) -Math.cos(yRotation / 180f * Math.PI) * speed;

        rightWheel.move(deltaPos);
        leftWheel.move(deltaPos);
        base.move(deltaPos);
        body.move(deltaPos);
        bulletHolder.move(deltaPos);
        axis.move(deltaPos);

        if(bullet != null && !onShot)
            bullet.translate(body.getRelativePointPosition(bulletPosition));

        leftWheel.rotateAroundXYZ(new float[]{pitch, 0.f, 0.f}, new float[]{1.f, 1.216f, -1.469f});
        rightWheel.rotateAroundXYZ(new float[]{pitch, 0.f, 0.f}, new float[]{1.f, 1.216f, -1.469f});
    }

    public void shot() {
        float[] gunPos = body.getRelativePointPosition(shotPosition);
        bullet.translate(gunPos);

        float[] shotAcceleration = new float[3];
        shotAcceleration[1] = (float) Math.sin(-pinch / 180f * Math.PI) * power;
        float sidePower = power-shotAcceleration[1];
        shotAcceleration[0] = (float) Math.sin(yRotation/ 180f * Math.PI) * sidePower;
        shotAcceleration[2] = (float) -Math.cos(yRotation / 180f * Math.PI) * sidePower;

        bullet.setSpeed(shotAcceleration);

        onShot = true;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
        float[] gunPos = body.getRelativePointPosition(bulletPosition);
        bullet.translate(gunPos);
    }

    public float[] getBulletPosition(){return bullet.getPosition();}

    public float[] getBulletSpeed(){return bullet.getSpeed();}

    public void addPower(float dP){
        power += dP;
    }
}
