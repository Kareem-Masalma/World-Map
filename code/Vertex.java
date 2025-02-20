public class Vertex {
    private Capital capital;
    private LinkedList edges;
    private boolean known;
    private int id;
    int currID;


    public Vertex() {
        capital = null;
        edges = new LinkedList();
        known = false;
    }

    public Vertex(Capital capital) {
        this.capital = capital;
        edges = new LinkedList();
        known = false;
    }

    public Capital getCapital() {
        return capital;
    }

    public LinkedList getEdge() {
        return edges;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public void addEdge(Edge edge) {
        edges.add(new Node(edge));
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return capital.getCapitalName().hashCode();
    }

    @Override
    public String toString() {
        return capital.getCapitalName();
    }
}