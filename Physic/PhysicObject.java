package Physic;

import Editor.GLEditorObject;

/**
 * Created by andri on 07-Sep-16.
 */
public class PhysicObject extends GLEditorObject {

    private float[] speed;
    private float[] angularSpeed;
    private float[] acceleration;
    private float weight;
    private float bottom = 0f;


    public PhysicObject(GLObjectBuilder builder) {
        super(builder);
        speed = new float[]{0.f, 0.0f, 0.0f};
        angularSpeed = new float[]{0.f, 0.0f, 0.0f};
        acceleration = new float[]{0.f, 0.f, 0.f};
        weight = 1.f;
        bottom = 0.36f;
    }

    public void update(){
        PhysicManager.applyRegularPhysic(this);
        updateProjectMatrix();
    }

    public float[] getSpeed() {
        return speed;
    }

    public float[] getAcceleration() {
        return acceleration;
    }

    public float getWeight() {
        return weight;
    }

    public float[] getAngularSpeed(){return angularSpeed;}

    public void setAngularSpeed(float[] angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public void setSpeed(float[] speed) {
        this.speed = speed;
    }

    public float getBottom() {
        return bottom;
    }
}
