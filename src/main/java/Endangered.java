
public enum Endangered {

	NOTENDANGERED("notEndangered"),
	VULNERABLE("vulnerable"),
	ENDANGERED("endangered"),
	CRITICALLYENDANGERED("criticallyEndangered"),
	EXTINCT("extinct");
	
	
	private String name;
	
	private Endangered(String s) {
		this.name = s;
	}
}
