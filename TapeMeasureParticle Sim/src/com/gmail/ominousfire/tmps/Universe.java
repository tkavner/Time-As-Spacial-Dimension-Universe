package com.gmail.ominousfire.tmps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.gmail.ominousfire.tmps.render.MovementHelper;
import com.gmail.ominousfire.tmps.render.Renderer;

public class Universe implements Runnable {
	
	Renderer renderer;
	public List<TapeMeasureParticle> particlesInUniverse;
	Random rand;
	Scanner scanner;
	public boolean isShuttingDown = false;
	
	public static int numParticles;
	public static final double MAX_X_START = .2;
	public static final double MAX_Y_START = .2;
	public static final double MAX_VEL_X_START = 1;
	public static final double MAX_VEL_Y_START = 1;
	
	Universe() {
		particlesInUniverse = new ArrayList<TapeMeasureParticle>();
		rand = new Random();
		scanner = new Scanner(System.in);
		System.out.println("Number of particles to simulate?");
		numParticles = scanner.nextInt();
		initilizeParticles();
		
		(new Thread(this, "Universe Main")).start();
		
		renderer = new Renderer(this);
	}
	
	void initilizeParticles() {
		double d;
		double d1;
		double d2;
		double d3;
		for (int i = 0; i < numParticles; i++) {
			d = rand.nextDouble() * MAX_X_START * 2 - MAX_X_START;
			d1 = rand.nextDouble() * MAX_Y_START * 2 - MAX_Y_START;
			d2 = rand.nextDouble() * MAX_VEL_X_START * 2 - MAX_VEL_X_START;
			d3 = rand.nextDouble() * MAX_VEL_Y_START * 2 - MAX_VEL_Y_START;
			LineSegment ls = new LineSegment(d, d1, d2, d3, 0);
			particlesInUniverse.add(new TapeMeasureParticle(ls));
		}
	}

	public static void main(String[] args) {
		new Universe();
	}

	@Override
	public void run() {
		while (!isShuttingDown) {
			try {
				checkForCollisions();
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		}
	}

	private void checkForCollisions() {
		for(int i = 0; i < particlesInUniverse.size(); i++) {
			TapeMeasureParticle tmp1 = particlesInUniverse.get(i);
			for(int j = 0; j < particlesInUniverse.size(); j++) {
				TapeMeasureParticle tmp2 = particlesInUniverse.get(j);
				CollisionPoint cp = tmp1.checkForCollisionAndInteract(tmp2);
				if (cp != null) {
					cp.startCoil();
					cp.addNewSegments();
					while(MovementHelper.wait){}
				}
			}
		}
	}

	void checkInput() {
		
	}

	public void shutdown() {
		isShuttingDown = true;
	}

}
