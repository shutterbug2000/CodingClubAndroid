package casses.dragon;

public enum Eatable {
	
	Imposible(1000000000),
	Hardcore(5000),
	Hard(1000),
	Difficult(100),
	Mediocre(10),
	Medium(3),
	Easy(1),
	Crappy(0);
	
	private static Eatable def = Crappy;
	private float level;
	
	Eatable(float level) {
		this.level = level;
	}
	
	public static Eatable getLevel(float amount) {
		for(Eatable eatable : Eatable.values()) {
			if(eatable.level <= amount) return eatable;
		}
		return def;
	}

}
