import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Collections;

public class Pathfinder {
	public static void fillMapRandom(float[][] map, int s) {
		System.out.println("Filling map " + map.length + ","+ map[0].length+" with random decimal numbers.");
		Random r = new Random(s);
				for (int i=0;i<map.length;i++)
			for (int j=0;j<map[0].length;j++)
				map[i][j] = r.nextFloat();
	}
	public static void printMap(float[][] map) {
		String response = "y";
		if (map.length > 20) {
			System.out.println("Very large map detected. Do you really want to print it? Type y for yes. ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			response = "n";
			try {
				java.lang.Thread.sleep(3000); } 
			catch (Exception e) { 			} 
			try {
				if (in.ready()) {
				response = in.readLine();	} 
				else {
					System.out.println("Defaulting to no after 5 sec.");
				} 	} 
			catch (Exception e) { 		}	
		}
		if (response != "y") 
			return;
		for (int i=map[0].length-1;i>=0;i--) {
			for (int j=0;j<map.length;j++) {
				System.out.print(String.format("%.3f", map[j][i]) + " "); //denne tar bare 3 desimaler uansett
				//System.out.print(String.format("%.3g", map[i][j]) + " "); 
				//Denne med g teller 3 fra ledende siffer og hopper over ledende 0
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void printCoordSequence(float[][] map, ArrayList<RouteNode> path) {
		System.out.print("Step list contains " + path.size() + "steps.");
		for(int i=0;i<path.size();i++) {
			System.out.print(path.get(i).getValueString(map));
			if(i<path.size()-1)
				System.out.print(" -> ");
		}
		System.out.println();
	}
	public static void printMapWithRoute(float[][] map, ArrayList<RouteNode> path) {
		String response = "y";
		if (map.length > 20) {
			System.out.println("Very large map detected. Do you really want to print it? Type y for yes. ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			response = "n";
			try {
				java.lang.Thread.sleep(3000); } 
			catch (Exception e) { 			} 
			try {
				if (in.ready()) {
				response = in.readLine();	} 
				else {
					System.out.println("Defaulting to no after 5 sec.");
				} 	} 
			catch (Exception e) { 		}	
		}
		if (response != "y") 
			return;
		char pathMap[][] = new char[map.length][map[0].length];
		for (int i=0;i<path.size();i++)
			pathMap[path.get(i).getx()][path.get(i).gety()] = 'r';
		pathMap[path.get(0).getx()][path.get(0).gety()] = 's';
		pathMap[path.get(path.size()-1).getx()][path.get((path.size()-1)).gety()] = 'e';
		
		for (int i=map[0].length-1;i>=0;i--) {
			for (int j=0;j<map.length;j++) {
				if(pathMap[j][i] =='r') 
					System.out.print("  x   ");
				else if(pathMap[j][i] =='s') 
					System.out.print("START ");
				else if(pathMap[j][i] =='e') 
					System.out.print(" END  ");
				else if(pathMap[j][i] =='e') 
					System.out.print(" END  ");
				else if(map[j][i] >= 2) 
					System.out.print(" WALL ");
				else
					System.out.print(String.format("%.3f", map[j][i]) + " "); //denne tar bare 3 desimaler uansett
				//System.out.print(String.format("%.3g", map[i][j]) + " "); 
				//Denne med g teller 3 fra ledende siffer og hopper over ledende 0
			}
			System.out.println();
		}
		System.out.println();
	}	
	public static int getClosedSize(boolean[][] b) {
		int n =0;
		for (int i=0;i<b.length;i++)
			for (int j=0;j<b[0].length;j++)
				if(b[i][j])
					n++;
		return n;
	}
	public static ArrayList<RouteNode> getPath(float[][] map, RouteNode start, RouteNode end) {
		//https://www.codeproject.com/Articles/9880/Very-simple-A-algorithm-implementation
		LinkedList<RouteNode> OPEN = new LinkedList<RouteNode>();
		//LinkedList<RouteNode> CLOSED = new LinkedList<RouteNode>();
		boolean CLOSEDB[][] = new boolean[map.length][map[0].length];
		float cumSumMap[][] = new float[map.length][map[0].length];
		
		start.setCost(map[start.getx()][start.gety()]);
		start.setCumCost(0);
		OPEN.add(start);
		
		ArrayList<RouteNode> path = new ArrayList<RouteNode>();		
		long tstart = System.currentTimeMillis();
		int whilecounter = 0;
		
		while (!OPEN.isEmpty()) {
			int bestIndex = -1;
			
			double bestSum = Double.MAX_VALUE;
			for(int i=0;i<OPEN.size();i++) {
				double sum = OPEN.get(i).getCumCost();
				if(sum < bestSum) {
					bestSum = sum;
					bestIndex = i;
				}
			}
			//System.out.println("Den beste ruten å gå videre til er " + OPEN.get(bestIndex).getValueString(map));
			RouteNode current = OPEN.remove(bestIndex);			
			if(current.isMatch(end)) {
				end = current;
				RouteNode n = end;
				while (n != null) {
					//System.out.println(n.getValueString(map) + " path.size = " +path.size());
					//printCoordSequence(map, path);
					path.add(n);
					n = n.parent;
				}
				Collections.reverse(path);				
				System.out.println("ROUTE FOUND using "+ whilecounter + " iterations. Path has " + path.size() + " steps, and cost " + end.getCumCost() + ".");
				break;
			}
			
			ArrayList<RouteNode> nextNodes = current.getNeighbors(map);
			for(int i=0;i<nextNodes.size();i++)
			{
				//where the magic happens	
				cumSumMap[nextNodes.get(i).getx()][nextNodes.get(i).gety()] = nextNodes.get(i).setCumCost(current.getCumCost());
				int oFound = -1;
				for (int j=0;j<OPEN.size();j++) {
					if (nextNodes.get(i).isMatch(OPEN.get(j))) {
						oFound = j;
						break;
					}
				}
				if (oFound>=0)
					if (OPEN.get(oFound).compareCost(nextNodes.get(i)) <= 0)
						continue;
				if (oFound!=-1)
					OPEN.remove(oFound);
				//alternativ implementasjon for å unngå CLOSED grunnet SYKT mye tidsbruk. 
				int closex = -1, closey = -1;
				doublefor:
				for (int j=0;j<map.length;j++)  {
					for (int k=0;k<map[0].length;k++) {
						int getix = nextNodes.get(i).getx();
						int getiy = nextNodes.get(i).gety();
						if (CLOSEDB[getix][getiy]) {
							closex = getix;
							closey = getiy;
							break doublefor;
						}
					}
				}
				if (closex>=0)
					if 	(cumSumMap[nextNodes.get(i).getx()][nextNodes.get(i).gety()] <= nextNodes.get(i).getCumCost())
						continue;
				
				if (closex!=-1) //Kjøres denne noensinne??
					CLOSEDB[closex][closey] = false;
		
				OPEN.push(nextNodes.get(i));
			}
			if (whilecounter % 100 ==0) {				
				System.out.println("whilerunde " + whilecounter + " tok " + (System.currentTimeMillis() - tstart) + "ms. OPEN.size()=" + OPEN.size() + " CLOSED.size()="+getClosedSize(CLOSEDB));
				tstart = System.currentTimeMillis();
			}
			whilecounter++; 			
			//CLOSED.push(current);
			CLOSEDB[current.getx()][current.gety()]=true;
		}
		return  path;
	}
	public static void makeWalls(float[][] map, int n, float wall, int rseed)	{
		if (map[0].length > 9) {
			for(int i=0;i<6;i++) {
				map[2][map[0].length -1-i] = wall;
				map[5][map[0].length -3-i] = wall;
				map[i][map[0].length -9] = wall;
				map[map.length-3][(map[0].length/2)+i-2] = wall;
				
			}
		}
		
		Random r = new Random(rseed);
		for(int i=0;i<n;i++) {
			int x = r.nextInt(map.length - 1);
			int y = r.nextInt(map[0].length - 2);
			map[x][y] = wall;
			map[x][y+1] = wall;
			map[x][y+2] = wall;
		}
	}
	
	
	public static void main(String[] args) {
		//String version = System.getProperty("java.version");
		//System.out.println("Current JRE version is " + version);				
				
		int mapSizex = 50;
		int mapSizey = 50;
		float wall = 10;
		int randomseed = 7;
		float[][] map = new float[mapSizex][mapSizey];				
		fillMapRandom(map,randomseed);
		
		//make a wall
		makeWalls(map, 20, wall, randomseed);
		printMap(map);
		
		RouteNode startpoint = new RouteNode(0,0, map[0][0]);
		RouteNode endpoint = new RouteNode(0,mapSizey-1, map[0][mapSizey-1]);
		RouteNode randompoint = new RouteNode(mapSizex-1,mapSizey/2);		
		System.out.println("My arbitrary point is " + randompoint.getValueString(map));
		//printCoordSequence(map, randompoint.getNeighbors(map));	
		
		ArrayList<RouteNode> shortestPath;
		long tstart = System.currentTimeMillis();
		shortestPath = getPath(map, startpoint, randompoint);
		System.out.println("Route found in (ms): " + (System.currentTimeMillis()-tstart) +"\n");
		printMapWithRoute(map, shortestPath);
		
		tstart = System.currentTimeMillis();
		shortestPath.addAll(getPath(map, randompoint, endpoint));
		System.out.println("Route found in (ms): " + (System.currentTimeMillis()-tstart) +"\n");
		printMapWithRoute(map, shortestPath);
		//printCoordSequence(map, shortestPath);
		
		BMPGenerator output = new BMPGenerator(mapSizex, mapSizey);
		output.makeBMP(map, shortestPath);
		
		
		
		
	}
}