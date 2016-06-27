package uex.pablomuri.flowApp;

/**
 * Clase para el objeto EdgePropierties
 * @author pablomuri
 *
 */
public class EdgeProperties{

	private int level = 0;
	private long caudal = 0;
	
	public EdgeProperties(int level, long caudal) {
		this.level = level;
		this.caudal = caudal;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getCaudal() {
		return caudal;
	}
	public void setCaudal(long caudal) {
		this.caudal = caudal;
	}


}
