package engine_v01.assets;

public abstract class Animation implements Cloneable {

	protected int length;
	protected float frame, speed;
	
	public abstract Vec2 getTexCoord(Vec2 texCoord);
	public abstract Vec2 getTextureDimensions();
	public abstract void bind();
	public abstract Animation clone();
	
	public void update(int delta) {
		frame = (frame + speed * delta) % length;
	}
	
	public Animation(float speed) {
		this.speed = speed;
	}
	
}
