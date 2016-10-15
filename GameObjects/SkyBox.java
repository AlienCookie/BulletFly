package GameObjects;

import Editor.Camera;
import Editor.GLEditorObject;
import openGL.GLObject;
import openGL.GLTextureLoader;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by andri on 21-Sep-16.
 */
public class SkyBox {

    private GLEditorObject box;

    public SkyBox(GLObject.GLObjectBuilder builder){
        Vector<Integer> texture = new Vector<>();
        try {
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_bottom.png")));
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_front.png")));
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_top.png")));
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_back.png")));
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_right.png")));
            texture.add(GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/skybox_left.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.addTexture(texture);
        box = new GLEditorObject(builder);
        box.scale(new float[]{100.f, 100.f, 100.f});
    }

    public void draw(float[] cameraPosition){

        box.translate(cameraPosition);

        box.updateProjectMatrix();
        box.setActiveTextureIndex(0);
        box.drawAsArray();

        box.rotateXYZ(new float[]{90.f, 0.f, 0.f});
        box.updateProjectMatrix();
        box.setActiveTextureIndex(1);
        box.drawAsArray();

        box.rotateXYZ(new float[]{90.f, 0.f, 0.f});
        box.updateProjectMatrix();
        box.setActiveTextureIndex(2);
        box.drawAsArray();

        box.rotateXYZ(new float[]{90.f, 0.f, 0.f});
        box.updateProjectMatrix();
        box.setActiveTextureIndex(3);
        box.drawAsArray();

        box.rotateXYZ(new float[]{0.f, 90.f, 0.f});
        box.updateProjectMatrix();
        box.setActiveTextureIndex(4);
        box.drawAsArray();

        box.rotateXYZ(new float[]{0.f, 180.f, 0.f});
        box.updateProjectMatrix();
        box.setActiveTextureIndex(5);
        box.drawAsArray();

        box.rotateXYZ(new float[]{0.f, -270.f, 0.f});
        box.rotateXYZ(new float[]{-270.f, 0.f, 0.f});
    }

}
