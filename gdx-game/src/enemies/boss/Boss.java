package enemies.boss;

public interface Boss {
	
	void hit(byte damage);
	void death();
	
	byte primeAttack();
	byte secondaryAttack();
	byte finishingAttack();
	
	

}
