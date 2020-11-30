package principal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;

public class LocalPageRank {
	
	public void execute(int interacoes) throws IOException{
		JCL_facade jclLambari = JCL_FacadeImpl.getInstanceLambari();
		JCL_facade jclPacu = JCL_FacadeImpl.getInstance();		
		
		@SuppressWarnings("unchecked")
		//Map<String, Neighbors> localGraph = (Map<String, Neighbors>) jclLambari.getValue("localGraph").getCorrectResult();
		ConcurrentHashMap<String, Neighbors> localGraph = (ConcurrentHashMap<String, Neighbors>) jclLambari.getValue("localGraph").getCorrectResult();
		
		ConcurrentHashMap<String, String> localGraphNeighbors = (ConcurrentHashMap<String, String>) jclLambari.getValue("localGraphNeighbors").getCorrectResult();
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter("arquivo.txt"));
		for(int a = 0; a<interacoes; a++) {
			for(String key: localGraph.keySet()){
				Neighbors ns = localGraph.get(key);
				ConcurrentSkipListSet<String> neighbors = ns.neighbors;					
					float vizinhos = 0;
					//calcular pagerank normalmente
					if(neighbors != null) {
					for(String viz:neighbors) {			
						String[] split = localGraphNeighbors.get(viz).split(":");
						int links = Integer.parseInt(split[1]);
					
						Float prvalue = Float.parseFloat(split[0]);
						vizinhos = (prvalue/links) + vizinhos;
						ns.pagerank = (float) (0.15 + (0.85 * vizinhos));					
					}				
				
				
				localGraph.put(key, ns);
					
				if(a == interacoes -1)
					buffWrite.append("key: " + key + " pagerank: " + ns.pagerank+ System.lineSeparator());
				
//				System.out.println("key: " + key + " pagerank: " + ns.pagerank);
			
					}
			}
			
	
			ConcurrentSkipListSet<Neighbors> keys = new ConcurrentSkipListSet<Neighbors>();
			
			for(String key: localGraph.keySet()){
				Neighbors ns = localGraph.get(key);
				keys.add(ns);
			}
			
			List<Future<JCL_result>> tickets;
			Object[] args ={keys};
			tickets = jclPacu.executeAll("RemoteUpdates", args);
				
			jclPacu.getAllResultBlocking(tickets);
			
			keys.clear();
			
			
		}
		buffWrite.close();
		
	}

}
