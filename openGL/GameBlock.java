package openGL;


import ToolBox.Matrix;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by andri on 05-Jun-16.
 */
public class GameBlock {

    private final float hW = 720/1280f;

    private float[] mvMatrix = {
	        hW, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] pMatrix2 = {
            0.1f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.1f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.1f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] scaleMove = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };

    private float[] scale2 = {
            0.05f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.05f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.05f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] trMatrix2 = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] Rotation = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] xRotation = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] yRotation = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
    };
    private float[] time = new float[1];
    private boolean rot = false;
    private boolean scl = false;
    private double angleX, angleY;
    GLObject figures[];//[];

    public GameBlock(InputStream objectStream, int shaderProgram) throws IOException {

        // Temporary part of code
        Matrix.multiplyMM(Rotation,  yRotation,  xRotation);
        Matrix.multiplyMM(scaleMove, trMatrix2, scale2);
        Matrix.multiplyMM(pMatrix2, scaleMove, Rotation);
        
        time[0] = (float)System.currentTimeMillis()/100.0f;

        InputStream textureSource = this.getClass().getResourceAsStream("/res/metaltexture2.png");

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
        
        int objectTexture = GLTextureLoader.loadTexture(textureSource);
        int anim1 = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/animation1.png"));
        int anim2 = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/animation2.png"));
        int anim3 = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/animation3.png"));
        int anim4 = GLTextureLoader.loadTexture(this.getClass().getResourceAsStream("/res/animation4.png"));

        figures = new GLObject[glVertexBuffer.getObjectsOffsets().length];


        int objSize = glVertexBuffer.getDataLenght();

        for(int i = figures.length-1; i>=0 ; i--) {

            figures[i] = new GLObject.GLObjectBuilder()
                    .addProgram(shaderProgram)
                    .addVBO(glVertexBuffer, 0)
                    .addOffset(glVertexBuffer.getObjectsOffsets()[i])
                    .addVertexArrayLenght(objSize - glVertexBuffer.getObjectsOffsets()[i])
                    .addVBO(glTextureCoorBuffer, 1)
                    .addTexture(objectTexture)
                    .addTexture(anim1)
                    .addTexture(anim2)
                    .addTexture(anim3)
                    .addTexture(anim4)
                    .addVBO(glNormalBuffer, 2)
                    .addUniformMatrix(mvMatrix, "u_mvMatrix")
                    .addUniformMatrix(pMatrix2, "u_pMatrix")
                    .buildObject();
            objSize = glVertexBuffer.getObjectsOffsets()[i];
        }
    }

    public void draw(){

        animation();
        if(rot || scl) {
            if(figures.length != 0)
                figures[0].addUniformMatrix(pMatrix2, "u_pMatrix");
        }
        for(int i = 0; i<figures.length; i++)
            figures[i].drawAsArray();
        rot = false;
        scl = false;
    }

    public void rotate(float[] shift){
        angleX += shift[1]/100;
        angleY += shift[0]/100;

        xRotation[5] = (float) Math.cos(angleX);
        xRotation[6] = (float) -Math.sin(angleX);
        xRotation[9] = (float) Math.sin(angleX);
        xRotation[10] = (float) Math.cos(angleX);

        yRotation[0] = (float) Math.cos(angleY);
        yRotation[2] = (float) Math.sin(angleY);
        yRotation[8] = (float) -Math.sin(angleY);
        yRotation[10] = (float) Math.cos(angleY);

        Matrix.multiplyMM(Rotation, yRotation, xRotation);
        Matrix.multiplyMM(pMatrix2, scaleMove, Rotation);
        rot = true;
    }

    public void scale(float scaleFactor){
        scale2[0] *= scaleFactor;
        scale2[5] *= scaleFactor;
        scale2[10] *= scaleFactor;
        Matrix.multiplyMM(scaleMove, trMatrix2, scale2);
        Matrix.multiplyMM(pMatrix2, scaleMove, Rotation);

        scl = true;
    }

    private void animation(){

        //figures[figures.length-1].setActiveTextureIndex(1);

        //time[0] = (float)SystemClock.uptimeMillis()/100;
        //figures[0].setUnifom(time[0], "time");
        figures[figures.length-1].setActiveTextureIndex((int)(System.currentTimeMillis()/100) % 4 + 1);
        /*long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.000030f * ((int) time);

        angleX +=angle;

        xRotation[5] = (float) Math.cos(angleX);
        xRotation[6] = (float) -Math.sin(angleX);
        xRotation[9] = (float) Math.sin(angleX);
        xRotation[10] = (float) Math.cos(angleX);

        Matrix.multiplyMM(Rotation, 0, yRotation, 0, xRotation, 0);
        Matrix.multiplyMM(pMatrix, 0, Rotation, 0, scale, 0);
        Matrix.multiplyMM(pMatrix2, 0, scaleMove, 0, Rotation, 0);
        rot = true;*/
    }

    public void cleanUp(){
        /*for(int i = 0; i<figures.length; i++)
            figures[i].cleanUp();*/
    }
}
