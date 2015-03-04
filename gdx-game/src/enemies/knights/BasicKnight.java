package enemies.knights;

import enemies.Enemy;

public class BasicKnight implements Enemy {
	
	byte health;

	public void setup() {
		// TODO Auto-generated method stub

	}
	
	public void updateState() {
		
		if (health <= 0) {
			die();
		}
		
	}

	
	public void attackPrime() {
		// TODO Auto-generated method stub

	}

	public void attackSpecial(float power) {
		// TODO Auto-generated method stub

	}
	
	public void takeDamage(byte amount) {
		health -= amount;
	}

	
	public void die() {
		// TODO Auto-generated method stub

	}

}
