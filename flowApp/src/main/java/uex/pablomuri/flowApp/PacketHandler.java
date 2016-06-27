package uex.pablomuri.flowApp;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import javax.swing.Timer;

import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.action.Output;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.Edge;
import org.opendaylight.controller.sal.core.Host;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.IPv4;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.opendaylight.controller.sal.utils.Status;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 * Clase que implementa IListenDataPacket, maneja los packet-in
 * 
 * @author pablomuri
 *
 */
public class PacketHandler implements IListenDataPacket {

	private static final Logger log = LoggerFactory
			.getLogger(PacketHandler.class);

	private IDataPacketService dataPacketService;
	private ISwitchManager switchManager;
	private IFlowProgrammerService flowProgrammerService;
	private IStatisticsManager statisticsManager;
	private ITopologyManager topologyManager;

	Configuration conf = new Configuration();

	private short idleTimeOut = conf.getIdleTimeOut();
	private short hardTimeOut = conf.getHardTimeOut();

	private int refreshTimeMonitor = conf.getRefreshTimeMonitor();

	private int level1Troughtput = conf.getLevel1Troughtput();

	private int eStandby = conf.geteStandby();
	private int eLevel1 = conf.geteLevel1();
	private int eLevel2 = conf.geteLevel2();

	private long standbyCost = conf.getStandbyCost();
	private long level1Cost = conf.getLevel1Cost();
	private long level2Cost = conf.getLevel2Cost();

	private boolean monitor = conf.isMonitor();

	//Mapas con los edge en nivel 1 y 2 y standby
	private Map<Edge, EdgeProperties> level1Edge = new HashMap<Edge, EdgeProperties>();
	private Map<Edge, EdgeProperties> level2Edge = new HashMap<Edge, EdgeProperties>();
	private Map<Edge, EdgeProperties> standByEdge = new HashMap<Edge, EdgeProperties>();
	
	//Mapa que contiene todos los edges y sus propiedades
	private Map<Edge, EdgeProperties> edgeLevelCaudal = new HashMap<Edge, EdgeProperties>();
	
	//Mapa que contiene la ultima lectura de Bytes de todos los edges
	private Map<Edge, Long> lastEdgeBytesMap = new HashMap<Edge, Long>();
	
	//Mapa que contiene los costes asignados a cada enlace
	private Map<Edge, Long> edgeCost = new HashMap<Edge, Long>();
	
	//variable usada para el tiempo actual
	private int time = 0;
	
	//Inicializamos el objeto mf usado para escribir en los ficheros
	MonitorFiles mf = new MonitorFiles(conf.getMonPath());

	//Objeto para el calculo de caminos usando Dijkstra
	Paths path;

	/**
	 * Pasa una IP en int a InetAddress
	 * 
	 * @param i direccion ip int
	 * @return direccion ip InetAddress
	 */
	static private InetAddress intToInetAddress(int i) {
		byte b[] = new byte[] { (byte) ((i >> 24) & 0xff),
				(byte) ((i >> 16) & 0xff), (byte) ((i >> 8) & 0xff),
				(byte) (i & 0xff) };
		InetAddress addr;
		try {
			addr = InetAddress.getByAddress(b);
		} catch (UnknownHostException e) {
			return null;
		}

		return addr;
	}

	/**
	 * Sets a reference to the requested DataPacketService
	 */
	void setDataPacketService(IDataPacketService s) {
		log.trace("Set DataPacketService.");

		dataPacketService = s;
	}

	/**
	 * Unsets DataPacketService
	 */
	void unsetDataPacketService(IDataPacketService s) {
		log.trace("Removed DataPacketService.");

		if (dataPacketService == s) {
			dataPacketService = null;
		}
	}

	/**
	 * Sets a reference to the requested SwitchManagerService
	 */
	void setSwitchManagerService(ISwitchManager s) {
		log.trace("Set SwitchManagerService.");

		switchManager = s;
	}

	/**
	 * Unsets SwitchManagerService
	 */
	void unsetSwitchManagerService(ISwitchManager s) {
		log.trace("Removed SwitchManagerService.");

		if (switchManager == s) {
			switchManager = null;
		}
	}

	/**
	 * Sets a reference to the requested FlowProgrammerService
	 */
	void setFlowProgrammerService(IFlowProgrammerService s) {
		log.trace("Set FlowProgrammerService.");

		flowProgrammerService = s;
	}

	/**
	 * Unsets FlowProgrammerService
	 */
	void unsetFlowProgrammerService(IFlowProgrammerService s) {
		log.trace("Removed FlowProgrammerService.");

		if (flowProgrammerService == s) {
			flowProgrammerService = null;
		}
	}

	/**
	 * Sets a reference to the requested StatisticsService
	 */
	void setStatisticsManagerService(IStatisticsManager s) {
		log.trace("Set StatisticsManagerService.");

		statisticsManager = s;
	}

	/**
	 * Unset StatisticsManager
	 */
	void unsetStatisticsManagerService(IStatisticsManager s) {
		log.trace("Unset StatisticsManagerService.");

		if (statisticsManager == s) {
			statisticsManager = null;
		}
	}

	/**
	 * Sets a reference to the requested TopologyManager
	 */
	void setTopologyManagerService(ITopologyManager s) {
		log.trace("Set TopologyManagerService.");

		topologyManager = s;
	}

	/**
	 * Unset TopologyManager
	 */
	void unsetTopologyManagerService(ITopologyManager s) {
		log.trace("Unset TopologyManagerService.");

		if (topologyManager == s) {
			topologyManager = null;
		}
	}

	/**
	 * Funcion que se lanza cuando todas las dependencias necesarias han sido
	 * cargadas
	 */
	void init() {
		log.info("Initialized");
		// Disabling the SimpleForwarding and ARPHandler bundle to not conflict
		// with this one
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundle.getSymbolicName().contains("simpleforwarding")) {
				try {
					bundle.uninstall();
				} catch (BundleException e) {
					log.error(
							"Exception in Bundle uninstall "
									+ bundle.getSymbolicName(), e);
				}
			}
		}

	}

	/**
	 * Funcion que se lanza al comenzar el programa
	 */
	void start() {
		log.info("Started");
		path = new Paths(this.topologyManager.getEdges());
		path.buildGraph();

		ActionListener taskMonitor = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				monitor();
			}
		};
		new Timer(refreshTimeMonitor * 1000, taskMonitor).start();
	}

	/**
	 * Funcion que se repite segun refreshTimeMonitor
	 */
	private void monitor() {

		System.out.println("...................Monitor.....................");
		this.showEdgeBps();
		time = time + refreshTimeMonitor;

	}

	void stop() {
		log.info("Stopped");
	}

	/**
	 * Muestra los datos a monitorizar, ademas inicializa el mapa de costes del
	 * grafo de la topologia
	 */
	private void showEdgeBps() {
		Date date = new Date();
		int nStandByEdge, nLevel1Edge, nLevel2Edge, nInterfaces;
		long totalTroughtput, energy;
		String sTime;
		long sum = iniEdgesBps(topologyManager.getEdges());
		totalTroughtput = sum / 2;
		nStandByEdge = standByEdge.size();
		nLevel1Edge = level1Edge.size();
		nLevel2Edge = level2Edge.size();
		// Calculamos energia total
		energy = standByEdge.size() * eStandby + level1Edge.size() * eLevel1
				+ level2Edge.size() * eLevel2;
		nInterfaces = edgeLevelCaudal.size();

		// Mostramos si monitor true
		if (monitor) {
			System.out.println("Energy total mW = " + energy
					+ "\nTroughtput total MB/s = " + totalTroughtput
					+ "\nNumero interfaces standby = " + nStandByEdge
					+ "\nNumero interfaces en nivel 1 = " + nLevel1Edge
					+ "\nNumero interfaces en nivel 2 = " + nLevel2Edge
					+ "\nNumero total interfaces = " + nInterfaces);
		}

		// Guardamos datos en ficheros
		sTime = Integer.toString(time);
		try {
			mf.addLine("date.txt", date.toString());
			mf.addLine("troughtput.txt",
					sTime + "\t" + Long.toString(totalTroughtput));
			mf.addLine("energy.txt", sTime + "\t" + Long.toString(energy));
			mf.addLine("standby.txt",
					sTime + "\t" + Integer.toString(nStandByEdge));
			mf.addLine("level1.txt",
					sTime + "\t" + Integer.toString(nLevel1Edge));
			mf.addLine("level2.txt",
					sTime + "\t" + Integer.toString(nLevel2Edge));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa los maps de los enlaces y sus propiedades
	 */
	public long iniEdgesBps(Map<Edge, Set<Property>> edgeMap) {
		Long troughput;
		long sum = 0;
		EdgeProperties ep;
		edgeLevelCaudal = new HashMap<Edge, EdgeProperties>();
		standByEdge = new HashMap<Edge, EdgeProperties>();
		level1Edge = new HashMap<Edge, EdgeProperties>();
		level2Edge = new HashMap<Edge, EdgeProperties>();
		Iterator<Edge> it = edgeMap.keySet().iterator();
		while (it.hasNext()) {
			Edge edge = it.next();
			// Guardamos en B/s
			troughput = getTroughput(edge);
			sum = sum + troughput;
			if (troughput == 0) { // standby
				ep = new EdgeProperties(0, troughput);
				edgeLevelCaudal.put(edge, ep);
				standByEdge.put(edge, ep);
				edgeCost.put(edge, standbyCost);
			} else if (troughput > 0 && troughput < level1Troughtput) {
				//level 1
				ep = new EdgeProperties(1, troughput);
				edgeLevelCaudal.put(edge, ep);
				level1Edge.put(edge, ep);
				edgeCost.put(edge, level1Cost);
			} else if (troughput >= level1Troughtput) {
				//level 2
				ep = new EdgeProperties(2, troughput);
				edgeLevelCaudal.put(edge, ep);
				level2Edge.put(edge, ep);
				edgeCost.put(edge, level2Cost);
			}
		}
		return sum;
	}

	/**
	 * Obtiene el troughput de un enlace
	 * 
	 * @param edge
	 * @return troughput actual
	 */
	public long getTroughput(Edge edge) {
		long lastBytes, troughput, actualBytes;
		NodeConnectorStatistics portStats = this.statisticsManager
				.getNodeConnectorStatistics(edge.getHeadNodeConnector());

		if (portStats == null) {
			// System.out.println("portStats null");
			actualBytes = 0;
		} else {
			// System.out.println("portStats not null");
			actualBytes = portStats.getReceiveByteCount()
					+ portStats.getTransmitByteCount();
		}

		if (lastEdgeBytesMap.get(edge) == null) {
			lastBytes = 0;
		} else {
			lastBytes = lastEdgeBytesMap.get(edge);
		}
		troughput = (actualBytes - lastBytes) / (refreshTimeMonitor);
		lastEdgeBytesMap.put(edge, actualBytes);
		return troughput;
	}

	/**
	 * Esta funcion es llamada cuando el controlador recibe un packet in
	 * 
	 * @param inPkt paquete en 
	 * @return PacketResult
	 */
	public PacketResult receiveDataPacket(RawPacket inPkt) {
		// Use DataPacketService to decode the packet.
		Packet pkt = dataPacketService.decodeDataPacket(inPkt);
		if (pkt instanceof Ethernet) {
			// packet-in is ethernet
			Ethernet ethFrame = (Ethernet) pkt;
			Object l3Pkt = ethFrame.getPayload();
			byte[] dstMAC = ethFrame.getDestinationMACAddress();

			byte[] srcMAC = ethFrame.getSourceMACAddress();

			if (l3Pkt instanceof IPv4) {
				// packet-in is IPv4
				IPv4 ipv4Pkt = (IPv4) l3Pkt;
				InetAddress srcAddr = intToInetAddress(ipv4Pkt
						.getSourceAddress());
				InetAddress dstAddr = intToInetAddress(ipv4Pkt
						.getDestinationAddress());
				System.out.println("packet in de " + srcAddr.toString()
						+ " para " + dstAddr.toString());
				// Object l4Datagram = ipv4Pkt.getPayload();
				// System.out.println("MAC paquete " + dstMAC);
				return installPathFlows(inPkt, srcMAC, dstMAC, srcAddr, dstAddr);
			}
		}

		return PacketResult.IGNORED;
	}

	/**
	 * Calcula el path entre dos puntos e instala los flujos correspondientes
	 * 
	 * @param inPkt
	 * @param srcMAC
	 * @param dstMAC
	 * @param srcAddr
	 * @param dstAddr
	 * @return PacketResult
	 */
	private PacketResult installPathFlows(RawPacket inPkt, byte[] srcMAC,
			byte[] dstMAC, InetAddress srcAddr, InetAddress dstAddr) {

		NodeConnector dstPort;

		if ((dstPort = searchNodeConnectorHost(dstAddr)) == null) {
			System.out.println("Host no encontrado");
			return PacketResult.IGNORED;
		} else {

			// Host encontrado, calculamos ruta

			Map<Edge, Set<Property>> edgeMap = topologyManager.getEdges();
			path = new Paths(edgeMap);
			path.buildGraph();
			path.iniDijkstra(edgeCost);
			Date date = new Date();
			NodeConnector srcPort = inPkt.getIncomingNodeConnector();
			Node srcNode = srcPort.getNode();
			Node dstNode = dstPort.getNode();

			if (path.connectionExists(srcNode, dstNode)) {
				List<Edge> edgePath = path.getPath(srcNode, dstNode);
				if (edgePath.size() > 0) {
					//guardamos en un fichero la ruta calculada
					// para poder depurar posibles fallos
					try {
						mf.addLine("paths.txt", "de " + srcAddr.toString()
								+ " para " + dstAddr.toString() + "\n"
										+ date.toString() + " "
										+ edgePath.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}

					for (Edge edge : edgePath) {
						// instalamos un flujo en cada nodo del camino 
						// calculado
						programFlow(srcAddr, srcMAC, dstAddr, dstMAC,
								edge.getTailNodeConnector(), edge
										.getTailNodeConnector().getNode());
					}
					//indicamos que el paquete se reenvie por el 
					//puerto del primer edge en el primer nodo
					inPkt.setOutgoingNodeConnector(edgePath.get(0)
							.getTailNodeConnector());
				} else {
					//si la lista de edges es 0, es por que el host
					//se encuentra el el mismo nodo (switch)
					inPkt.setOutgoingNodeConnector(dstPort);
				}
				
				//instalamos la ultima ruta indicando que el ultimo nodo
				// tiene que enviar el paquete por dstPort
				programFlow(srcAddr, srcMAC, dstAddr, dstMAC, dstPort, dstNode);
				this.dataPacketService.transmitDataPacket(inPkt);
				return PacketResult.CONSUME;
			}

		}
		// Si no hay ruta posible desechamos el paquete
		return PacketResult.IGNORED;
	}

	/**
	 * Programa un flujo en un nodo determinado con los datos dados
	 * 
	 * @param srcAddr
	 * @param srcMAC
	 * @param dstAddr
	 * @param dstMAC
	 * @param outConnector
	 * @param node
	 * @return devuelve si el flujo ha sido instalado correctamente
	 */
	private boolean programFlow(InetAddress srcAddr, byte[] srcMAC,
			InetAddress dstAddr, byte[] dstMAC, NodeConnector outConnector,
			Node node) {

		Match match = new Match();
		match.setField(MatchType.DL_TYPE, (short) 0x0800); // IPv4 ethertype
		match.setField(MatchType.NW_SRC, srcAddr);
		match.setField(MatchType.NW_DST, dstAddr);
		// match.setField(MatchType.NW_PROTO, IPProtocols.UDP.byteValue());
		// match.setField(MatchType.DL_SRC, srcMAC);
		// match.setField(MatchType.DL_DST, dstMAC);

		List<Action> actions = new ArrayList<Action>();
		actions.add(new Output(outConnector));

		// Create the flow
		Flow flow = new Flow(match, actions);

		flow.setIdleTimeout(idleTimeOut);
		flow.setHardTimeout(hardTimeOut);
		flow.setPriority((short) 1);

		// Use FlowProgrammerService to program flow.
		Status status = flowProgrammerService.addFlow(node, flow);
		if (!status.isSuccess()) {
			log.error("Could not program flow: " + status.getDescription());
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Busca el puerto correspodiente al que esta conectado un host con la IP
	 * dada
	 * 
	 * @param addrIP
	 * @return puerto correspondiente a el host
	 */
	private NodeConnector searchNodeConnectorHost(InetAddress addrIP) {
		//sacamos un set de todos los puertos que tienen un host conectado
		Set<NodeConnector> nodePorts = this.topologyManager
				.getNodeConnectorWithHost();
		//recorremos todos los puertos buscando la direccion ip
		for (NodeConnector port : nodePorts) {
			List<Host> hosts = this.topologyManager
					.getHostsAttachedToNodeConnector(port);
			for (Host host : hosts) {

				if (host.getNetworkAddress().equals(addrIP)) {
					return port;
				}
			}
		}
		return null;
	}

}
