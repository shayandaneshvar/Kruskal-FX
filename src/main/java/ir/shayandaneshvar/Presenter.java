package ir.shayandaneshvar;

import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Presenter {
    private Graph graph;
    private SmartGraphPanel<String, String> graphView;
    private GraphEdgeList<String, String> graphEdgeList;
    @FXML
    private JFXTextArea matrixArea;
    @FXML
    private JFXTextField length;
    @FXML
    private JFXCheckBox autoLayout;

    @FXML
    void drawGraph() {
        Platform.runLater(() -> {
            handleGraphWindow("Graph");
            getGraph(Integer.parseInt(length.getText().trim()));
            prepareGraph(graph);
        });
    }

    @FXML
    void drawKruskalMST() {
        Platform.runLater(() -> {
            handleGraphWindow("Minimum Spanning Tree");
            prepareGraph(Objects.requireNonNull(kruskalMSTAlgorithm()));
        });
    }

    private void prepareGraph(Graph graph) {
        for (int i = 0; i < graph.getSize(); i++) {
            graphEdgeList.insertVertex(String.valueOf(i));
        }
        graph.getEdges().forEach(e -> {
            var from = String.valueOf(e.getFrom());
            var to = String.valueOf(e.getTo());
            var weight = String.valueOf(e.getWeight());
            graphEdgeList.insertEdge(from,
                    to, from + "-" + to + ", weight:" + weight);
            graphView.update();
        });
    }

    private void handleGraphWindow(String title) {
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphEdgeList = new GraphEdgeList<>();
        graphView = new SmartGraphPanel<>(graphEdgeList, strategy);
        graphView.automaticLayoutProperty.setValue(autoLayout.isSelected());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(new Image("file:///" + new File("" +
                "src/main/resources/images/icon.png").getAbsolutePath()));
        Scene scene = new Scene(graphView, 800, 800, false,
                SceneAntialiasing.BALANCED);
        stage.setScene(scene);
        stage.show();
        graphView.init();
    }

    private void getGraph(int size) {
        String string = matrixArea.getText().replaceAll("\\D+", "-");
        StringTokenizer tokenizer = new StringTokenizer(string, "-");
        if (tokenizer.countTokens() < size) {
            length.setText("?");
            return;
        }
        graph = new Graph(size);
        for (int i = 0; i < size * size; i++) {
            int weight = Integer.parseInt(tokenizer.nextToken());
            if (weight == 0) {
                continue;
            }
            graph.addEdge(new Edge(i / size, i % size, weight));
        }
    }

    private Graph kruskalMSTAlgorithm() {
        Queue<Edge> edges = graph.getEdges()
                .stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        Graph mst = new Graph(graph.getSize());
        Graph.Subset[] subsets = initialize(graph.getSize());
        while (mst.getEdges().size() < graph.getSize() - 1) {
            Edge next = edges.poll();
            if (next == null) return null;
            var p = find(subsets, next.getFrom());
            var q = find(subsets, next.getTo());
            if (p != q) {
                merge(subsets, p, q);
                mst.addEdge(next);
            }
        }
        return mst;
    }

    private void merge(Graph.Subset[] subsets, int p, int q) {
        int pRoot = find(subsets, p);
        int qRoot = find(subsets, q);
        subsets[pRoot].setParent(qRoot);
    }

    private int find(Graph.Subset[] subsets, Integer i) {
        if (!subsets[i].getParent().equals(i)) {
            subsets[i].setParent(find(subsets, subsets[i].getParent()));
        }
        return subsets[i].getParent();
    }

    private Graph.Subset[] initialize(Integer size) {
        var result = new Graph.Subset[size];
        for (int i = 0; i < size; i++) {
            result[i] = new Graph.Subset();
            result[i].setParent(i);
        }
        return result;
    }
}
