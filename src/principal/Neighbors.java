package principal;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Neighbors implements Comparable<Neighbors>{
	

	public ConcurrentSkipListSet<String> neighbors;
	public String key;
	public int edgeOut;
	public int inter = 0;
	public float pagerank = 1;
	
	public Neighbors(ConcurrentSkipListSet<String> neighbors, int edgeOut, String key){
		this.neighbors = new ConcurrentSkipListSet<String>(neighbors);
		this.edgeOut = edgeOut;
		this.key = key;
	}
	
	public int getEdgeOut() {
		return edgeOut;
	}
	
	public void setEdgeOut(int edgeOut) {
		this.edgeOut = edgeOut;
	}
	
	public Set<String> getNeighbors(){
		return neighbors;
	}
	
	public void setNeighbors (Set<String> neighbors) {
		this.neighbors = new ConcurrentSkipListSet<String>(neighbors); 
	}
	
	public int getInter(){
		return inter;
	}
	
	public void setInter (int inter) {
		this.inter = inter; 
	}
	
	public void destroy() {
		this.neighbors.clear();

	}

	public void serPagerank(float pagerank) {
		this.pagerank = pagerank;
	}

	@Override
	public int compareTo(Neighbors n) {
		// TODO Auto-generated method stub
		return key.compareTo(n.key);
	}
}
