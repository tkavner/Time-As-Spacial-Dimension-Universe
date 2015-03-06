package com.gmail.ominousfire.tmps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollisionPoint {
	
	/**
	 * Used so that we can get complex interactions, it is a master list of all collisions
	 */
	static Map<LineSegment, CollisionPoint> lineSegments = new HashMap<LineSegment, CollisionPoint>();
	List<LineSegment> segments;
	double posT;

	CollisionPoint(double posT, LineSegment... ls1) {
		segments = new ArrayList<LineSegment>(); //initilize variables
		this.posT = posT;
		for(int i = 0; i < ls1.length; i++) { //start adding our points
			segments.add(ls1[i]);
		}
	}
	
	CollisionPoint register() {
		CollisionPoint otherPoint = null;
		for (LineSegment ls : segments) {
			if (lineSegments.get(ls) != null) {
				otherPoint = lineSegments.get(ls);
				break;
			}
		}
		if (otherPoint == null) {
			for (LineSegment ls : segments) {
				lineSegments.put(ls, this);
			}
			return this;
		}
		for (LineSegment ls : segments) if (!otherPoint.segments.contains(ls)) otherPoint.segments.add(ls);
		return otherPoint;
	}

	public void coil() {
		for (LineSegment ls: segments) {
			lineSegments.remove(ls);
			TapeMeasureParticle tmp = ls.isPartOf;
			tmp.removeHigherSegments(ls);
		}
	}
	
	public void startCoil() {
		for (LineSegment ls: segments) {
			TapeMeasureParticle tmp = ls.isPartOf;
			tmp.removeHigherSegments(ls);
		}
	}

	static void remove(LineSegment lineSegment) {
		CollisionPoint cp = lineSegments.get(lineSegment);
		if (cp == null) return;
		cp.coil();
	}

	public void addNewSegments() {
		// TODO Finish
		for (LineSegment ls: segments) {
			TapeMeasureParticle tmp = ls.isPartOf;
			//Find new segment's velX and velY
			double newVelX = ls.velX;
			double newVelY = ls.velY;
			for (LineSegment ls1: segments) {
				if (ls != ls1) {
					newVelX += (ls1.velX / 4);
					newVelY += (ls1.velY / 4);
				}
			}
			//Find new segment's posX, posY (posT = this.posT)
			double newPosX = ls.posX + this.posT * ls.velX;
			double newPosY = ls.posY + this.posT * ls.velY;
			LineSegment newSegment = new LineSegment(newPosX, newPosY, newVelX, newVelY, this.posT);
			tmp.addNewSegment(newSegment);
		}
	}

}
