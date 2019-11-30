package principal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;

public class RemoteUpdates{
	
	
	JCL_facade jclLambari = JCL_FacadeImpl.getInstanceLambari();
	//Map<String, Neighbors> localGraph = (Map<String, Neighbors>) jclLambari.getValue("localGraph").getCorrectResult();
	ConcurrentHashMap<String, String> localGraphNeighbors = (ConcurrentHashMap<String, String>) jclLambari.getValue("localGraphNeighbors").getCorrectResult();
	
	public void execute(ConcurrentSkipListSet<Neighbors> keys){
		
		for(Neighbors key: keys){
			if(localGraphNeighbors.get(key.key) != null){
				String[] split = localGraphNeighbors.get(key.key).split(":");
			
				localGraphNeighbors.put(key.key, key.pagerank+":"+split[1]);		
			}
		}
	}

}
