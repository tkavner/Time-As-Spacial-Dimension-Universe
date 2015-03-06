package com.gmail.ominousfire.tmps;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class TapeMeasureParticle {

	List<LineSegment> tapeMeasureBends;

	public static final double MAXIMUM_RANGE_TO_INTERACT = .001;

	TapeMeasureParticle(LineSegment starterSegment) {
		tapeMeasureBends = new ArrayList<LineSegment>();
		addNewSegment(starterSegment);
	}

	void addNewSegment(LineSegment newSegment) {
		newSegment.isPartOf = this;
		tapeMeasureBends.add(newSegment);
	}

	public CollisionPoint checkForCollisionAndInteract(TapeMeasureParticle tmp2) {
		//if the line segments intersect, they should bounce off each other
		//the line segments intersect if either one's non-finished end intersects with any of the other parts of the particle.
		//Else, it should intersected earlier on.
		LineSegment ls1 = tapeMeasureBends.get(tapeMeasureBends.size() - 1);
		for (int i = 0; i < tmp2.tapeMeasureBends.size(); i++) {
			LineSegment ls2 = tmp2.tapeMeasureBends.get(i);
			boolean collides = false;
			boolean inRange = false;
			//Find the T at which it must intersect at.
			double t = (ls2.posX - ls1.posX) / (ls1.velX - ls2.velX);
			//Find if it collides on the Y at the given x and t
			if (t > ls2.posT && t > ls1.posT &&
					Math.abs(t * (ls1.velY - ls2.velY) + ls1.posY - ls2.posY) < MAXIMUM_RANGE_TO_INTERACT) {
				collides = true;
			}
			//if it isnt the end of the segment
			if (i + 1 < tmp2.tapeMeasureBends.size()) {
				LineSegment ls3 = tmp2.tapeMeasureBends.get(i + 1);
				if (ls3.posT > t) {
					inRange = true;
				}
			} else {
				inRange = true;
			}
			if (inRange && collides) {
				return (new CollisionPoint(t, ls1, ls2)).register();
			}
		}
		return null;
	}

	public void removeHigherSegments(LineSegment ls) {
		int cleanUntil = tapeMeasureBends.indexOf(ls);
		for(int i = tapeMeasureBends.size() - 1; tapeMeasureBends.size() < cleanUntil; i--) {
			LineSegment higherSegment = tapeMeasureBends.get(i);
			tapeMeasureBends.remove(higherSegment);
			CollisionPoint.remove(higherSegment);
		}
	}


	public void render() {
		for(int i = 0; i < tapeMeasureBends.size(); i++) {
			LineSegment ls = tapeMeasureBends.get(i);
			GL11.glBegin(GL11.GL_LINES);
			if (i + 1 < tapeMeasureBends.size()) {
				//Random rnd = new Random(ls.hashCode());
				//GL11.glColor3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
				LineSegment ls2 = tapeMeasureBends.get(i + 1);
				synchronized (ls2) {
					GL11.glColor3f(1,0,0);
					GL11.glVertex3f((float) ls.posX, (float) ls.posY, (float) ls.posT);
					GL11.glColor3f(0,0,0);
					GL11.glVertex3f((float) ls2.posX, (float) ls2.posY, (float) ls2.posT);

				}
			} else {
				//Random rnd = new Random(ls.hashCode());
				//GL11.glColor3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
				GL11.glColor3f(0,0,0);
				GL11.glVertex3f((float) ls.posX, (float) ls.posY, (float) ls.posT);
				GL11.glColor3f(1,1,1);
				GL11.glVertex3f((float) (ls.posX + ls.velX * 1000), (float) (ls.posY + ls.velY * 1000), (float) (ls.posT + 1000));
			}
			GL11.glEnd();
		}
	}
}
