package principal;
import java.util.List;
import java.util.concurrent.Future;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;
import util.ReadInput;

public class MainJCL {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainJCL();
		
	}
	
	public MainJCL(){
		
		JCL_facade jcl = JCL_FacadeImpl.getInstance();
		
		long time = System.currentTimeMillis();	
		reportPerformanceFor("Come�ou load grafo", time);
		//loading a graph in JCL
		LoadGraphInJCL.loadInJcl(ReadInput.readInput("followers.txt"));	
		reportPerformanceFor("graph allocation ", time);
		
		
		time = System.currentTimeMillis();
		reportPerformanceFor("Come�ou pagerank", time);
		//starting a local page rank on each JCL Host
		List<Future<JCL_result>> tickets;
		jcl.register(LocalPageRank.class, "LocalPageRank");
		jcl.register(RemoteUpdates.class, "RemoteUpdates");
		
		int interacoes = 2;
		Object[] args = {interacoes};
		
		tickets = jcl.executeAll("LocalPageRank", args);
		jcl.getAllResultBlocking(tickets);
		
		reportPerformanceFor("PageRank algoritmo ", time);

		jcl.destroy();
	}
	
	private static void reportPerformanceFor(String msg, long refTime)
    {
        double time = (System.currentTimeMillis() - refTime) / 1000.0;
        double mem = usedMemory() / (1024.0 * 1024.0);
        mem = Math.round(mem * 100) / 100.0;
        System.out.println(msg + " (" + time + " sec, " + mem + "MB)");
    }

    private static long usedMemory()
    {
        Runtime rt = Runtime.getRuntime();

        return rt.totalMemory() - rt.freeMemory();
    }

}
