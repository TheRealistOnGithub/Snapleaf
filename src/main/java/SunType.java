
public enum SunType {
	
	FULLSHADE("fullShade"),
	PARTSHADE("partShade"),
	FULLSUN("fullSun");
	
	
	private String name;
	
	private SunType(String s) {
		this.name = s;
	}
}
