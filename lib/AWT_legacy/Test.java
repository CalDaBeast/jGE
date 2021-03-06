package com.cal.test;
import java.awt.Color;
import java.util.Random;
import jge.behavior.*;
import jge.behavior.prefab.MoveToMouseOnClick;
import jge.entity.*;
import jge.input.AWTMouseHandler;
import jge.render.*;
import jge.render.awt.RenderAWT;
import jge.render.RenderGL;
import jge.world.*;

public class Test{

	public Test(){}

	public static void main(String[] args){
		RenderGL render = new RenderGL(800,600,60);
		World w = new World(800, 600);
		render.setRenderingWorld(w);
		Shape s = new Shape(Coordinates.make(100, 100), Coordinates.make(50,50), ShapeType.OVAL, Color.red);
		s.addBehavior(new MoveToMouseOnClick());
		System.out.println(s);
		w.add(s);
		render.start();
	}
	
	@SuppressWarnings("deprecation")
	public static void main3(String[] args){
		RenderGL render = new RenderGL(800, 600, 60);
		World w = new World(800, 450);
		render.setRenderingWorld(w);
		Behavior b = new Behavior("randommotion"){
			@Action
			public void onTick(Behaving b){
				if(b instanceof CoordinateObject){
					CoordinateObject object = (CoordinateObject) b;
					object.getPos().add((new Random()).nextInt(4) - 1.5);
				}
			}
		};
		Shape s2 = new Shape(Coordinates.make(-30, 30), Coordinates.make(50, 25), ShapeType.RECTANGLE, Color.GREEN, Priority.HIGHEST);
		Shape s3 = new Shape(Coordinates.make(30, 30), Coordinates.make(50, 25), ShapeType.OVAL, Color.MAGENTA);
		Shape s4 = new Shape(Coordinates.make(30, -30), Coordinates.make(50, 25), ShapeType.OVAL, Color.RED, Priority.LOWEST);
		GroupEntity group = new GroupEntity(w, Coordinates.make(200, 200), s2, s3, s4);
		for(Entity ent : group.getGroupObject().getObjects()){
			ent.addBehavior(b);
		}
		group.addBehavior(new MoveToMouseOnClick());
		w.add(group);
		w.getTickManager().startNewTickThread();
		render.start();
	}
	
	public static void main2(String[] args){
		jge.behavior.Behavior b = (new jge.behavior.Behavior("test"){
			@Action
			public void onTick(Behaving b){
				Entity e = (Entity) b;
				if(((AWTMouseHandler)e.getOwningWorld().getRenderer().getMouseHandler()).leftDown){
					e.updatePos(e.getOwningWorld().getRenderer().getMousePos());
				}
			}
		});
		Entity e = new Entity(Coordinates.make(90,90), Coordinates.make(50, 50), "images.jpg");
		e.setScale(5);
		Shape s = new Shape(Coordinates.make(30, 30), Coordinates.make(100, 100), ShapeType.OVAL, Color.DARK_GRAY, Priority.HIGHEST);
		e.addBehavior(b);
		Shape s2 = new Shape(Coordinates.make(300, 300), Coordinates.make(150, 100), ShapeType.RECTANGLE, Color.GREEN, Priority.HIGHEST);
		Shape s3 = new Shape(Coordinates.make(300, 30), Coordinates.make(50, 25), ShapeType.OVAL, Color.MAGENTA);
		Shape s4 = new Shape(Coordinates.make(30, 300), Coordinates.make(50, 25), ShapeType.OVAL, Color.RED, Priority.LOWEST);
		RenderAWT render = (RenderAWT) Screen.addWindow("shapes", 800, 450);
		World world = new World(800, 450, 1);
		render.setRenderingWorld(world);
		world.add(e);
		world.add(s2);
		world.add(s3);
		world.add(s4);
		world.add(s);
		world.getTickManager().startNewTickThread();
		render.startRendering();
	}
}
