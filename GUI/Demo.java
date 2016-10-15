/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package GUI;

import GameObjects.Cannon;
import Physic.PhysicManager;
import org.lwjgl.nuklear.NkColor;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkPanel;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Java port of
 * <a href="https://github.com/vurtun/nuklear/blob/master/demo/glfw_opengl3/main.c">https://github.com/vurtun/nuklear/blob/master/demo/glfw_opengl3/main.c</a>.
 */
public class Demo {

	private static final int EASY = 0;
	private static final int HARD = 1;

	public NkColor background;
	private int op = EASY;
	private NkContext ctx;
	private Cannon cannon;

	public Demo(NkContext _ctx, Cannon cannon) {
		background = NkColor.create();
		nk_rgb(28, 48, 62, background);
		this.cannon = cannon;
		this.ctx = _ctx;
	}

	public void layout(int x, int y) {
		try ( MemoryStack stack = stackPush() ) {
			NkPanel layout = NkPanel.mallocStack(stack);
			NkRect rect = NkRect.mallocStack(stack);

			if ( nk_begin(
				ctx,
				layout,
				"Parameters",
				nk_rect(x, y, 1600, 150, rect),
				NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE
			) ) {
				nk_layout_row_dynamic(ctx, 35, 8);

				PhysicManager.gravity = nk_propertyf(ctx, "Gravity:", 0f, -980f * PhysicManager.gravity, 50f, 0.01f, 0.01f) / -980f;

				cannon.pinch = -nk_propertyf(ctx, "Angle:", 0f, -cannon.pinch, 61f, 1f, 0.1f);

				cannon.power = nk_propertyf(ctx, "Power:", 0f, cannon.power, 50f, 0.1f, 0.1f);

			}
			nk_end(ctx);
		}
	}

}