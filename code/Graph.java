import javafx.scene.control.Tab;

public class Graph {
    private int numVertices;
    private Hash hash;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        hash = new Hash(numVertices);
    }

    public void addVertex(Vertex v) {
        hash.add(v);
    }

    public Vertex getVertex(Vertex v) {
        return hash.get(v);
    }

    public Vertex getVertix(String capital) {
        return hash.get(capital);
    }

    public boolean contains(Vertex v) {
        return hash.contains(v);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public void printGraph() {
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                System.out.println(hash.getHash()[i]);
            }
        }
    }

    /*
     * This method returns the shortest path between two vertices based on the filter
     * provided. The filter can be "Time", "Cost" or "Distance".
     * */
    public TableEntry calculateResult(Vertex src, Vertex dest, String filter) {
       return dijkstra(src, dest, filter);
    }

    private Vertex smallestUnknownDistanceVertex(TableEntry[] table) {
        int minDist = Integer.MAX_VALUE;
        Vertex minVertex = null;

        for (int i = 1; i <= numVertices; i++) {
            if (table[i] != null && !table[i].getVertex().isKnown() && table[i].getCurrCost() < minDist) {
                minDist = table[i].getCurrCost();
                minVertex = table[i].getVertex();
            }
        }

        return minVertex;
    }

    /*
     * This method returns the shortest path between two vertices based on the time taken to travel
     * between the two vertices.
     * */
    private TableEntry dijkstra(Vertex src, Vertex dest, String filter) {
        TableEntry[] table = new TableEntry[numVertices + 1];
        // Read the vertices from the hash table
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                table[i] = new TableEntry(hash.getHash()[i], new Vertex(), Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE);
                hash.getHash()[i].setKnown(false);
                hash.getHash()[i].setId(i);
            }
        }

        System.out.println("Src ID: " + src.getId());
        table[src.getId()].setCost(0);
        table[src.getId()].setTime(0);
        table[src.getId()].setDistance(0);
        table[src.getId()].setCurrCost(0);
        table[src.getId()].setPath(null);
        while (true) {
            Vertex v = smallestUnknownDistanceVertex(table);
            if (v == null) {
                break;
            }

            v.setKnown(true);
            LinkedList edges = v.getEdge();
            Node node = edges.getFront();

            while (node != null) {
                Edge e = node.getElement();
                Vertex w = e.getDestination();
                if(w.getCapital().getCapitalName().equals(dest.getCapital().getCapitalName())) {
                    LinkedList path = getPath(table, dest);

                    hash.setVerticesUnknown();
                    return new TableEntry(dest, table[dest.getId()].getPath(), table[dest.getId()].getCost(),
                            table[dest.getId()].getTime(), table[dest.getId()].getDistance(), table[dest.getId()].getCurrCost(), path);
                }

                if (!w.isKnown()) {
                    int cvw;
                    if (filter.equals("Time")) {
                        cvw = e.getTime();
                    } else if (filter.equals("Cost")) {
                        cvw = e.getCost();
                    } else {
                        cvw = e.getDistance();
                    }

                    if (table[v.getId()].getCurrCost() + cvw < table[w.getId()].getCurrCost()) {
                        table[w.getId()].setCurrCost(table[v.getId()].getCurrCost() + cvw);
                        table[w.getId()].setCost(table[v.getId()].getCost() + e.getCost());
                        table[w.getId()].setTime(table[v.getId()].getTime() + e.getTime());
                        table[w.getId()].setDistance(table[v.getId()].getDistance() + e.getDistance());
                        table[w.getId()].setPath(v);
                    }
                }
                node = node.getNext();
            }
        }

        hash.setVerticesUnknown();
        return null;
    }

    /*
     * This method returns the path from the source to the destination vertex.
     * */
    public LinkedList getPath(TableEntry[] table, Vertex dest) {
        LinkedList path = new LinkedList();
        Vertex curr = dest;
        while (curr != null && curr.getCapital() != null) {
            path.addFirst(new Node(new Edge(table[curr.getId()].getPath(), curr, table[curr.getId()].getCost(), table[curr.getId()].getTime(), table[curr.getId()].getDistance())));
            curr = table[curr.getId()].getPath();
        }
        return path;
    }
}