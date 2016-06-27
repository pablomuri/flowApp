package uex.pablomuri.flowApp;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.opendaylight.controller.sal.core.ConstructionException;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.Edge;
import org.opendaylight.controller.sal.core.Property;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Calculo de caminos usando el algoritmos shortest path de Dijkstra
 * 
 * @author pablomuri
 *
 */
public class Paths {

	private Graph<Node, Edge> topoGraph = new DirectedSparseMultigraph<Node, Edge>();
	private Transformer<Edge, Long> costTransformer = null;
	private Map<Edge, Long> edgeCost = new HashMap<Edge, Long>();
	private Map<Edge, Set<Property>> edges;
	private DijkstraShortestPath<Node, Edge> dijkstra;

	/**
	 * Constructor principal de la clase.
	 * 
	 * @param edges
	 */
	public Paths(Map<Edge, Set<Property>> edges) {
		this.edges = edges;
	}

	/**
	 * Crea el grafo principal a partir del mapa de edges
	 */
	public void buildGraph() {
		Iterator<Edge> it = edges.keySet().iterator();
		while (it.hasNext()) {
			Edge edge = it.next();
			topoGraph.addEdge(edge, edge.getTailNodeConnector().getNode(), edge
					.getHeadNodeConnector().getNode(), EdgeType.DIRECTED);
		}
	}

	/**
	 * Inicializa el objeto dijkstra que nos permitira el calculo de caminos a
	 * partir del grafo y el mapa edges y costes
	 * 
	 * @param edgeCost
	 */
	public void iniDijkstra(Map<Edge, Long> edgeCost) {
		this.edgeCost = edgeCost;
		this.buildCostTransformer();
		this.dijkstra = new DijkstraShortestPath<Node, Edge>(topoGraph,
				costTransformer);
	}

	/**
	 * Crea el transformer a partir del mapa de edges y costes para poder usarlo
	 * en el constructor de DijkstraShortestPath
	 */
	private void buildCostTransformer() {

		costTransformer = new Transformer<Edge, Long>() {
			public Long transform(Edge e) {
				return edgeCost.get(e);
			}
		};
	}

	/**
	 * Calcula la ruta mas corta entre dos nodos dados
	 * 
	 * @param srcNode
	 *            nodo fuente
	 * @param dstNode
	 *            nodo destino
	 * @return devuelve una lista de los edges que forman el camino
	 */
	public List<Edge> getPath(Node srcNode, Node dstNode) {
		// System.out.println("Calculamos path entre " + srcNode + "  " +
		// dstNode);
		List<Edge> edgeList;
		try {
			edgeList = dijkstra.getPath(srcNode, dstNode);
		} catch (IllegalArgumentException e) {
			return null;
		}
		return edgeList;
	}

	/**
	 * Devuelve el edge inverso
	 * 
	 * @param edge
	 *            dado
	 * @return edge inverso
	 */
	public Edge getInvertEdge(Edge edge) {
		try {
			return (new Edge(edge.getHeadNodeConnector(),
					edge.getTailNodeConnector()));
		} catch (ConstructionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Devuelve un mapa con la distancia a todos los nodos de la topologia
	 * 
	 * @param srcNode
	 *            nodo fuente
	 * @return mapa con todos los nodos y sus distancia a el nodo fuente
	 */
	public Map<Node, Number> getNodesDistance(Node srcNode) {
		return dijkstra.getDistanceMap(srcNode);
	}

	/**
	 * Devuelve si existe una conexion entre dos nodos
	 * 
	 * @param srcNode
	 * @param dstNode
	 * @return true si la conexion existe, false si no
	 */
	public boolean connectionExists(Node srcNode, Node dstNode) {
		if (srcNode.equals(dstNode)) {
			return true;
		} else {
			try {
				Number distance = dijkstra.getDistance(srcNode, dstNode);
				System.out.println(distance.longValue());
				return (distance != null);
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
	}

}
