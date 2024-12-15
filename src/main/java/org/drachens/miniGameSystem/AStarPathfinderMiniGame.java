package org.drachens.miniGameSystem;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.*;

public class AStarPathfinderMiniGame {

    public List<Pos> findPath(Pos start, Pos goal, Instance instance) {
        Map<Node.LocationKey, Node> closedSet = new HashMap<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Map<Node.LocationKey, Node> allNodes = new HashMap<>();

        Node.LocationKey startKey = new Node.LocationKey(start);
        Node startNode = new Node(start, null, 0, heuristic(start, goal));
        openSet.add(startNode);
        allNodes.put(startKey, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            Pos currentLocation = currentNode.getLocation();
            Node.LocationKey currentKey = new Node.LocationKey(currentLocation);

            if (currentLocation.equals(goal)) {
                return reconstructPath(currentNode);
            }

            closedSet.put(currentKey, currentNode);

            for (Pos neighbor : getNeighbors(currentLocation, instance)) {
                Node.LocationKey neighborKey = new Node.LocationKey(neighbor);
                if (closedSet.containsKey(neighborKey)) continue;

                double tentativeG = currentNode.getG() + currentLocation.distance(neighbor);
                Node neighborNode = allNodes.getOrDefault(neighborKey, new Node(neighbor, null, Double.MAX_VALUE, heuristic(neighbor, goal)));

                if (tentativeG < neighborNode.getG()) {
                    neighborNode.setPrevious(currentNode);
                    neighborNode.setG(tentativeG);

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                    allNodes.put(neighborKey, neighborNode);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Pos> reconstructPath(Node node) {
        List<Pos> path = new ArrayList<>();
        while (node != null) {
            path.add(node.getLocation());
            node = node.getPrevious();
        }
        Collections.reverse(path);
        return path;
    }

    private List<Pos> getNeighbors(Pos pos, Instance instance) {
        List<Pos> neighbors = new ArrayList<>();
        int[] deltas = {-1, 1};
        int x = pos.blockX();
        int y = pos.blockY();

        for (int dx : deltas) {
            for (int dy : deltas) {
                Pos neighbor = new Pos(x + dx, y + dy, pos.blockZ());
                if (isWalkable(neighbor, instance)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    private boolean isWalkable(Pos pos, Instance instance) {
        return instance.getBlock(pos) != Block.AIR;
    }

    private double heuristic(Pos a, Pos b) {
        return a.distance(b);
    }

    private static class Node {
        private final Pos location;
        private Node previous;
        private double g;
        private final double h;

        public Node(Pos location, Node previous, double g, double h) {
            this.location = location;
            this.previous = previous;
            this.g = g;
            this.h = h;
        }

        public Pos getLocation() {
            return location;
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public double getG() {
            return g;
        }

        public void setG(double g) {
            this.g = g;
        }

        public double getF() {
            return g + h; //
        }

        private static class LocationKey {
            private final int x, y;

            public LocationKey(Pos pos) {
                this.x = pos.blockX();
                this.y = pos.blockY();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                LocationKey that = (LocationKey) o;
                return x == that.x && y == that.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
