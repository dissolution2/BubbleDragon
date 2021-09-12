package com.mygdx.game.framework.debug.world.gameObstacles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.GameUtility;

public class ObstacleDoor extends Obstacle{

	private enum DoorState{

		OPEN,
		CLOSE

	}
	private DoorState currentDoorState, prevDoorState;

	private float speed = 5;
	private float doorAngle, doorScale;
	private Vector2 initialPosition, finalPosition;

	private TextureRegion doorTextureNormal, doorTextureMedium, doorTextureLarge ;

	private boolean booActivDoorOpenIsFinished;
	private boolean booActivDoorCloseIsFinished;
	private boolean activateOpenAfterBossDeathBool;

private String doorSize;
//private char swithDoorSize;

	public ObstacleDoor(PlayScreen screen, float x, float y, MapObject rectangleObject) {
		super(screen, x, y, rectangleObject);
		create(screen, x, y, rectangleObject);


		doorTextureNormal = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas4.atlas").findRegion("Door"), 0, 0, 23, 115);
		doorTextureMedium = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas6.atlas").findRegion("Door"), 0, 0, 25, 176);
		doorTextureLarge = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas5.atlas").findRegion("Door"), 0, 0, 25, 258);

		stringTextureRegion = "Door";
		doorScale = 1;
		
		//Motion speed

		if(rectangleObject.getProperties().get("Size") != null){
			doorSize =  (String)rectangleObject.getProperties().get("Size");
			//swithDoorSize = doorSize.charAt(0);
		}

		if(rectangleObject.getProperties().get("Speed") != null){
			speed = Float.parseFloat((String) rectangleObject.getProperties().get("Speed"));
		}
		
		initialPosition = new Vector2(posX, posY);
		
		if(width > height){
			finalPosition = new Vector2(posX + Math.signum(speed) * 1.9f*width, posY);
			doorAngle = 0;
			//System.out.println("DoorAngel 0");
		}
		else{
			finalPosition = new Vector2(posX, posY + Math.signum(speed) * 1.9f*height);
			doorAngle = 90;
			doorScale = height/width;
			//System.out.println("DoorAngel 90");
		}
/*
		//setBounds(finalPosition.x, finalPosition.y, 23f / GameUtility.PPM, 115 / GameUtility.PPM); // ???
		if(height < 1.76f ) {
			//setBounds(posX, posY, 23f / 1.3f / GameUtility.PPM, 115 / 1.3f / GameUtility.PPM); // N
			setBounds(posX, posY, 30f / GameUtility.PPM, 115 / 1.3f / GameUtility.PPM); // N
		}else {
			if(height > 1.85f && !(height > 4f ) ) {
				setBounds(posX, posY, 30f / GameUtility.PPM, 176 / 1.12f / GameUtility.PPM); // M
			}else {
				setBounds(posX, posY, 30f / GameUtility.PPM, 258 / 1.08f / GameUtility.PPM); // L
			}
		}
*/

		switch (doorSize.charAt(0)){
			case 'N':
				setBounds(posX, posY, 30f / GameUtility.PPM, 115 / 1.3f / GameUtility.PPM); // N
				break;
			case 'M':
				setBounds(posX, posY, 30f / GameUtility.PPM, 176 / 1.12f / GameUtility.PPM); // M
				break;
			case 'L':
				setBounds(posX, posY, 30f / GameUtility.PPM, 258 / 1.08f / GameUtility.PPM); // L
				break;
		}
/*
		if(rectangleObject.getProperties().get("Size").equals("NORMAL")){
			//System.out.println("Normal Door");
			setBounds(posX, posY, 30f / GameUtility.PPM, 115 / 1.3f / GameUtility.PPM); // N
		}
		if(rectangleObject.getProperties().get("Size").equals("MEDIUM")){
			//System.out.println("Medium Door");
			setBounds(posX, posY, 30f / GameUtility.PPM, 176 / 1.12f / GameUtility.PPM); // M
		}
		if(rectangleObject.getProperties().get("Size").equals("LARGE")){
			//System.out.println("Large Door");
			setBounds(posX, posY, 30f / GameUtility.PPM, 258 / 1.08f / GameUtility.PPM); // L
		}
*/

	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}
	
	@Override
	public void active(BubblePlayer player) {//Hero hero){
		//System.out.println("Door Close " + body.getLinearVelocity());
		if(active) {
			//System.out.println("Door Close " + body.getLinearVelocity());
			body.setLinearVelocity(Math.signum(0.2f) * (initialPosition.x - body.getPosition().x) * 0.2f,
					Math.signum(0.2f) * (initialPosition.y - body.getPosition().y) * 0.2f
			);
		}else {
			//System.out.println("Door Opens " + body.getLinearVelocity());
			body.setLinearVelocity(Math.signum(0.2f) * (finalPosition.x - body.getPosition().x) * 0.2f,
					Math.signum(0.2f) * (finalPosition.y - body.getPosition().y) * 0.2f
			);
		}
	}

	@Override
	public void update(float dt){


		/*
System.out.println("doorScale " + doorScale);

		System.out.println("Door Open Var's");
		System.out.println("ObstacleDoor class Math.signum(Speed) " + Math.signum(speed));
		System.out.println("ObstacleDoor class finalPo(X) - body.pos(x) " + (finalPosition.x - body.getPosition().x));
		System.out.println("ObstacleDoor class speed " + speed);
		System.out.println("ObstacleDoor class finalPo(Y) - body.pos(y) " + (finalPosition.y - body.getPosition().y));
		System.out.println("X: " + (Math.signum(speed) * (finalPosition.x - body.getPosition().x) * speed) );
		System.out.println("Y: " + (Math.signum(speed) * (finalPosition.y - body.getPosition().y) * speed));

		System.out.println("Door Close Var's");
		System.out.println("ObstacleDoor class Math.signum(Speed) " + Math.signum(speed));
		System.out.println("ObstacleDoor class finalPo(X) - body.pos(x) " + (finalPosition.x - body.getPosition().x));
		System.out.println("ObstacleDoor class speed " + speed);
		System.out.println("ObstacleDoor class finalPo(Y) - body.pos(y) " + (finalPosition.y - body.getPosition().y));
		System.out.println("X: " + (Math.signum(speed) * (initialPosition.x - body.getPosition().x) * speed) );
		System.out.println("Y: " + (Math.signum(speed) * (initialPosition.y - body.getPosition().y) * speed));
*/
		/*
		Door Open Var's
		ObstacleDoor class Math.signum(Speed) 1.0
		ObstacleDoor class finalPo(X) - body.pos(x) 0.0
		ObstacleDoor class speed 5.0
		ObstacleDoor class finalPo(Y) - body.pos(y) 3.3249998
		X: 0.0
		Y: 16.625

		Door Close Var's
		ObstacleDoor class Math.signum(Speed) 1.0
		ObstacleDoor class finalPo(X) - body.pos(x) 0.0
		ObstacleDoor class speed 5.0
		ObstacleDoor class finalPo(Y) - body.pos(y) 3.3249998
		X: 0.0
		Y: 0.0

		// new Algoritem!!! activateOpen
		body.getPosition().y > finalPosition.y - (body.getPosition().cpy().y / 2)

		Door Open Var's
		ObstacleDoor class Math.signum(Speed) 1.0
		ObstacleDoor class finalPo(X) - body.pos(x) 0.0
		ObstacleDoor class speed 5.0
		ObstacleDoor class finalPo(Y) - body.pos(y) 1.8529971
		X: 0.0
		Y: 9.264985
		Door Close Var's
		ObstacleDoor class Math.signum(Speed) 1.0
		ObstacleDoor class finalPo(X) - body.pos(x) 0.0
		ObstacleDoor class speed 5.0
		ObstacleDoor class finalPo(Y) - body.pos(y) 1.8529971
		X: 0.0
		Y: -7.360016
		 */

		setPosition(body.getPosition().x - getWidth() / 4, body.getPosition().y - getHeight() / 4);
		setRegion(getFrame(dt));
	}

	public TextureRegion getFrame(float dt) {

		switch (doorSize.charAt(0)) {
			case 'N':
				return doorTextureNormal;
			//break;
			case 'M':
				return doorTextureMedium;
			//break;
			case 'L':
				return doorTextureLarge;
			//break;
			default:
				return doorTextureNormal;
			//break;
		}
	}


	@Override
	public void activateOpenAfterBossDeath(){
		if(!activateOpenAfterBossDeathBool) {
			System.out.println("ObstacleDoor Class Activate!!! Open Door Boss is dead " + height);
			System.out.println("body.getPosition().y " + body.getPosition().y );
			System.out.println("initialPosition.y + 2.1 " + (initialPosition.y + 2.1) );
			/* On moved door up !!
			bstacleDoor Class Activate!!! Open Door Boss is dead height = 4.7875
			body.getPosition().y = 4.1425014
			initialPosition.y + 2.1 = 6.242499923706054
			 */

			/* door usual place start value

			ObstacleDoor Class Activate!!! Open Door Boss is dead height = 4.7875
			body.getPosition().y = 2.9087052
			initialPosition.y + 2.1 = 4.982500171661377

			end value

			ObstacleDoor Class Activate!!! Open Door Boss is dead height = 4.7875
			body.getPosition().y = 4.9884706
			initialPosition.y + 2.1 = 4.982500171661377
			ObstacleDoor Class Activate!!! Open Door Boss is dead finished!!
*/
			if (height > 4.42f) {
				//System.out.println("height Large: " + height );
				if (body.getPosition().y > (initialPosition.y + 2.1)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					activateOpenAfterBossDeathBool = true;
					System.out.println("ObstacleDoor Class Activate!!! Open Door Boss is dead finished!!");
					//System.out.println("Open Door L - Pos.y: " + body.getPosition().y +
					//		" initPos.y(+2.1) " + (initialPosition.y + 2.1) );
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
					//System.out.println("Open LinVel: " + body.getLinearVelocity() );
				}
			}
		}

		if(!activateOpenAfterBossDeathBool) {
			if (height > 1.85f && !(height > 4f)) {
				//System.out.println("height Big: " + height );
				if (body.getPosition().y > (initialPosition.y + 1.2)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					activateOpenAfterBossDeathBool = true;
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
				}
			}
		}

		if(!activateOpenAfterBossDeathBool) {
			if (height < 1.76f) {
				//System.out.println("height Small: " + height );
				if (body.getPosition().y > (initialPosition.y + 0.7)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					activateOpenAfterBossDeathBool = true;
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
				}
			}
		}



	}

	@Override
	public void activateClose(){
		//System.out.println("ObstacleDoor Class Activate!!! CloseDoor, body posY: " + body.getPosition().y);
		if(!booActivDoorCloseIsFinished) {
			//System.out.println("ObstacleDoor Class Activate!!! Close Door");
			if (body.getPosition().y > 2.91f) {
				if (body.getPosition().y > (initialPosition.y - 0.7)) {//7.3f) { // all ways true
					body.setLinearVelocity(Math.signum(9f) * (initialPosition.x - body.getPosition().x) * 9f,
							Math.signum(9f) * (initialPosition.y - body.getPosition().y) * 9f
					);
					//System.out.println("Close Door L - Pos.y: " + body.getPosition().y +
					//		" initPos.y(-0.7) " + (initialPosition.y - 0.7));
					//System.out.println("Close LinVel: " + body.getLinearVelocity());
				} else {
					body.setLinearVelocity(0f, 0f);
					booActivDoorCloseIsFinished = true;
					//System.out.println("Active Close Setting LinVel: 0");
				}
			} else {
				// door is closed sett all to 0 !??
				System.out.println("Door should be closed no action should be taken!!!");
				body.setLinearVelocity(0f, 0f);
				booActivDoorCloseIsFinished = true;
			}
		}
	}

	//float testPoint = initialPosition.cpy().y * 2;
	@Override
	public void activateOpen() {

		//System.out.println("ObstacleDoor Class Activate!!! OpenDoor, body posY: " + body.getPosition().y +
		//		" FINAL POSITION.Y: " + finalPosition.y);

		//System.out.println("how will this be body.getPosY: " + body.getPosition().y +
		//		" finalPos: " + finalPosition.y  + " initialPosition.y " + initialPosition.y );

		//System.out.println("height: " + height );
		//System.out.println("Door Size is: " + doorSize.charAt(0) );
		if(!booActivDoorOpenIsFinished) {
			//System.out.println("ObstacleDoor Class Activate!!! Open Door");
			if (height > 4.42f) {
				//System.out.println("height Large: " + height );
				if (body.getPosition().y > (initialPosition.y + 2.1)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					booActivDoorOpenIsFinished = true;
					//System.out.println("Open Door L - Pos.y: " + body.getPosition().y +
					//		" initPos.y(+2.1) " + (initialPosition.y + 2.1) );
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
					//System.out.println("Open LinVel: " + body.getLinearVelocity() );
				}
			}
		}
		if(!booActivDoorOpenIsFinished) {
			if (height > 1.85f && !(height > 4f)) {
				//System.out.println("height Big: " + height );
				if (body.getPosition().y > (initialPosition.y + 1.2)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					booActivDoorOpenIsFinished = true;
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
				}
			}
		}

		if(!booActivDoorOpenIsFinished) {
			if (height < 1.76f) {
				//System.out.println("height Small: " + height );
				if (body.getPosition().y > (initialPosition.y + 0.7)) {//11.1537508f) ) {//8.f ) {
					body.setLinearVelocity(0f, 0f);
					booActivDoorOpenIsFinished = true;
				} else {
					body.setLinearVelocity(Math.signum(-0.2f) * (finalPosition.x - body.getPosition().x) * -0.2f,
							Math.signum(-0.2f) * (finalPosition.y - body.getPosition().y) * -0.2f
					);
				}
			}
		}


	}


	public boolean getIsBooActivDoorOpenIsFinished(){return this.booActivDoorOpenIsFinished; }
	public void setIsBooActivDoorOpenIsFinished(boolean value){ this.booActivDoorOpenIsFinished = value; }

	public boolean getIsBooActivDoorCloseIsFinished(){ return  this.booActivDoorCloseIsFinished; }
	public void setIsBooActivDoorCloseIsFinished(boolean value){ this.booActivDoorCloseIsFinished = value; }



	public void draw(Batch batch){
		//if(!destroyed || stateTimer < 4) //1
		super.draw(batch);
	}
}
