package DeafultPackage;

import Editor.GameEditor;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Platform;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBDebugOutput.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.*;


public class DisplayManager {

    private static final NkAllocator ALLOCATOR;
    Callback debugProc;
    static {
        ALLOCATOR = NkAllocator.create();
        ALLOCATOR.alloc((handle, old, size) -> {
            long mem = nmemAlloc(size);
            if ( mem == NULL )
                throw new OutOfMemoryError();

            return mem;

        });
        ALLOCATOR.mfree((handle, ptr) -> nmemFree(ptr));
    }


    private long win;

    // private Renderer renderer;
    private GameEditor editor;


    public DisplayManager() {
        GLFWErrorCallback.createPrint().set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize glfw");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if ( Platform.get() == Platform.MACOSX )
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        int WINDOW_WIDTH = 1600;
        int WINDOW_HEIGHT = 900;

        win = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "GLFW Nuklear Demo", NULL, NULL);
        if ( win == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(win);
        GLCapabilities caps = GL.createCapabilities();



        debugProc = GLUtil.setupDebugMessageCallback();

        if ( caps.OpenGL43 )
            glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer)null, false);
        else if ( caps.GL_KHR_debug ) {
            KHRDebug.glDebugMessageControl(
                    KHRDebug.GL_DEBUG_SOURCE_API,
                    KHRDebug.GL_DEBUG_TYPE_OTHER,
                    KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION,
                    (IntBuffer)null,
                    false
            );
        } else if ( caps.GL_ARB_debug_output )
            glDebugMessageControlARB(GL_DEBUG_SOURCE_API_ARB, GL_DEBUG_TYPE_OTHER_ARB, GL_DEBUG_SEVERITY_LOW_ARB, (IntBuffer)null, false);

        editor = new GameEditor(win);
    }

    protected void run() {
        glfwSwapInterval(1);
        glfwShowWindow(win);
        while ( !glfwWindowShouldClose(win) ) {

            editor.prepareInput();

            glfwPollEvents();

            editor.inputManaging();

            editor.draw();

            glfwSwapBuffers(win);
        }
        editor.cleanUp();
        glfwFreeCallbacks(win);
        if ( debugProc != null )
            debugProc.free();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}