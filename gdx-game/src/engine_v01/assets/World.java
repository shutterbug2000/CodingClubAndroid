package engine_v01.assets;

import java.util.ArrayList;
import java.util.List;

import engine_v01.assets.Shape.CollisionResult;
import static org.lwjgl.opengl.GL11.*;

/**
 * The plug-in for 2D physics. Everything physics-related starts here and ends here, no exceptions
 */
public class World {
	
	public static class EntityType {
		
		private float mass, rest, s_friction, d_friction;
		private Animation texture;
		private boolean stretch;
		
		public EntityType(Animation texture, boolean stretch, float mass, float rest, float s_friction, float d_friction) {
			this.texture = texture;
			this.mass = mass;
			this.rest = rest;
			this.s_friction = s_friction;
			this.d_friction = d_friction;
			this.stretch = stretch;
		}
		
	}
	
	public class Entity {
		
		/**Utilizes Shape2D and Vector2D classes, which include some complex maths. Check for
		 * yourself if you want to see what's happening behind the scenes.
		 */
		protected Shape shape, texCoords;
		protected Vec2 velocity;
		/**
		 * Mass, inverse mass, restitution, static friction, and dynamic friction.
		 */
		protected float mass, i_mass, rest, s_friction, d_friction;
		protected Animation texture;
		
		protected void remove() {
			/**
			 * "Delete" this entity. Java Garbage Collection will erase the memory.
			 */
			entities.remove(this);
		}
		
		/**
		 * Applies gravity to the entity.
		 * @param delta Delta time
		 */
		protected void update(int delta) {
			shape.translate(velocity.scale(delta));
			velocity = velocity.add(gravity.scale(mass));
			Vec2 n = velocity.subtract(velocity.scale(airDensity));
			velocity = n.dot(velocity) < 0 ? new Vec2(0, 0) : n;
			texture.update(delta);
		}
		
		/**
		 * Checks for a collision between 2 entities.
		 * @param impulse Impulse vector.
		 * @param c Correction vector to push the entities apart.
		 */
		protected void onCollision(Vec2 impulse, Vec2 c) {
			shape.translate(c.scale(i_mass));
			velocity = velocity.add(impulse.scale(i_mass));
		}
		
		/**
		 * Draws an entity.
		 */
		protected void draw() {
			texture.bind();
			glBegin(GL_POLYGON);
			for(int i = 0; i < shape.vertices.length; i++) {
				texture.getTexCoord(texCoords.vertices[i]).glTexCoord();
				shape.vertices[i].glVertex();
			}
			glEnd();
		}
		
		public Entity(Shape shape, Vec2 velocity, Animation texture, boolean stretch, float mass, float rest, float s_friction, float d_friction) {
			this.shape = shape;
			this.velocity = velocity;
			this.texture = texture.clone();
			this.mass = mass;
			this.i_mass = mass == 0 ? 0 : 1 / mass;
			this.rest = rest;
			this.s_friction = s_friction;
			this.d_friction = d_friction;
			Vec2 min = shape.min(), d = stretch ? shape.max().subtract(min) : texture.getTextureDimensions();
			Vec2[] texCoords = new Vec2[shape.vertices.length];
			for(int i = 0; i < shape.vertices.length; i++) texCoords[i] = shape.vertices[i].subtract(min).divide(d);
			this.texCoords = new Shape(texCoords);
			entities.add(this);
		}
		
		public Entity(Shape shape, Vec2 velocity, EntityType t) {
			this(shape, velocity, t.texture, t.stretch, t.mass, t.rest, t.s_friction, t.d_friction);
		}
		
	}
	
	/**
	 * Apply physics to entities and check for collisions using delta timing.
	 * @param delta Delta time
	 */
	public void update(int delta) {
		for(int i1 = 0; i1 < entities.size(); i1++) {
			Entity entity1 = entities.get(i1);
			entity1.update(delta);
			for(int i2 = i1 + 1; i2 < entities.size(); i2++) {
				
				Entity entity2 = entities.get(i2);
				float tm = entity1.i_mass + entity2.i_mass;
				if(tm == 0) continue;
				CollisionResult r = entity1.shape.checkCollision(entity2.shape);
				if(r == null) continue;
				Vec2 rv = entity2.velocity.subtract(entity1.velocity);
				float d = entity2.velocity.subtract(entity1.velocity).dot(r.normal);
				if(d > 0) continue;
				Vec2 t = rv.subtract(r.normal.scale(r.normal.dot(rv))).normalize();
				float e = Math.min(entity1.rest, entity2.rest), j = -(1 + e) * d / tm, jt = -rv.dot(t) / tm, sf = (entity1.s_friction + entity2.s_friction) / 2;
				
				Vec2 i = r.normal.scale(j).add(t.scale(Math.abs(jt) < j * sf ? jt : (entity1.d_friction + entity2.d_friction) / 2 * -j)), c = r.normal.scale(r.depth / tm);
				entity1.onCollision(i.negate(), c.negate());
				entity2.onCollision(i, c);
				
			}
		}
	}
	
	/**
	 * Draws the entities in the world.
	 */
	public void draw() {
		for(Entity entity : entities) entity.draw();
	}

	private List<Entity> entities = new ArrayList<Entity>();
	private Vec2 gravity;
	/**
	 * Resistance when moving through the air. Space has 0 resistance.
	 */
	private float airDensity;
	
	public World(Vec2 gravity, float airDensity) {
		this.gravity = gravity;
		this.airDensity = airDensity;
	}
	
}
