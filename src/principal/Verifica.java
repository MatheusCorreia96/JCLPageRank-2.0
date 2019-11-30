package principal;

import java.util.concurrent.ConcurrentHashMap;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;

public class Verifica {
	
	JCL_facade jclLambari = JCL_FacadeImpl.getInstanceLambari();
	
	public void execute() {
		ConcurrentHashMap<String, Float> localGraphNeighbors = (ConcurrentHashMap<String, Float>) jclLambari.getValue("localGraphNeighbors").getCorrectResult();
		
		System.err.println("map on JCL host " + localGraphNeighbors.size());
		System.err.println("Arestas de saida: ");
		for(String keyy: localGraphNeighbors.keySet()) {
			Float ns = localGraphNeighbors.get(keyy);
			System.err.println(keyy +": "+ns);
		}
	}
}
