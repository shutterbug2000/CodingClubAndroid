package engine_v01.assets;

import org.newdawn.slick.opengl.Texture;

public class TextureAtlas extends Animation {

	private Texture texture;
	private Vec2 dimensions, point;
	
	public TextureAtlas(float speed, Texture texture, Vec2 dimensions) {
		super(speed);
		this.texture = texture;
		this.dimensions = dimensions;
		length = (int) (dimensions.x * dimensions.y);
	}

	public Vec2 getTexCoord(Vec2 texCoord) {
		return texCoord.divide(dimensions).add(point);
	}

	public Vec2 getTextureDimensions() {
		return new Vec2(texture.getImageWidth(), texture.getImageHeight()).divide(dimensions);
	}

	public void bind() {
		texture.bind();
	}
	
	public TextureAtlas clone() {
		return new TextureAtlas(speed, texture, dimensions.clone());
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		int y = (int) Math.floor(frame / dimensions.x);
		point = new Vec2((int) Math.floor(frame) - y * dimensions.x, y).divide(dimensions);
	}

}
