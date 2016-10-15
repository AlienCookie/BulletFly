package GameObjects;

import Physic.PhysicObject;
import openGL.GLTextureLoader;

import java.io.IOException;

/**
 * Created by andri on 11-Sep-16.
 */
public class Bullet  extends PhysicObject{
    public Bullet(GLObjectBuilder builder) {
        super(builder);

        int[] bulletTexture = {-1};
        try {
           bulletTexture[0] = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/metaltexture2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setTexture(bulletTexture);
        this.setTextured(true);
    }
}
