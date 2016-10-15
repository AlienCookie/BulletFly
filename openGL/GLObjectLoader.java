package openGL;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/*
 */
public class GLObjectLoader {
    protected float[] vertices;
    protected float[] textureCoord;
    protected float[] normals;

    protected int[] objOffsets = new int[1];

    protected Vector<String> names = new Vector<>();


    public GLObjectLoader(InputStream obj) throws IOException {
        Scanner objectScanner = new Scanner(obj);
        //Scanner lineScanner;
        String outLine;
        boolean objectStart = true;

        String[] divStrings;

        List<Float> verticesList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Float> textureCoordList = new ArrayList<>();

        List<Integer> vertexIndecesList = new ArrayList<>();
        List<Integer> textureCoordIndecesList = new ArrayList<>();
        List<Integer> normalsIndecesList = new ArrayList<>();

        while(objectScanner.hasNextLine()){
            outLine = objectScanner.nextLine();

            if(!outLine.isEmpty()) {
                if (outLine.charAt(0) == 'v' && outLine.charAt(1) == ' ') {
                    objectStart = true;
                    if(objectStart)
                    outLine = outLine.substring(2);
                    divStrings = outLine.split(" ");

                    verticesList.add(new Float(divStrings[0]));
                    verticesList.add(new Float(divStrings[1]));
                    verticesList.add(new Float(divStrings[2]));
                }
                else if (outLine.charAt(0) == 'v' && outLine.charAt(1) == 'n') {
                    objectStart = true;
                    outLine = outLine.substring(3);
                    divStrings = outLine.split(" ");

                    normalsList.add(new Float(divStrings[0]));
                    normalsList.add(new Float(divStrings[1]));
                    normalsList.add(new Float(divStrings[2]));
                }
                else if (outLine.charAt(0) == 'v' && outLine.charAt(1) == 't') {
                    objectStart = true;
                    outLine = outLine.substring(3);
                    divStrings = outLine.split(" ");

                    textureCoordList.add(new Float(divStrings[0]));
                    textureCoordList.add(new Float(divStrings[1]));
                }

                else if (outLine.charAt(0) == 'n' && outLine.charAt(1) == 'm') {
                    objectStart = true;
                    outLine = outLine.substring(3);

                    names.add(outLine);
                }

                else if (outLine.charAt(0) == 'f' && outLine.charAt(1) == ' ') {
                    if(objectStart){
                        if(vertexIndecesList.size() != 0)
                            objOffsets = Arrays.copyOf(objOffsets, objOffsets.length+1);
                        objOffsets[objOffsets.length-1] = vertexIndecesList.size();
                        objectStart = false;
                    }
                    outLine = outLine.substring(2);
                    String[] strings = outLine.split(" ");

                    String[] indices = strings[0].split("/");
                    if(!indices[0].isEmpty())
                        vertexIndecesList.add(new Integer(indices[0])-1);
                    if(!indices[1].isEmpty())
                        textureCoordIndecesList.add(new Integer(indices[1])-1);
                    if(!indices[2].isEmpty())
                        normalsIndecesList.add(new Integer(indices[2])-1);

                    indices = strings[1].split("/");
                    if(!indices[0].isEmpty())
                        vertexIndecesList.add(new Integer(indices[0])-1);
                    if(!indices[1].isEmpty())
                        textureCoordIndecesList.add(new Integer(indices[1])-1);
                    if(!indices[2].isEmpty())
                        normalsIndecesList.add(new Integer(indices[2])-1);

                    indices = strings[2].split("/");
                    if(!indices[0].isEmpty())
                        vertexIndecesList.add(new Integer(indices[0])-1);
                    if(!indices[1].isEmpty())
                        textureCoordIndecesList.add(new Integer(indices[1])-1);
                    if(!indices[2].isEmpty())
                        normalsIndecesList.add(new Integer(indices[2])-1);
                }
            }
        }

        ListIterator<Float> floatListIterator;
        ListIterator<Integer> intListIterator;

        if(verticesList.size() != 0) {
            floatListIterator = verticesList.listIterator();
            float[] verticesSet = new float[verticesList.size()];
            while (floatListIterator.hasNext())
            {
                verticesSet[floatListIterator.nextIndex()] = floatListIterator.next();
            }

            intListIterator = vertexIndecesList.listIterator();
            vertices = new float[vertexIndecesList.size() * 3];
            while (intListIterator.hasNext())
            {
                int nextIndex = intListIterator.nextIndex();
                int next = intListIterator.next();
                vertices[nextIndex*3] = verticesSet[next*3];
                vertices[nextIndex*3+1] = verticesSet[next*3+1];
                vertices[nextIndex*3+2] = verticesSet[next*3+2];
            }

        }
        if(textureCoordList.size() != 0) {
            floatListIterator = textureCoordList.listIterator();
            float[] textureCoordSet = new float[textureCoordList.size()];
            while (floatListIterator.hasNext())
            {
                textureCoordSet[floatListIterator.nextIndex()] = floatListIterator.next();
            }

            intListIterator = textureCoordIndecesList.listIterator();
            textureCoord = new float[textureCoordIndecesList.size() * 2];
            while (intListIterator.hasNext())
            {
                int nextIndex = intListIterator.nextIndex();
                int next = intListIterator.next();
                textureCoord[nextIndex*2] = textureCoordSet[next*2];
                textureCoord[nextIndex*2+1] = textureCoordSet[next*2+1];
            }
        }
        if(normalsList.size() != 0) {
            floatListIterator = normalsList.listIterator();
            float[] normalsSet = new float[normalsList.size()];
            while (floatListIterator.hasNext())
            {
                normalsSet[floatListIterator.nextIndex()] = floatListIterator.next();
            }

            intListIterator = normalsIndecesList.listIterator();
            normals = new float[normalsIndecesList.size() * 3];
            while (intListIterator.hasNext())
            {
                int nextIndex = intListIterator.nextIndex();
                int next = intListIterator.next();
                normals[nextIndex*3] = normalsSet[next*3];
                normals[nextIndex*3+1] = normalsSet[next*3+1];
                normals[nextIndex*3+2] = normalsSet[next*3+2];
            }
        }


    }


    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoord() {
        return textureCoord;
    }

    public float[] getNormals() {
        return normals;
    }

    public Vector<String> getNames() {
        return names;
    }

    public int[] getObjOffsets(){
        return objOffsets;
    }
}
