package Physic;

/**
 * Created by andri on 07-Sep-16.
 */
public class PhysicManager {

    public static float gravity = -0.01f;

    public static void applyRegularPhysic(PhysicObject object){
        float[] acceleration = object.getAcceleration();
        float[] speed = object.getSpeed();
        speed[1] += gravity;
        //speed += acceleration;
        object.move(speed);
        object.rotateXYZ(new float[]{4.0f, 0.f, 0.0f});

        if(object.getPosition()[1] < object.getBottom()){
            float[] toGrount = object.getPosition();
            toGrount[1] = object.getBottom();
            object.translate(toGrount);
            speed[1] = 0.f;
            object.rotateXYZ(new float[]{-4.0f, 0.f, 0.0f});
            object.rotateXYZ(new float[]{-speed[1]*180f / (float)Math.PI, 0, speed[0]*180f / (float)Math.PI});
        }
        speed[0] *=0.987f;
        speed[2] *=0.987f;

        if(Math.abs(speed[0]) < 0.01f) speed[0] = 0.f;
        if(Math.abs(speed[2]) < 0.01f) speed[2] = 0.f;

    }
}
