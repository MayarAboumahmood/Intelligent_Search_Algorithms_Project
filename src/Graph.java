import java.util.*;

public class Graph {
    List<Road> roads = new ArrayList<>();
    Station home;
    public static List<Bus> buses = new ArrayList<>();
    public List<State> closed = new ArrayList<>();
    public List<State> visited = new ArrayList<>();

    PriorityQueue<State> nextStatesWithFastestWay = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if ((heuristic(o1.currentStation) + o1.timeSpent) == (heuristic(o2.currentStation) + o2.timeSpent)) {
                return (int) (o1.timeSpent - o2.timeSpent);
            }
            return (int) ((heuristic(o1.currentStation) + o1.timeSpent) - (heuristic(o2.currentStation) + o2.timeSpent));
        }
    });

    PriorityQueue<State> nextStatesWithBestPower = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return (int) ((heuristic(o2.currentStation) + o2.health) - (heuristic(o1.currentStation) + o1.health));
        }
    });

    PriorityQueue<State> nextStatesWithCheapestWay = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {

            return (int) ((heuristic(o2.currentStation) + o2.money) - (heuristic(o1.currentStation) + o1.money));
        }
    });

    PriorityQueue<State> nextBestOverall = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {

            return (int) ((heuristic(o2.currentStation) + all(o2)) - (heuristic(o1.currentStation) + all(o1)));
        }
    });

    Queue<State> next = new LinkedList<>();

    PriorityQueue<State> cheapestSol = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return (int) (o2.money - o1.money);
        }
    });
    PriorityQueue<State> fastestSol = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return (int) ((o1.timeSpent + heuristic(o1.currentStation)) - +(o2.timeSpent + heuristic(o2.currentStation)));
        }
    });
    PriorityQueue<State> healthiestSol = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o2.health == o1.health) {
                return (int) ((int) o1.timeSpent - o2.timeSpent);
            }
            return (int) (o2.health - o1.health);
        }
    });

    PriorityQueue<State> overallSol = new PriorityQueue<>(new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return (int) (all(o2) - all(o1));
        }
    });


    public void dijkstra(State current) {

        visited.add(current);
        if (current.currentStation.equals(home)) {
            fastestSol.add(current);
            return;
        }
        Queue<Road> nextRoad = new LinkedList<>(getNextRoads(current.currentStation));

        while (!nextRoad.isEmpty()) {
            Road temp = nextRoad.remove();
            next.addAll(current.getNextStates(current, temp));
        }


        while (!next.isEmpty()) {
            State temp = next.remove();
            if (temp.money > 0 && temp.health > 0) {
                dijkstra(temp);
            }
        }

    }

    public void fastestWay(State current) {

        visited.add(current);

        if (closed.contains(current)) {
            State temp = closed.get(closed.indexOf(current));
            if (temp.timeSpent < current.timeSpent) {
                return;
            } else {
                closed.remove(temp);
                closed.add(current);
            }
        }


        if (current.currentStation.equals(home)) {
            fastestSol.add(current);
            return;
        }

        closed.add(current);
        Queue<Road> nextRoad = new LinkedList<>(getNextRoads(current.currentStation));

        while (!nextRoad.isEmpty()) {
            Road temp = nextRoad.remove();
            nextStatesWithFastestWay.addAll(current.getNextStates(current, temp));
        }


        while (!nextStatesWithFastestWay.isEmpty()) {
            State temp = nextStatesWithFastestWay.remove();
            if (temp.money > 0 && temp.health > 0) {
                fastestWay(temp);
            }
        }

    }

    public void bestOverall(State current) {

        visited.add(current);

        if (closed.contains(current)) {
            State temp = closed.get(closed.indexOf(current));
            if (all(temp) > all(current)) {
                return;
            } else {
                closed.remove(temp);
                closed.add(current);
            }
        }


        if (current.currentStation.equals(home)) {
            overallSol.add(current);
            return;
        }

        closed.add(current);
        Queue<Road> nextRoad = new LinkedList<>(getNextRoads(current.currentStation));

        while (!nextRoad.isEmpty()) {
            Road temp = nextRoad.remove();
            nextBestOverall.addAll(current.getNextStates(current, temp));
        }


        while (!nextBestOverall.isEmpty()) {
            State temp = nextBestOverall.remove();
            if (temp.money > 0 && temp.health > 0) {
                bestOverall(temp);
            }
        }

    }

    public void cheapestWay(State current) {

        visited.add(current);

        if (closed.contains(current)) {
            State temp = closed.get(closed.indexOf(current));
            if (temp.money > current.money) {
                return;
            } else {
                closed.remove(temp);
                closed.add(current);
            }
        }


        if (current.currentStation.equals(home)) {
            cheapestSol.add(current);
            return;
        }

        closed.add(current);
        Queue<Road> nextRoad = new LinkedList<>(getNextRoads(current.currentStation));

        while (!nextRoad.isEmpty()) {
            Road temp = nextRoad.remove();
            nextStatesWithCheapestWay.addAll(current.getNextStates(current, temp));
        }


        while (!nextStatesWithCheapestWay.isEmpty()) {
            State temp = nextStatesWithCheapestWay.remove();
            if (temp.money > 0 && temp.health > 0) {
                cheapestWay(temp);
            }
        }

    }

    public void healthiestWay(State current) {

        visited.add(current);

        if (closed.contains(current)) {
            State temp = closed.get(closed.indexOf(current));
            if (temp.health > current.health) {
                return;
            } else {
                closed.remove(temp);
                closed.add(current);
            }
        }
        if (current.currentStation.equals(home)) {
            healthiestSol.add(current);
            return;
        }
        closed.add(current);
        Queue<Road> nextRoad = new LinkedList<>(getNextRoads(current.currentStation));

        while (!nextRoad.isEmpty()) {
            Road temp = nextRoad.remove();
            nextStatesWithBestPower.addAll(current.getNextStates(current, temp));
        }


        while (!nextStatesWithBestPower.isEmpty()) {
            State temp = nextStatesWithBestPower.remove();
            if (temp.money > 0 && temp.health > 0) {
                healthiestWay(temp);
            }
        }

    }


    public float heuristic(Station s) {
        float k = (float) Math.sqrt(Math.pow((home.x - s.x), 2) + Math.pow((home.y - s.y), 2));
        return k;

    }

    float all(State state) {

        float all = 0;
        all += state.money * 2;
        all += (state.health) * 500 / 5;
        all -= state.timeSpent * 1000 / 5;
        return all;
    }

    public List<Road> getNextRoads(Station current) {
        List<Road> nextRoads = new ArrayList<>();
        for (Road r : roads
        ) {
            if (r.src.equals(current)) {
                nextRoads.add(r);
            }
        }
        return nextRoads;
    }
}
