package com.gmail.ominousfire.tmps.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.gmail.ominousfire.tmps.TapeMeasureParticle;
import com.gmail.ominousfire.tmps.Universe;

public class Renderer implements Runnable {
	private static final int SEGMENT_SIZE = 10;
	private int depthRender = 100;
	private double yRender = 100;
	private double xRender = 100;
	private Universe universe;
	
	

	public Renderer(Universe universe) {
		this.universe = universe;
		(new Thread(this, "Render")).start();
	}

	void init() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
		    Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glOrtho(-xRender, xRender  , -yRender, yRender , -depthRender , depthRender);   
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	void renderParticle(TapeMeasureParticle tmp) {
		
	}

	@Override
	public void run() {
		init();
		while (!universe.isShuttingDown) 
		{
			prerender();
			MovementHelper.moveToCharacter();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glColor3f(1, 0, 0);
			GL11.glVertex3f(-1000,0,0);
			GL11.glVertex3f(1000,0,0);
			GL11.glColor3f(0, 1, 0);
			GL11.glVertex3f(0,-1000,0);
			GL11.glVertex3f(0,1000,0);
			GL11.glColor3f(0, 0, 1);
			GL11.glVertex3f(0,0,-10);
			GL11.glColor3f(0, 1, 1);
			GL11.glVertex3f(0,0,10);
			GL11.glEnd();
			for(int i = 0; i < Universe.numParticles / SEGMENT_SIZE; i++) {
				TapeMeasureParticle[] tmpArray = new TapeMeasureParticle[SEGMENT_SIZE];
				synchronized (universe.particlesInUniverse) {
					for (int j = 0; j < SEGMENT_SIZE;j++) {
						tmpArray[j] = universe.particlesInUniverse.get(i + j);
					}
				}
				for (int j = 0; j < SEGMENT_SIZE;j++) {
					TapeMeasureParticle tmp = tmpArray[j];
					synchronized(tmp) {
						tmp.render();
					}
				}
			}
			Display.update();
			if (Display.isCloseRequested()) universe.shutdown();
			MovementHelper.handleMovement();
		}
	}

	private void prerender() {
		float width = (float)Display.getWidth();
        float height = (float)Display.getWidth();
     
        //GL11.glEnable(GL11.GL_TEXTURE_2D);							// Enable Texture Mapping
       					// Set The Blending Function For Translucency
        GL11.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);               //This Will Clear The Background Color To Black
        GL11.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);  // Really Nice Perspective Calculations
        GL11.glViewport(0, 0, (int)width, (int)height);          
        GL11.glMatrixMode(GL11.GL_PROJECTION);                   
        GL11.glLoadIdentity();                                   
        GLU.gluPerspective(45.0f, width / height, 0.1f, 100.0f); 
        GL11.glMatrixMode(GL11.GL_MODELVIEW);                    
        GL11.glLoadIdentity();
        GL11.glRenderMode(GL11.GL_RGBA);
        
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
		
		
	}
}
