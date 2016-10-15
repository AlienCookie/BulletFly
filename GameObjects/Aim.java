package GameObjects;

import Editor.GLEditorObject;
import openGL.FileManager;
import openGL.GLObjectsCreator;
import openGL.GLProgram;
import openGL.GLTextureLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by andri on 14-Sep-16.
 */
public class Aim extends GLEditorObject {

    GLEditorObject shot;

    private Vector<Float> hitsPositions = new Vector<>();
    private boolean wasHit = false;

    public Aim(GLObjectBuilder builder, GLObjectBuilder shotBuilder) {
        super(builder);

        int[] bulletTexture = {-1};
        try {
            bulletTexture[0] = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/aim.png"));

            shotBuilder = shotBuilder
                    .addTexture(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/shot.png")));
            shot = new GLEditorObject(shotBuilder);
            shot.setTextured(true);
            shot.scale(new float[]{1.8f, 1.8f, 1.8f});

        } catch (IOException e) {
            e.printStackTrace();
        }


        this.setTexture(bulletTexture);
        this.setTextured(true);

        this.move(new float[]{40.f, 25.f, 0.f});
        this.scale(new float[]{10.f, 10.f, 10.f});
    }

    public void onHit(float[] shotPos){
        hitsPositions.add(shotPos[0]);
        hitsPositions.add(shotPos[1]);
        hitsPositions.add(shotPos[2]);
        wasHit = true;
    }

    public void drawAim(){
        for(int i = 0; i < hitsPositions.size()/3; i++) {
            float[] hitCoor = {hitsPositions.get(i*3)+0.01f, hitsPositions.get(i*3+1), hitsPositions.get(i*3+2)};
            shot.translate(hitCoor);
            shot.updateProjectMatrix();
            shot.drawAsArray();

            hitCoor[0] -= 0.02f;
            shot.translate(hitCoor);
            shot.updateProjectMatrix();
            shot.drawAsArray();
        }

        this.updateProjectMatrix();
        this.drawAsArray();
    }

    public boolean wasHit() {
        return wasHit;
    }

    public void setWasHit(boolean hit){wasHit = hit;}
}
