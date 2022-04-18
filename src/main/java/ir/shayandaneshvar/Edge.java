package ir.shayandaneshvar;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private Integer from, to, weight;

    public Edge(Integer from, Integer to, Integer weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return (Objects.equals(from, edge.from) || Objects.equals(to, edge
                .from)) && (Objects.equals(to, edge.to) || Objects.equals(from,
                edge.to)) && Objects.equals(weight, edge.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight) +
                Objects.hash(to, from, weight) + 7 * (to + from) + 11 * weight;
    }

    @Override
    public int compareTo(Edge o) {
        return weight - o.weight;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getWeight() {
        return weight;
    }
}