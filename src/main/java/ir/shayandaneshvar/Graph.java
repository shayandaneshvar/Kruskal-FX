package ir.shayandaneshvar;

import java.util.HashSet;
import java.util.Set;

public class Graph {
    private Integer vertices;
    private HashSet<Edge> edges;

    public Graph(Integer numberOfVertices) {
        vertices = numberOfVertices;
        edges = new HashSet<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Set<Edge> getEdges() {
        return (Set<Edge>) edges.clone();
    }


    public Integer getSize() {
        return vertices;
    }

    public static class Subset {
        private Integer parent;

        public Integer getParent() {
            return parent;
        }

        public void setParent(Integer parent) {
            this.parent = parent;
        }
    }
}