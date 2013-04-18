package jge.entity;

import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import jge.behavior.ActionType;
import jge.behavior.Behaving;
import jge.behavior.Behavior;
import jge.group.Groupable;
import jge.render.*;
import jge.util.Util;
import jge.world.*;

public class Entity extends CoordinateObject implements Renderable, Behaving, Scaleable, Prioritizable, Groupable{

	private Image image;
	private Coordinates dim;
	private double scale = 1;
	protected HashMap<String, Behavior> behaviors = new HashMap<String, Behavior>();
	private Priority priority;

	public Entity(Coordinates pos, Coordinates dim, Image image){
		this(pos, dim, image, Priority.NORMAL);
	}
	
	public Entity(Coordinates pos, Coordinates dim, Image image, Priority p){
		super(pos);
		this.image = image;
		this.dim = dim;
		setPriority(p);
	}
	
	public Entity(Coordinates pos, Coordinates dim, String filePath, Priority p){
		super(pos);
		this.image = Util.imageFromPath(filePath);
		this.dim = dim;
		setPriority(p);
	}
	
	public Entity(Coordinates pos, Coordinates dim, String filePath){
		this(pos, dim, filePath, Priority.NORMAL);
	}

	public Entity(Coordinates pos, Coordinates dim, Image image, Priority p, Behavior...behaviors){
		super(pos);
		this.image = image;
		this.dim = dim;
		for(Behavior b : behaviors){
			addBehavior(b);
		}setPriority(p);
	}
	
	public Entity(Coordinates pos, Coordinates dim, Image image, Behavior...behaviors){
		this(pos, dim, image, Priority.NORMAL, behaviors);
	}
	
	public Entity(Coordinates pos, Coordinates dim, String filePath, Priority p, Behavior...behaviors){
		super(pos);
		this.image = Util.imageFromPath(filePath);
		this.dim = dim;
		for(Behavior b : behaviors){
			addBehavior(b);
		}setPriority(p);
	}
	
	public Entity(Coordinates pos, Coordinates dim, String filePath, Behavior...behaviors){
		this(pos, dim, filePath, Priority.NORMAL, behaviors);
	}

	@Override
	public void render(GraphicsWrapper g){
		Coordinates onScreen = getOwningWorld().getScreenPosition(getOwningWorld().makeWithinMapBounds(getPos()));
		Coordinates scaledDim = Coordinates.make(this.getDimentions()).multiply(this.getScale());
		onScreen = onScreen.subtract(Coordinates.make(scaledDim).multiply(0.5));
		//g.drawImage(image, (int)onScreen.getX(), (int)onScreen.getY(), null);
		g.drawImage(image, onScreen, scaledDim);
	}

	public void addBehavior(Behavior b){
		behaviors.put(b.getName(), b);
		b.action(ActionType.START, this);
	}

	public void removeBehavior(String name){
		if(!behaviors.containsKey(name)) throw new IllegalArgumentException(this.getClass().getName() + " does not contain a " + name + " object");
		Behavior b = behaviors.get(name);
		removeBehavior(b);
	}

	public void removeBehavior(Behavior b){
		if(b == null) throw new IllegalArgumentException(this.getClass().getName() + " does not contain a " + b + " object");
		behaviors.remove(b.getName());
		b.action(ActionType.END, this);
	}

	public void tickAllBehaviors(){
		actionRelevantBehaviors(ActionType.TICK);
	}

	public void actionRelevantBehaviors(ActionType type, Object additional){
		Iterator<Entry<String, Behavior>> i = behaviors.entrySet().iterator();
		while(i.hasNext()){
			i.next().getValue().action(type, additional, this);
		}
	}

	public void actionRelevantBehaviors(ActionType type){
		actionRelevantBehaviors(type, null);
	}
	
	public void destroy(){
		getOwningWorld().remove(this);
	}

	public void setDimentions(Coordinates dim){
		this.dim = dim;
	}

	public Coordinates getDimentions(){
		return dim;
	}

	public void setScale(double scale){
		this.scale = scale;
	}

	public double getScale(){
		return scale;
	}
	
	public void setPriority(Priority p){
		this.priority = p;
	}
	
	public Priority getPriority(){
		return priority;
	}
	
	@Override
	public String toString(){
		return "Entity at " + getPos() + " with " + behaviors.size() + " behavior(s) and priority of " + getPriority();
	}

}
