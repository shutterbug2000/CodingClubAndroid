package enemies;

public interface Enemy {
	
	void setup();
	void updateState();
	
	void attackPrime();
	void attackSpecial(float power);
	
	void die();

}
