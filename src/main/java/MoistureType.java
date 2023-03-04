
public enum MoistureType {
	
	WET("wet"),
	DRY("dry"),
	AVERAGE("average");
	
	
	private String name;
	
	private MoistureType(String s) {
		this.name = s;
	}
}
