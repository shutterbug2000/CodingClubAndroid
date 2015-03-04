package engine_v01.assets;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * This is a utility class which includes many basic and some more complex vector maths.
 * Used in other utility classes as well as the entity system.
 */
public class Vec2 implements Cloneable {
	
	GL11 gl11;

	public float x, y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//Derive a vector from an angle using sine/cosine functions
	public static Vec2 fromAngle(float a) {
		return new Vec2((float) Math.sin(a), (float) Math.cos(a));
	}
	
	public static Vec2 fromMousePosition() {
		return new Vec2(Mouse.getX(), Mouse.getY());
	}
	
	public Vec2 add(Vec2 b) {
		return new Vec2(x + b.x, y + b.y);
	}
	
	public Vec2 subtract(Vec2 b) {
		return new Vec2(x - b.x, y - b.y);
	}
	
	//Rotate the vector around the origin using dot products and cross products
	public Vec2 rotate(Vec2 b) {
		return new Vec2(cross(b), dot(b));
	}
	
	public Vec2 midpoint(Vec2 b) {
		return new Vec2((x + b.x) / 2, (y + b.y) / 2);
	}
	
	public Vec2 reflect(Vec2 b) {
		return subtract(b.scale(2 * dot(b)));
	}
	
	//Scale the vector using a scalar
	public Vec2 scale(float s) {
		return new Vec2(x * s, y * s);
	}
	
	public Vec2 divide(float s) {
		return s == 0 ? new Vec2(0, 0) : new Vec2(x / s, y / s);
	}
	
	public Vec2 divide(Vec2 b) {
		return new Vec2(x / b.x, y / b.y);
	}
	
	//Find the vector perpendicular to itself, using the formula -y / x
	public Vec2 perpendicular() {
		return new Vec2(-y, x);
	}
	
	public Vec2 reflection() {
		return new Vec2(-x, y);
	}
	
	//Reverse the vector's direction
	public Vec2 negate() {
		return new Vec2(-x, -y);
	}	
	
	//Normalize the vector by dividing both its values by its length
	public Vec2 normalize() {
		return divide(length());
	}
	
	//Get the dot product of two vectors by adding together the product of their x values and y values
	public float dot(Vec2 b) {
		return x * b.x + y * b.y;
	}
	
	//Get the cross product (similar to dot product) by subtracting the cross-products (Think of a fraction)
	public float cross(Vec2 b) {
		return x * b.y - y * b.x;
	}
	
	public Vec2 cross(float scalar) {
		return new Vec2(y * scalar, x * -scalar);
	}
	
	//Calculate the length using the pythagorean theorem.
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	//Find the angle between the vector and the x-axis using trigonometry.
	public float direction() {
		return (float) Math.atan2(y, x);
	}
	
	public float overlap(Vec2 b) {
		return Math.min(y, b.y) - Math.max(x, b.x);
	}
	
	//Plug-in for OpenGL which allows the user to define vertex coordinates using a Vector2D
	public void glVertex() {
		GL11.glVertex2f(x, y);
	}
	
	public void glTexCoord() {
		GL11.glTexCoord2f(x, y);
	}
	
	@Override
	public Vec2 clone() {
		return new Vec2(x, y);
	}
	
	@Override
	public String toString() {
		return "{" + x + ", " + y + "}";
	}

}
