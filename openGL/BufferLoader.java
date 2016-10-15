package openGL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/*
 */
public abstract class BufferLoader {

    public static void loadElementToVAO(int targetVAO, GLBufferData glBufferData, List<Integer> vboList){
        int buffer;

        GL30.glBindVertexArray(targetVAO);

        buffer = GL15.glGenBuffers();
        vboList.add(buffer);

        GL15.glBindBuffer(glBufferData.getBufferType(), buffer);
       /* if(glBufferData.getType() == 1)
            GL15.glBufferData(glBufferData.getBufferType(), glBufferData.getData(), glBufferData.getUsage());
        else*/
            GL15.glBufferData(glBufferData.getBufferType(), glBufferData.getData(), glBufferData.getUsage());
        GL30.glBindVertexArray (0);
    }

    public static void loadArrayToVAO(int targetVAO, GLBufferData glBufferData, List<Integer> vboList){
        int buffer;

        GL30.glBindVertexArray(targetVAO);

        buffer = GL15.glGenBuffers();
        vboList.add(buffer);

        GL15.glBindBuffer(glBufferData.getBufferType(), buffer);
        /* if(glBufferData.getType() == 1)
        GL15.glBufferData(glBufferData.getBufferType(), glBufferData.getData(), glBufferData.getUsage());
    else*/
        GL15.glBufferData(glBufferData.getBufferType(), glBufferData.getData(), glBufferData.getUsage());
        GL20.glEnableVertexAttribArray(glBufferData.getShaderVarRef());
        GL20.glVertexAttribPointer(glBufferData.getShaderVarRef(), glBufferData.getStep(), GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindVertexArray (0);
    }
}
