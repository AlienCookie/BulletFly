package GameObjects;

import Editor.GLEditorObject;
import openGL.GLTextureLoader;

import java.io.IOException;

/**
 * Created by andri on 11-Sep-16.
 */
public class Floor extends GLEditorObject {
    private float[] prevPlayerPos = new float[3];

    public Floor(GLObjectBuilder builder) {
        super(builder);

        int[] grassTexture = {-1};
        try {
            grassTexture[0] = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/grass.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setTexture(grassTexture);
        this.setTextured(true);
    }

    public void draw(int ammount, float[] playerPosition){
        float[] shift = {
                playerPosition[0] - prevPlayerPos[0],
                playerPosition[1] - prevPlayerPos[1],
                playerPosition[2] - prevPlayerPos[2]};
     //   this.translate(new float[]{playerPosition[0], 0.f, playerPosition[2]});
    //   this.move(new float[]{shift[0], 0.f, shift[2]});

        for(int i = -ammount; i < ammount; i++) {
            for (int j = -ammount; j < ammount; j++) {
                this.move(new float[] {8f*i, 0f, 8f*j});
                this.updateProjectMatrix();
                this.drawAsArray();
                this.move(new float[] {-8f*i, 0f, -8f*j});
            }
        }
        prevPlayerPos = playerPosition;
    }
}
