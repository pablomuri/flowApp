package uex.pablomuri.flowApp;



import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * Clase que permite cargar la configuracion a partir de un fichero
 * @author pablomuri
 *
 */
public class Configuration {

	private String monPath = "./";
	private boolean monitor = true;
	private short idleTimeOut = 15;
	private short hardTimeOut = 7200;

	private int refreshTimeMonitor = 10;
	
	private int level1Troughtput = 12500000;
	
	private int eStandby = 49;
	private int eLevel1 = 315;
	private int eLevel2 = 619;

	private long standbyCost = 50;
	private long level1Cost = 15;
	private long level2Cost = 1;
 
	/**
	 * Constructor de la clase
	 */
	public Configuration() {
    	load();
    }

	/**
	 * Carga la configuracion a partir de un fichero
	 */
    public void load() {

        StringTokenizer Tok;

        try {
            FileReader fr = new FileReader("./flowapp.conf");
            BufferedReader entrada = new BufferedReader(fr);
            String s;
            String s1;

            while ((s = entrada.readLine()) != null) {//Procesamos la linea
                System.out.println(s);
                if (s.length() == 0) {
                } else if (s.charAt(0) == '@') { //comando
                    s = s.substring(1);
                    Tok = new StringTokenizer(s);
                    s1 = Tok.nextToken();
                    if (s1.equalsIgnoreCase("IDLETIMEOUT")) {
                        idleTimeOut = (short) Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("HARDTIMEOUT")) {
                    	hardTimeOut = (short) Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("REFRESHTIME")) {
                        refreshTimeMonitor = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("LEVEL1")) {
                       	level1Troughtput = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("ESTANDBY")) {
                        eStandby = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("ELEVEL1")) {
                       	eLevel1= Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("ELEVEL2")) {
                        eLevel2 = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("STANDBYCOST")) {
                        standbyCost = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("LEVEL1COST")) {
                    	level1Cost= Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("LEVEL2COST")) {
                        level2Cost = Integer.parseInt(Tok.nextToken());
                    } else if (s1.equalsIgnoreCase("MONITOR")) {
                        String m = Tok.nextToken();
                        if(m.equalsIgnoreCase("true")){
                        	monitor = true;
                        } else if (m.equalsIgnoreCase("false")){
                        	monitor = false;
                        }
                    } else if (s1.equalsIgnoreCase("MONPATH")) {
                        monPath = Tok.nextToken();
                    } else { //error de entrada
                        System.out.println("Error de entrada " + s);
                        return; //salimos sin modificar nada
                    }
                }
            }//end del while
            entrada.close();
        }//end del try
        catch (java.io.FileNotFoundException fnfex) {
            System.out.println("Archivo no encontrado: " + fnfex);
        } catch (java.io.IOException ioex) {
        }


    }//end del metodo

    


	@Override
	public String toString() {
		return "Configuration [monPath=" + monPath + ", monitor=" + monitor
				+ ", idleTimeOut=" + idleTimeOut + ", hardTimeOut="
				+ hardTimeOut + ", refreshTimeMonitor=" + refreshTimeMonitor
				+ ", level1Troughtput=" + level1Troughtput + ", eStandby="
				+ eStandby + ", eLevel1=" + eLevel1 + ", eLevel2=" + eLevel2
				+ ", standbyCost=" + standbyCost + ", level1Cost=" + level1Cost
				+ ", level2Cost=" + level2Cost + "]";
	}

	public String getMonPath() {
		return monPath;
	}

	public boolean isMonitor() {
		return monitor;
	}

	public short getIdleTimeOut() {
		return idleTimeOut;
	}

	public short getHardTimeOut() {
		return hardTimeOut;
	}

	public int getRefreshTimeMonitor() {
		return refreshTimeMonitor;
	}

	public int getLevel1Troughtput() {
		return level1Troughtput;
	}

	public int geteStandby() {
		return eStandby;
	}

	public int geteLevel1() {
		return eLevel1;
	}

	public int geteLevel2() {
		return eLevel2;
	}

	public long getStandbyCost() {
		return standbyCost;
	}

	public long getLevel1Cost() {
		return level1Cost;
	}

	public long getLevel2Cost() {
		return level2Cost;
	}

    
}