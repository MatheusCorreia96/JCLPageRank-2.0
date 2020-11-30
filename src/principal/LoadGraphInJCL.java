package principal;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;

public class LoadGraphInJCL {
	
	public static void loadInJcl(ConcurrentHashMap<String, Neighbors> graphNeighbors){
		
		JCL_facade jcl = JCL_FacadeImpl.getInstance();
		File[] jar = {new File("../JCLGraph/lib/Principal.jar")};		
		
		System.out.println(jcl.register(jar, "LocalStorage"));
		
		List<Map.Entry<String, String>> devs = jcl.getDevices();
				
		
		//maps to be stored in JCL
		@SuppressWarnings("unchecked")
		ConcurrentHashMap<String, Neighbors>[] manyGraphNeighbors = new ConcurrentHashMap[devs.size()];
		ConcurrentHashMap<String, String>[] neighborsMap = new ConcurrentHashMap[devs.size()];
		for(int i=0; i<devs.size();i++) {
			manyGraphNeighbors[i] = new ConcurrentHashMap<String, Neighbors>();
			neighborsMap[i] = new ConcurrentHashMap<String, String>();
		}
		
		ConcurrentSkipListSet<String> keys = new ConcurrentSkipListSet<String>(graphNeighbors.keySet());
		
		for(String oneK:keys){
			//jcl.instantiateGlobalVar(oneK, graphNeighbors.get(oneK).edgeOut);
			
			int hostNum = Math.abs(oneK.hashCode()%devs.size());
			Neighbors n = graphNeighbors.get(oneK);
			manyGraphNeighbors[hostNum].put(oneK, n);					
			for(String v: n.neighbors) {
				if(graphNeighbors.get(v) != null) 
					neighborsMap[hostNum].put(v, "1.0:"+Integer.toString(graphNeighbors.get(v).edgeOut));			
			}
			
		}
		
		graphNeighbors.clear();
		graphNeighbors = null;
		
		List<Future<JCL_result>> tickets = new LinkedList<Future<JCL_result>>();
		
		for(int i=0; i<devs.size(); i++){
			Object[] args = {manyGraphNeighbors[i], neighborsMap[i]};
			tickets.add(jcl.executeOnDevice(devs.get(i), "LocalStorage", args));
		}
		
		

		jcl.getAllResultBlocking(tickets);
		
		
		for(int i=0; i<devs.size(); i++){
			manyGraphNeighbors[i].clear();
			neighborsMap[i].clear();
		}			
	}
	
	

}
