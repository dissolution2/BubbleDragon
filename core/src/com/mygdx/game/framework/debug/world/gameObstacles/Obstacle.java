package com.mygdx.game.framework.debug.world.gameObstacles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
//import com.mygdx.game.Utils.GameConstants;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.GameUtility;

public class Obstacle  extends Sprite {

	protected World world;
	protected PlayScreen screen;

	public Body body;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected PolygonShape polygonShape;
	protected Rectangle rectangle;
	public float posX, posY, width, height, angle;
	public int associationNumber;
	public boolean active;
	
	//Graphs
	protected NinePatch ninePatch;
	protected String stringTextureRegion;
	
	//Sound
	protected Sound sound;
	
	//Box2DLight
	protected short categoryBits = 0001;
	
	//public Obstacle(NameGame game, World world, OrthographicCamera camera, MapObject rectangleObject){
	//}
	
	//public Obstacle(World world, MapObject rectangleObject){
	//}

	public Obstacle(PlayScreen screen, float x, float y, MapObject rectangleObject){

	}

	//public Obstacle(NameGame game, World world, OrthographicCamera camera, PolylineMapObject polylineObject){
	/*
	public Obstacle(World world, PolylineMapObject polylineObject){
		setInitialState(polylineObject);
	}
	*/

	public Obstacle(PlayScreen screen, float x, float y, PolylineMapObject polylineObject){
			setInitialState(polylineObject);
	}

	//public void create(World world, OrthographicCamera camera, MapObject rectangleObject){
	public void create(PlayScreen screen, float x, float y, MapObject rectangleObject){
		setInitialState(rectangleObject);

		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		
		stringTextureRegion = "WhiteSquare";
		
		rectangle = ((RectangleMapObject) rectangleObject).getRectangle();
			
		this.posX = ( (rectangle.x + rectangle.width/2) * GameUtility.MPP) * 0.2f ; //GameConstants.MPP;

		this.posY = ( (rectangle.y + rectangle.height/2) * GameUtility.MPP) * 0.2f; //GameConstants.MPP;
		this.width = (rectangle.width/2) * GameUtility.MPP; //GameConstants.MPP;
		this.height = (rectangle.height/2) * GameUtility.MPP; //GameConstants.MPP;
/*
		System.out.println("Obstacle Class: rectangle.x " + rectangle.x );
		System.out.println("Obstacle Class: rectangle.height/2 " + rectangle.height/2 );
		System.out.println("Obstacle Class: GameUtility.MPP " + GameUtility.MPP );

		System.out.println("Obstacle Class: width " + width );
		System.out.println("Obstacle Class: height " + height );

		System.out.println("Obstacle Class: posX " + posX);
		System.out.println("Obstacle Class: getX() :" + x);
		System.out.println("Obstacle Class: posY " + posY);
		System.out.println("Obstacle Class: getY() :" + y);
*/

		/*
		Obstacle Class: rectangle.x 223.5
		Obstacle Class: rectangle.height/2 35.0
		Obstacle Class: GameUtility.MPP 0.05
		Obstacle Class: width 0.32500002
		Obstacle Class: height 1.75
		Obstacle Class: posX 2.3
		Obstacle Class: getX() :223.5
		Obstacle Class: posY 2.2350001
		Obstacle Class: getY() :188.5
		 */

		if(rectangleObject.getProperties().get("rotation") != null)
			this.angle = -Float.parseFloat(rectangleObject.getProperties().get("rotation").toString())*MathUtils.degreesToRadians;
		
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(width/4, height/4); // Added /4

		bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(posX, posY)); // 9.275f,6.665f)); //x,y)); //
    	bodyDef.type = getBodyType();
		body = world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        //fixtureDef.density = (float)(GameConstants.DENSITY/(width * height));
        fixtureDef.friction = 0.5f;  
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = GameUtility.GAME_OBSTACLE_BIT; //categoryBits;
   
        body.createFixture(fixtureDef).setUserData("Obstacle");
        body.setUserData("Obstacle");
        body.setFixedRotation(false);
        
        if(rectangleObject.getProperties().get("rotation") != null){
            /*
             * To obtain x' et y' positions from x et y positions after a rotation of an angle A
             * around the origine (0, 0) :
             * x' = x*cos(A) - y*sin(A)
             * y' = x*sin(A) + y*cos(A)
             */
        	float Xrotation = (float)(body.getPosition().x - width + width * Math.cos(angle) + height * Math.sin(angle));
        	float Yrotation = (float)(width * Math.sin(angle) + body.getPosition().y + height - height * Math.cos(angle));
        	body.setTransform(Xrotation, Yrotation, this.angle);
        }
        
        polygonShape.dispose();  
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return posX;
	}
	
	public float getY(){
		return posY;
	}
	
	public BodyType getBodyType(){
		return BodyType.StaticBody;
	}

	public void setX( float X){
		posX = X;
	}

	public void setY( float Y){
		posY = Y;
	}
	
	public void setInitialState(MapObject mapObject){
		//Association Number
		if(mapObject.getProperties().get("Association Number") != null){
			associationNumber = Integer.parseInt((String) mapObject.getProperties().get("Association Number"));
		}
		
		//Is the Obstacle active ?
		if(mapObject.getProperties().get("Active") != null){
			if(Integer.parseInt((String) mapObject.getProperties().get("Active")) == 1)
				active = true;
			else 
				active = false;
		}
		else
			active = true;		
	}

	public void active(BubblePlayer player) {//Hero hero){
	}

	public void update(float dt) {}

	public void activateOpen(){}
	public void activateClose(){}

	public void activateOpenAfterBossDeath(){}

/*
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){		
		batch.setColor(1, 1, 1, 1);

		System.out.println("Obstacle Class draw");

		batch.draw(textureAtlas.findRegion(stringTextureRegion), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				width,
				height,
				2 * width,
				2 * height,
				1,
				1,
				body.getAngle()*MathUtils.radiansToDegrees);
	}

	public void draw(SpriteBatch batch){
		batch.setColor(1, 1, 1, 1);

		System.out.println("Obstacle Class draw");
		ninePatch.draw(batch, 
						this.body.getPosition().x - width,
						this.body.getPosition().y - height, 
						2 * width, 
						2 * height);
	}
*/
	public void soundPause(){
		if(sound != null)
			sound.pause();
	}
	
	public void soundResume(){
		if(sound != null)
			sound.resume();
	}
	
	public void dispose(){	
	}
}
