package principal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ParallelUpdate{
	

	public void execute(Integer ClusterCoreSize, Integer specificCore, ConcurrentSkipListSet<Neighbors> keys, ConcurrentHashMap<String, String> localGraphNeighbors){
		
		
		for(Neighbors key: keys){
			
			int hostNum = Math.abs(key.key.hashCode()%ClusterCoreSize);	
			
			if(hostNum == specificCore) {
				if(localGraphNeighbors.get(key.key) != null){
					String[] split = localGraphNeighbors.get(key.key).split(":");
				
					localGraphNeighbors.put(key.key, key.pagerank+":"+split[1]);		
				}
			}
			//aqui vc checa o hash
			//se o hash der igual a specificCore vc o processa conforme já faz
			//caso contrario vc pega outro neighbor de keys
		}
		
	}
	

}

