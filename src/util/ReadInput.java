package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import principal.Neighbors;

public class ReadInput {
	
	public static ConcurrentHashMap<String, Neighbors> readInput(String path){
		//read files of type followers -> represents a graph
		// 83 473 means 83 follows 473, i.e., 83 is a neighbor of 473
		
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File(path)));
			String oneLine = null;
			ConcurrentHashMap<String, Neighbors> graphNeighbors = new ConcurrentHashMap<String, Neighbors>();
			while((oneLine=r.readLine())!=null){
				String[] partitions = oneLine.split(" ");
				String key = partitions[1];
				String key2 = partitions[0];
				if(graphNeighbors.containsKey(key)) {
					if(!graphNeighbors.get(key).neighbors.contains(partitions[0])) {
						graphNeighbors.get(key).neighbors.add(partitions[0]);
					}
				}else{
					ConcurrentSkipListSet<String> s = new ConcurrentSkipListSet<String>();
					s.add(partitions[0]);
					Neighbors neighbors = new Neighbors(s,0, key);
					graphNeighbors.put(key, neighbors);
				}
				
				if(graphNeighbors.containsKey(key2)){
					
					int edgeO = graphNeighbors.get(key2).edgeOut;
					edgeO++;
					graphNeighbors.get(key2).setEdgeOut(edgeO);
				}else {
					ConcurrentSkipListSet<String> s = new ConcurrentSkipListSet<String>();
					Neighbors neighbors = new Neighbors(s,1,key2);
					graphNeighbors.put(key2, neighbors);
				}
			
				partitions=null;
			}
			
			r.close();
			
			return graphNeighbors;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
