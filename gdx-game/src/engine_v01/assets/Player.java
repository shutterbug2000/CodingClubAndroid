package engine_v01.assets;

import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import org.lwjgl.input.Keyboard;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends Creature {
	
	private static final float jump_threshold = 0.9f;
	
	public static class PlayerType extends CreatureType {
		
		private float speed, airSpeed;
		private Vec2 jump;
		
		public PlayerType(Animation texture, boolean stretch, float mass, float rest, float s_friction, float d_friction, float health, float speed, float airSpeed, Vec2 jump) {
			super(texture, stretch, mass, rest, s_friction, d_friction, health);
			this.speed = speed;
			this.airSpeed = airSpeed;
			this.jump = jump;
		}
		
	}
	
	public class PlayerControls {
		
		public void update(int delta) {
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				velocity.x += onGround ? speed : airSpeed;
				side = false;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				velocity.x -= onGround ? speed : airSpeed;
				side = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W) && onGround) {
				velocity = velocity.add(jump);
				onGround = false;
			}
		}
		
	}
	
	protected float speed, airSpeed;
	protected Vec2 jump, n_jump;
	private boolean side, onGround;
	
	public Player(World world, Shape shape, Vec2 velocity, PlayerType t) {
		super(world, shape, velocity, t);
		this.speed = t.speed;
		this.airSpeed = t.airSpeed;
		this.jump = t.jump;
		n_jump = t.jump.normalize();
	}
	
	@Override
	protected void onCollision(Vec2 impulse, Vec2 c) {
		if(impulse.normalize().dot(n_jump) > jump_threshold) onGround = true;
		super.onCollision(impulse, c);
	}
	
	protected void draw() {
		texture.bind();
		ShapeRenderer shaperender = new ShapeRenderer();
		shaperender.begin(ShapeType.Filled);
		for(int i = 0; i < shape.vertices.length; i++) {
			Vec2 uv = texCoords.vertices[i];
			texture.getTexCoord(side ? new Vec2(1 - uv.x, uv.y) : uv).glTexCoord();
			shaperender.point(shape.vertices[i].x,shape.vertices[i].y,0);
		}
		shaperender.end();
	}

}
