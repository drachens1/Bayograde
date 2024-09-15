package org.drachens.util;

import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.Provinces.ProvinceManager;

import java.util.*;

public class AStarPathfinder {
    private final Country country;
    private final ProvinceManager provinceManager;
    private final AStarPathfinderVoids pathfinderVoids;

    public AStarPathfinder(Country country, ProvinceManager provinceManager, AStarPathfinderVoids pathfinderVoids) {
        this.country = country;
        this.provinceManager = provinceManager;
        this.pathfinderVoids = pathfinderVoids;
    }

    public List<Province> findPath(Province start, Province goal, Country country) {
        Set<ProvinceKey> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>((n1, n2) -> {
            if (n1.getPriority() != n2.getPriority()) {
                return Double.compare(n1.getPriority(), n2.getPriority());
            } else {
                return Double.compare(n1.getF(), n2.getF());
            }
        });
        Map<ProvinceKey, Node> allNodes = new HashMap<>();

        ProvinceKey startKey = new ProvinceKey(start);
        Node startNode = new Node(start, null, 0, estimateDistance(start, goal), pathfinderVoids.calcPrio(start));
        openSet.add(startNode);
        allNodes.put(startKey, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            Province currentProvince = currentNode.getProvince();
            ProvinceKey currentKey = new ProvinceKey(currentProvince);

            if (currentProvince.equals(goal)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentKey);

            for (Province neighbor : getNeighbors(currentProvince, country)) {
                ProvinceKey neighborKey = new ProvinceKey(neighbor);
                if (closedSet.contains(neighborKey)) continue;

                double tentativeG = currentNode.getG() + currentProvince.distance(neighbor);
                Node neighborNode = allNodes.getOrDefault(neighborKey, new Node(neighbor, null, Double.MAX_VALUE, estimateDistance(neighbor, goal), pathfinderVoids.calcPrio(neighbor)));

                if (tentativeG < neighborNode.getG()) {
                    neighborNode.setPrevious(currentNode);
                    neighborNode.setG(tentativeG);
                    neighborNode.setF(tentativeG + neighborNode.getH());
                    neighborNode.setPriority(pathfinderVoids.calcPrio(neighbor));

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                    allNodes.put(neighborKey, neighborNode);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Province> reconstructPath(Node node) {
        List<Province> path = new ArrayList<>();
        while (node != null) {
            path.add(node.getProvince());
            node = node.getPrevious();
        }
        Collections.reverse(path);
        return path;
    }

    private double estimateDistance(Province start, Province goal) {
        return start.distance(goal);
    }

    private List<Province> getNeighbors(Province province, Country country) {
        List<Province> neighbors = new ArrayList<>();
        int[] deltas = {-1, 0, 1};
        int y = province.getPos().blockY();

        for (int dx : deltas) {
            for (int dz : deltas) {
                if (dx == 0 && dz == 0) continue;
                Province check = provinceManager.getProvince(province.getPos().add(dx, 0, dz));
                if (pathfinderVoids.isWalkable(check, country)) {
                    neighbors.add(check);
                }
            }
        }
        return neighbors;
    }

    private static class Node {
        private final Province Province;
        private final double h;
        private Node previous;
        private double g;
        private double f;
        private double priority;

        public Node(Province Province, Node previous, double g, double h, double priority) {
            this.Province = Province;
            this.previous = previous;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.priority = priority;
        }

        public Province getProvince() {
            return Province;
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

        public double getH() {
            return h;
        }

        public double getF() {
            return f;
        }

        public void setF(double f) {
            this.f = f;
        }

        public double getPriority() {
            return priority;
        }

        public void setPriority(double priority) {
            this.priority = priority;
        }
    }

    private static class ProvinceKey {
        private final int x, y, z;
        private final String world;

        public ProvinceKey(Province Province) {
            this.x = Province.getPos().blockX();
            this.y = Province.getPos().blockY();
            this.z = Province.getPos().blockZ();
            this.world = Province.getInstance().getDimensionName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProvinceKey that = (ProvinceKey) o;
            return x == that.x && y == that.y && z == that.z && Objects.equals(world, that.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, world);
        }
    }
}
