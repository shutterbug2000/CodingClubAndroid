package engine_v01.assets;

/**A more complex geometrical class, usually used to detect collisions, but has many applications.
 * Utilizes the Vector2D class heavily.
 */
public class Shape {
	
	//The wrapper class used to handle collisions, usually discarded immediately
	public class CollisionResult {
		
		public Vec2 normal;
		public float depth;
		
		private CollisionResult(Vec2 normal, float depth) {
			this.normal = normal;
			this.depth = depth;
		}
		
	}

	public Vec2[] vertices;
	
	public Shape(Vec2...vertices) {
		this.vertices = vertices;
	}
	
	//Move the shape using a vector
	public void translate(Vec2 d) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i] = vertices[i].add(d);
		}
	}
	
	public Shape rotate(Vec2 center, float a) {
		Vec2[] newVertices = new Vec2[vertices.length];
		Vec2 d = Vec2.fromAngle(a);
		//Iterate over vertices
		for(int i = 0; i < vertices.length; i++) {
			//Translate vertex to origin
			Vec2 p = vertices[i].subtract(center);
			Vec2 r = p.rotate(d);
			newVertices[i] = center.add(r);
		}
		return new Shape(newVertices);
	}
	
	public Vec2 min() {
		
		float x = vertices[0].x, y = vertices[0].y;
		for(int i = 1; i < vertices.length; i++) {
			Vec2 vertex = vertices[i];
			x = Math.min(x, vertex.x);
			y = Math.min(y, vertex.y);
		}
		return new Vec2(x, y);
		
	}
	
	public Vec2 max() {
		
		float x = vertices[0].x, y = vertices[0].y;
		for(int i = 1; i < vertices.length; i++) {
			Vec2 vertex = vertices[i];
			x = Math.max(x, vertex.x);
			y = Math.max(y, vertex.y);
		}
		return new Vec2(x, y);
		
	}
	
	public Vec2 center() {
		return min().midpoint(max());
	}
	
	//Get the axes for testing by normalizing the vectors perpendicular the the edges
	public Vec2[] axes() {
		Vec2[] axes = new Vec2[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			Vec2 p1 = vertices[i];
			Vec2 p2 = vertices[i + 1 == vertices.length ? 0 : i + 1];
			Vec2 edge = p1.subtract(p2);
			Vec2 normal = edge.perpendicular();
			axes[i] = normal.normalize();
		}
		return axes;
	}
	
	//Project the shape onto a 1-dimensional surface, like creating a shadow
	public Vec2 project(Vec2 axis) {
		float min = axis.dot(vertices[0]), max = min;
		for(int i = 1; i < vertices.length; i++) {
			float p = axis.dot(vertices[i]);
			if(p < min) min = p;
			else if(p > max) max = p;
		}
		Vec2 projection = new Vec2(min, max);
		return projection;
	}

	//Test for collisions using the Seperating-Axis Theorem. The theorem states that if all
	//of the overlaps of the shadows of the shapes when projected onto their individual
	//axes do not overlap, then the shapes are not colliding. This algorithm works well
	//because it test collisions between any kind of shape, as long as the shape is not convex
	public CollisionResult checkCollision(Shape b) {
		
		float depth = 1000;
		
		Vec2 normal = null;
		Vec2[] axes1 = axes(), axes2 = b.axes();
		
		for(Vec2 axis : axes1) {
			
			Vec2 p1 = project(axis), p2 = b.project(axis);
			float ol = p1.overlap(p2);
			
			if (ol < 0) return null;
			if(ol < depth) {
				depth = ol;
				normal = axis;
			}
			
		}
		
		for(Vec2 axis : axes2) {
			
			Vec2 p1 = project(axis), p2 = b.project(axis);
			float ol = p1.overlap(p2);
			
			if (ol < 0) return null;
			if(ol < depth) {
				depth = ol;
				normal = axis;
			}
			
		}
		
		Vec2 d = center().subtract(b.center());
		if(normal.dot(d) > 0) normal = normal.negate();

		return new CollisionResult(normal, depth);
	}

	@Override
	public String toString() {
		String s = "{";
		for(Vec2 v : vertices) {
			s += v.toString() + " ";
		}
		return s + "}";
	}
	
}

