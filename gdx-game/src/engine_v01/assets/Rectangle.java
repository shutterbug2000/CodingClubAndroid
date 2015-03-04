package engine_v01.assets;

/**
 * Extension of the Shape2D class designed to speed up processing by using a minimal amount of
 * axes. Also contains an convenience constructor which takes the center and the dimensions
 */
public class Rectangle extends Shape {

	private Rectangle(Vec2...vec2s) {
		super(vec2s);
	}
	
	@Override
	public Vec2[] axes() {
		Vec2[] axes = new Vec2[2];
		for(int i = 0; i < 2; i++) {
			Vec2 p1 = vertices[i];
			Vec2 p2 = vertices[i == 2 ? 0 : i + 1];
			Vec2 edge = p1.subtract(p2);
			Vec2 normal = edge.perpendicular();
			axes[i] = normal.normalize();
		}
		return axes;
	}
	
	public static Rectangle fromHalfDimension(Vec2 c, Vec2 hd) {
		Vec2 p = hd.reflection();
		return new Rectangle(c.subtract(hd), c.subtract(p), c.add(hd), c.add(p));
	}
	
	public static Rectangle fromCorners(Vec2 c1, Vec2 c2) {
		return new Rectangle(c1, new Vec2(c2.x, c1.y), c2, new Vec2(c1.x, c2.y));
	}

}
