package openGL;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by andri on 02-Aug-16.
 */
public class GLObjectsCreator {

    private static float hW = 720/1280f;

    private static float[] mvMatrix = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private static float[] pMatrix = mvMatrix.clone();

    public static Map<String, GLObject.GLObjectBuilder> glObjectBuilderMap(InputStream objectStream) throws IOException
    {
        mvMatrix[0] = hW;

        Map<String, GLObject.GLObjectBuilder> glObjectBuilderMap = new LinkedHashMap<>();

        GLObjectLoader glObjectLoader = new GLObjectLoader(objectStream);

        GLBuffer glVertexBuffer = new GLBuffer
                .GLBufferBuilder(new GLBufferData(glObjectLoader.getVertices()))
                .offsets(glObjectLoader.getObjOffsets())
                .build();

        GLBuffer glTextureCoorBuffer = new GLBuffer
                .GLBufferBuilder(new GLBufferData(glObjectLoader.getTextureCoord()))
                .step(2)
                .build();

        GLBuffer glNormalBuffer = new GLBuffer
                .GLBufferBuilder(new GLBufferData(glObjectLoader.getNormals()))
                .build();

        // InputStream vShaderStream = GLObjectsCreator.class.getClass().getResourceAsStream("/res/vshader2.txt");
        // InputStream fShaderStream = GLObjectsCreator.class.getClass().getResourceAsStream("/res/fshader2.txt");
        // int program = GLProgram.generateProgram(FileManager.streamToString(vShaderStream), FileManager.streamToString(fShaderStream));

        for (int i = 0; i < glVertexBuffer.getObjectsOffsets().length; i++) {
            InputStream vShaderStream = GLObjectsCreator.class.getClass().getResourceAsStream("/res/vshader2.txt");
            InputStream fShaderStream = GLObjectsCreator.class.getClass().getResourceAsStream("/res/fshader2.txt");
            int program = GLProgram.generateProgram(FileManager.streamToString(vShaderStream), FileManager.streamToString(fShaderStream));

            int arrayLen = (i == glVertexBuffer.getObjectsOffsets().length-1) ? glVertexBuffer.getDataLenght() /12
                    - glVertexBuffer.getObjectsOffsets()[i] :
                    glVertexBuffer.getObjectsOffsets()[i+1]
                            - glVertexBuffer.getObjectsOffsets()[i];

            int offset = glVertexBuffer.getObjectsOffsets()[i];
            glObjectBuilderMap.put(glObjectLoader.getNames().get(i),
                    new GLObject.GLObjectBuilder()
                            .addVBO(glVertexBuffer, 0)
                            .addOffset(offset)
                            .addVertexArrayLenght(arrayLen)
                            .addVBO(glTextureCoorBuffer, 1)
                            .addVBO(glNormalBuffer, 2)
                            .addProgram(program)
                            .addUniformMatrix(mvMatrix, "u_mvMatrix")
                            .addUniformMatrix(pMatrix, "u_pMatrix"));
        }

        return glObjectBuilderMap;
    }
}
