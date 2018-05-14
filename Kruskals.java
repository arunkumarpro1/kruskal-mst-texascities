package com.arun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Kruskals {
	public static void main(String[] args) throws FileNotFoundException {
		int edgesAccepted = 0;

		EdgeComparator ec = new EdgeComparator();
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>(ec);
		Edge e;
		String[] entries;
		HashMap<String,Vertex> vertices = new HashMap<>();
		String uset, vset;
		int sumOfDistance=0;

		Scanner s = new Scanner(new File("cities.csv"));
		while (s.hasNext()) {
			entries = s.nextLine().split(",");
			vertices.put(entries[0], new Vertex(entries[0]));
			for (int i = 1; i < entries.length; i += 2)
				pq.add(new Edge(entries[0], entries[i], Integer
						.parseInt(entries[i + 1])));
		}
		DisjointSet ds = new DisjointSet(vertices);
		int NUM_VERTICES = vertices.size();
		while (edgesAccepted < NUM_VERTICES - 1) {
			e = pq.poll(); // get minimum edge = (u,v)
			uset = ds.find(e.getFromCity()); // find set vertex u is in.
			vset = ds.find(e.getToCity()); // find set vertex v is in.
			if (!uset.equals(vset)) // if not same set (not yet connected)
			{
				// accept the edge
				edgesAccepted++;
				ds.union(uset, vset); // connect them
				System.out.println(e.getFromCity() + " <-> " + e.getToCity()+ " Distance: "+e.getDistance());
				sumOfDistance += e.getDistance();
			}
		}
	System.out.println("\nThe total sum of all the distances in the minimum spanning tree is "+sumOfDistance);
	s.close();}
}

class Vertex {
	String city;
	int value;

	public Vertex(String city) {
		super();
		this.city = city;
		this.value = -1;
	}
}

class Edge {
	private String fromCity;
	private String toCity;
	private int distance;

	
	public Edge(String fromCity,String toCity,int distance) {
		super();
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.distance = distance;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}

class EdgeComparator implements Comparator<Edge> {
	@Override
	public int compare(Edge e1, Edge e2) {
		if (e1.getDistance() == e2.getDistance())
			return 0;
		else if (e1.getDistance() > e2.getDistance())
			return 1;
		else
			return -1;
	}
}

class DisjointSet {

	private HashMap<String,Vertex> s;
	Vertex rootV1,rootV2;

	/**
	 * Construct the disjoint sets object.
	 * 
	 * @param numElements
	 *            the initial number of disjoint sets.
	 */
	public DisjointSet(HashMap<String,Vertex> vertices) {
		s = vertices;
	}

	/**
	 * Union two disjoint sets using the height heuristic. For simplicity, we
	 * assume root1 and root2 are distinct and represent set names.
	 * 
	 * @param root1
	 *            the root of set 1.
	 * @param root2
	 *            the root of set 2.
	 */
	public void union(String root1, String root2) {
		rootV1 = s.get(root1);
		rootV2 = s.get(root2);
		if (rootV2.value < rootV1.value) // root2 is deeper
			rootV1.city = root2; // Make root2 new root
		else {
			if (rootV1.value == rootV2.value)
				rootV1.value = rootV1.value--; // Update height if same
			rootV2.city = root1; // Make root1 new root
		}
	}

	/**
	 * Perform a find. Error checks omitted again for simplicity.
	 * Since union by height method is used,
	 * path compression is not done.
	 * @param x
	 *            the element being searched for.
	 * @return the set containing x.
	 */

	public String find(String x) {
		if (s.get(x).city.equals(x))
			return x;
		else
			return find(s.get(x).city); 
	}
}

