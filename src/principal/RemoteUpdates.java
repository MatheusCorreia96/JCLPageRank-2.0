package principal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import java.util.concurrent.Future;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;


public class RemoteUpdates{
	
	
	JCL_facade jclLambari = JCL_FacadeImpl.getInstanceLambari();
	//Map<String, Neighbors> localGraph = (Map<String, Neighbors>) jclLambari.getValue("localGraph").getCorrectResult();
	ConcurrentHashMap<String, String> localGraphNeighbors = (ConcurrentHashMap<String, String>) jclLambari.getValue("localGraphNeighbors").getCorrectResult();
	
	public void execute(ConcurrentSkipListSet<Neighbors> keys){
		
		int ClusterCoreSize = jclLambari.getClusterCores();
		
		
		Object[][] argsExecC = new Object[ClusterCoreSize][4];
		
		//monta os argumentos pro update remote parallel via jcl
		for(int i=0; i<ClusterCoreSize; i++){
			Object[] args = {ClusterCoreSize, i, keys, localGraphNeighbors};
			argsExecC[i] = args;
		}
		
		
		//pode ja ter registrado em chamadas de remote update anteriores
		jclLambari.register(ParallelUpdate.class, "ParallelUpdate");
		
		List<Future<JCL_result>> ticket = jclLambari.executeAllCores("ParallelUpdate", argsExecC);
		
		jclLambari.getAllResultBlocking(ticket);
			
	}

}
