package com.gmail.ominousfire.tmps;

public class LineSegment {
	
	TapeMeasureParticle isPartOf;
	double lineLocation;
	double velX;
	double velY;
	double posX;
	double posY;
	double posT;
	
	LineSegment(double posX, double posY, double velX, double velY, double posT) {
		this.posX = posX;
		this.posY = posY;
		this.velX = velX;
		this.velY = velY;
		this.posT = posT;
		this.lineLocation = 0;
		this.isPartOf = null;
	}
	
	LineSegment(double posX, double posY, double velX, double velY, double posT, double lineLocation) {
		this(posX, posY, velX, velY, posT);
		this.lineLocation = lineLocation;
	}
}
