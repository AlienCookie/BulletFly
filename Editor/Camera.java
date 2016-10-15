package Editor;

import ToolBox.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
/**
 * Created by andri on 11-Sep-16.
 */
public class Camera {
    public static float[] projectionMatrix = {
            1.f, 0.f, 0.f, 0.f,
            0.f, 1.f, 0.f, 0.f,
            0.f, 0.f, 1.f, 0.f,
            0.f, 0.f, 0.f, 1.f,};

    private static float[] perspectiveMatrix = projectionMatrix.clone(),
            tempMatrix = projectionMatrix.clone();

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.01f;
    private static final float FAR_PLANE = 200;

    private float yaw = 0.f;
    private float pitch = 0.f;

    private float[] angle = new float[3];

    private float[] position = new float[4];


    public Camera() {
        float aspectRatio = (float) 640 / (float) 360;
        float y_scale = (float) (1f / Math.tan((FOV / 2f)*0.0174533f));// * aspectRatio;
        float x_scale = y_scale;// / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        perspectiveMatrix[0] = x_scale;
        perspectiveMatrix[5] = y_scale;
        perspectiveMatrix[10] = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        perspectiveMatrix[11] = -1.0f;
        perspectiveMatrix[14] = -((2f * NEAR_PLANE * FAR_PLANE) / frustum_length);
        perspectiveMatrix[15] = 0.0f;

        position[2] = 3f;
        position[1] = 2f;

        updateProjectMatrix();
    }

    public void updateProjectMatrix() {

        tempMatrix = Matrix.createViewMatrix(pitch, yaw, position);

        Matrix.multiplyMM(projectionMatrix, perspectiveMatrix, tempMatrix);
    }

    public float[] getAngle() {
        return angle;
    }

    public float[] getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
