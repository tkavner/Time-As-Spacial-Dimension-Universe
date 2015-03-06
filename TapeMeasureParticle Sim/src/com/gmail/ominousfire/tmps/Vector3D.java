package com.gmail.ominousfire.tmps;

public class Vector3D {
	
	
	public static Point3D subtract(Point3D v1, Point3D v2) {
		return new Point3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static Point3D crossProduct(Point3D v1, Point3D v2) {
		return new Point3D(
				v1.y * v2.z - v1.z * v2.y, 
				v1.z * v2.x - v1.x * v2.z, 
				v1.x * v2.y - v1.y * v2.x
				);
	}
	
	public static Point3D normalVector(Point3D p1, Point3D p2, Point3D p3) {
		Point3D n = crossProduct(subtract(p1, p2), subtract(p2, p3));
		float f = (float) Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
		n.x /= f;
		n.y /= f;
		n.z /= f;
		return n;
	}
} class Point3D  {
	float x;
	float y;
	float z;
	
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void add(Point3D o1) {
		this.x += o1.x;
		this.y += o1.y;
		this.z += o1.z;
	}
	
	public void normalize() {
		float f = (float) Math.sqrt(x * x + y * y + z * z);
		x /= f;
		y /= f;
		z /= f;
	}
	
	public String toString() {
		return "" + x + " " + y + " " + z;
	}
	
}

