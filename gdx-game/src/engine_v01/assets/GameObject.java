package engine_v01.assets;

//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import engine_v01.assets.Player.PlayerControls;
import engine_v01.assets.Player.PlayerType;

/**
 * The game will be initialized from this class. The initialization/boot process will
 * include creating the world, starting the timer, loading resources, initializing OpenGL, etc.
 */
public class GameObject extends Thread {
	
	private int width, height, time, lastTime;
	private World world;
	private GLSLProgram shader;
	
	public GameObject(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets the current time from the system, used in calculating the delta time
	 * @return System Time
	 */
	private int getTime() {
		return (int) (Sys.getTime() * 1000 / Sys.getTimerResolution());
	}
	
	/**
	 * Creates and runs the game
	 */
	public void start() {
		
		try {
			
			Display.setTitle("Game");
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setVSyncEnabled(true);
			Display.create();

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glClearColor(0.5f, 0.5f, 1, 1);
			
			glOrtho(0, width, height, 0, 1, -1);
			
			shader = new GLSLProgram(GLSLProgram.loadShader(GL_VERTEX_SHADER, "res/shader/lighting.vs"), GLSLProgram.loadShader(GL_FRAGMENT_SHADER, "res/shader/lighting.fs"));
			shader.use();
			shader.setUniform("fog", 0.5f, 0.5f, 1);
			
			Animation t1 = new TextureAtlas(0, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/texture/material/foliage1.png")), new Vec2(1, 1)),
					t2 = new TextureLineup(0.004f, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/texture/Playable/Knight/Knight_Good.png")), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/texture/Playable/Knight/Knight_Good_Swing.png")));
			
			lastTime = getTime();
			world = new World(new Vec2(0, 0.01f), 0.01f);
			world.new Entity(Rectangle.fromHalfDimension(new Vec2(500, 600), new Vec2(500, 100)), new Vec2(0, 0), t1, false, 0, 0.3f, 0.9f, 0.99f);
			PlayerType type = new PlayerType(t2, true, 1, 0.3f, 0.8f, 0.9f, 1, 0.015f, 0.0001f, new Vec2(0, -0.2f));
			PlayerControls controls = null;
			ArrayList<Vec2> clickPoints = new ArrayList<Vec2>();
			
			while(!Display.isCloseRequested()) {
				
				time = getTime();
				int delta = time - lastTime;
				
				lastTime = time;
				
				while(Mouse.next()) {
					if(!Mouse.getEventButtonState()) continue;
					Vec2 m = new Vec2(Mouse.getX(), height - Mouse.getY());
					switch(Mouse.getEventButton()) {
						case 0 : clickPoints.add(m);
						break;
						case 1 : {
							Player p = new Player(world, Rectangle.fromHalfDimension(m, new Vec2(20, 40)), new Vec2(0, 0), type);
							controls = p.new PlayerControls();
							break;
						}
					}
				}
				
				while(Keyboard.next()) {
					if(!Keyboard.getEventKeyState()) continue;
					switch(Keyboard.getEventKey()) {
						case Keyboard.KEY_SPACE : {
							if(clickPoints.size() < 3) continue;
							world.new Entity(new Shape(clickPoints.toArray(new Vec2[clickPoints.size()])), new Vec2(0, 0), t1, false, 0, 0.3f, 0.6f, 0.5f);
							clickPoints.clear();
							break;
						}
						case Keyboard.KEY_C : clickPoints.clear();
						break;
						case Keyboard.KEY_ESCAPE : return;
					}
				}
				
				if(controls != null) controls.update(delta);
				world.update(delta);

				shader.setUniform("point", Mouse.getX(), Mouse.getY());
				shader.setUniform("time", time);
				
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				world.draw();
				
				if(clickPoints.size() > 1) {
					float i = 0.5f + 0.5f * (float) Math.sin(0.003f * time);
					glDisable(GL_TEXTURE_2D);
					glColor4f(0, 0, 0, i);
					glBegin(GL_LINE_LOOP);
					for(Vec2 v : clickPoints) v.glVertex();
					glEnd();
					glColor4f(1, 1, 1, 1);
					glEnable(GL_TEXTURE_2D);
				}
			
				Display.update();
				Display.sync(60);
			
			}
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Display.destroy();
			System.exit(1);
		}
		
	}
	
	public static void main(String[] args) {
		new GameObject(1000, 700).start();
	}
	
}
