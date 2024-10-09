import java.util.ArrayList;

public class RouteNode {
	private int x, y;
	private float selfcost;
	private float cumulativecost;
	RouteNode parent;
	
	RouteNode(int x, int y, float cost) {
		this.x = x;
		this.y = y;			
		this.selfcost = cost;
		parent = null;
	}
	RouteNode(int x, int y) {
		this.x = x;
		this.y = y;			
		this.selfcost = 0;
		parent = null;
	}
	RouteNode(int x, int y, float cost, RouteNode parent) {
		this.x = x;
		this.y = y;			
		this.selfcost = cost;
		this.parent = parent;
	}
	public int getx() {
		return x; }
	public int gety() {
		return y; }
	public boolean isMatch(RouteNode n) {
		return (this.x == n.x) && (this.y == n.y);	}
	public float getCost() {
		return selfcost; }
	public void setCost(float cost) {
		selfcost = cost; }
	public float getCumCost() {
		return cumulativecost; }
	public float setCumCost(float sumcost) {
		return cumulativecost = selfcost + sumcost; }
	public float compareCost(RouteNode n) {
		return n.getCost() - selfcost;	}	
	public String getString() {
		return "(" + x + "," + y + ")";	}
	public String getValueString(float[][] map) {
		return getString() + "=" + String.format("%.3f", map[x][y]); }
	public ArrayList<RouteNode> getNeighbors(float[][] map) {
		//RouteNode[] neighbors = new RouteNode[4];
		ArrayList<RouteNode> neighbors = new ArrayList<RouteNode>();
		if(x>0 && x<map.length -1) {
			if(y>0 && y<map[0].length-1) { 
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==0) {
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				//neighbors[1]= null;
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==map[0].length-1) {
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				//neighbors[3]= null;
			}			
		}
		else if (x==0) {
			if(y>0 && y<map[0].length-1) { 
				//neighbors[0]= null;
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==0) {
				//neighbors[0]= null;
				//neighbors[1]= null;
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==map[0].length-1) {
				//neighbors[0]= null;
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				neighbors.add(new RouteNode(x+1, y, map[x+1][y],this));
				//neighbors[3]= null;
			}
		}		
		else if (x==map.length-1) {
			if(y>0 && y<map[0].length-1) { 
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				//neighbors[2]= null;
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==0) {
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				//neighbors[1]= null;
				//neighbors[2]= null;
				neighbors.add(new RouteNode(x, y+1, map[x][y+1],this));
			}
			else if(y==map[0].length-1) {
				neighbors.add(new RouteNode(x-1, y, map[x-1][y],this));
				neighbors.add(new RouteNode(x, y-1, map[x][y-1],this));
				//neighbors[2]= null;
				//neighbors[3]= null;
			}
		}
		return neighbors;
	}
}