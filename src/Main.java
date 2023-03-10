import java.util.List;
/*get the shortest path from the current place to the home(and cheapest and healthiest)
using A star algorithm and get the optimal solution with some additions to the A star,
when every station I go through have different waiting time for bus/taxi when I have a limited money/health besides not every bus go on every road,
and some road just for walk,
and every road have a different speed for taxi and for bus.*/

public class Main {
    public static void main(String[] args) {

        Station s1 = new Station("1", 0, 0, 1, 0);
        Station s2 = new Station("2", 0.5f, -1, 1, 0);
        Station s3 = new Station("3", 2, 0, 5, 2);
        Station s4 = new Station("4", 2, 1, 5, 2);
        Station s5 = new Station("5", 3, -1, 3, 2);
        Station s6 = new Station("6", 2.5F, 2, 3, 1);
        Station s7 = new Station("7", 4, 0.7F, 5, 2);
        Station s8 = new Station("8", 4, 1, 3, 2);
        Station s9 = new Station("9", 6, 2, 0, 0);


        Road s1s2 = new Road(s1, s2, 0.5f, false, 0, 0);
        Road s1s3 = new Road(s1, s3, 2, true, 55, 30);
        Road s1s4 = new Road(s1, s4, 3, true, 60, 40);
        Road s2s5 = new Road(s2, s5, 5, true, 85, 60);
        Road s3s6 = new Road(s3, s6, 2, true, 60, 40);
        Road s3s7 = new Road(s3, s7, 5, true, 80, 55);
        Road s4s6 = new Road(s4, s6, 0.7f, false, 0, 0);
        Road s5s7 = new Road(s5, s7, 3, true, 65, 40);
        Road s6s7 = new Road(s6, s7, 4, true, 70, 45);
        Road s6s8 = new Road(s6, s8, 3, true, 60, 40);
        Road s7s8 = new Road(s7, s8, 0.4f, false, 0, 0);
        Road s7s9 = new Road(s7, s9, 5, true, 85, 60);
        Road s8s9 = new Road(s8, s9, 4, true, 70, 45);

        Graph g = new Graph();
        g.roads.add(s1s2);
        g.roads.add(s1s3);
        g.roads.add(s1s4);
        g.roads.add(s2s5);
        g.roads.add(s3s6);
        g.roads.add(s3s7);
        g.roads.add(s4s6);
        g.roads.add(s5s7);
        g.roads.add(s6s7);
        g.roads.add(s6s8);
        g.roads.add(s7s8);
        g.roads.add(s7s9);
        g.roads.add(s8s9);

        g.home = s9;

        Bus b1 = new Bus();
        b1.roads.add(s1s3);
        b1.roads.add(s3s6);
        b1.roads.add(s5s7);
        b1.roads.add(s3s7);
        b1.roads.add(s6s8);


        Bus b2 = new Bus();
        b2.roads.add(s1s4);
        b2.roads.add(s2s5);
        b2.roads.add(s5s7);
        b2.roads.add(s8s9);

        Graph.buses.add(b1);
        Graph.buses.add(b2);

        State start = new State(100, 5000, 0, s1, null, false, null, "start");

//
//        g.fastestWay(start);
//        State fastest = g.fastestSol.remove();
//        System.out.println("fastest road is :");
//        while (fastest != null) {
//            System.out.print("Station : ");
//            System.out.println(fastest.currentStation.name);
//            System.out.print("time : ");
//            System.out.println(fastest.timeSpent);
//            System.out.print("way : ");
//            System.out.println(fastest.reachedBy);
//            System.out.print("cash : ");
//            System.out.println(fastest.money);
//            System.out.print("health : ");
//            System.out.println(fastest.health);
//            System.out.println("==============");
//            fastest = fastest.comingFrom;
//        }
//        System.out.print("visited Nodes: ");
//        System.out.println(g.visited.size());
//        System.out.println("====================================================");
//
//        g.visited.clear();
//
//
//        g.healthiestWay(start);
//        State healthiest = g.healthiestSol.remove();
//        System.out.println("healthiest road is :");
//        while (healthiest != null) {
//            System.out.print("Station : ");
//            System.out.println(healthiest.currentStation.name);
//            System.out.print("time : ");
//            System.out.println(healthiest.timeSpent);
//            System.out.print("way : ");
//            System.out.println(healthiest.reachedBy);
//            System.out.print("cash : ");
//            System.out.println(healthiest.money);
//            System.out.print("health : ");
//            System.out.println(healthiest.health);
//            System.out.println("==============");
//            healthiest = healthiest.comingFrom;
//        }
//        System.out.print("visited Nodes: ");
//        System.out.println(g.visited.size());
//        System.out.println("====================================================");
//        g.visited.clear();
//
//        g.cheapestWay(start);
//        State cheapest = g.cheapestSol.remove();
//        System.out.println("cheapest road is :");
//        while (cheapest != null) {
//            System.out.print("Station : ");
//            System.out.println(cheapest.currentStation.name);
//            System.out.print("time : ");
//            System.out.println(cheapest.timeSpent);
//            System.out.print("way : ");
//            System.out.println(cheapest.reachedBy);
//            System.out.print("cash : ");
//            System.out.println(cheapest.money);
//            System.out.print("health : ");
//            System.out.println(cheapest.health);
//            System.out.println("==============");
//            cheapest = cheapest.comingFrom;
//        }
//        System.out.print("visited Nodes: ");
//        System.out.println(g.visited.size());
//        System.out.println("====================================================");
//        g.visited.clear();
//
//        g.bestOverall(start);
//        State oa = g.overallSol.remove();
//        System.out.println("best overall road is :");
//        while (oa != null) {
//            System.out.print("Station : ");
//            System.out.println(oa.currentStation.name);
//            System.out.print("time : ");
//            System.out.println(oa.timeSpent);
//            System.out.print("way : ");
//            System.out.println(oa.reachedBy);
//            System.out.print("cash : ");
//            System.out.println(oa.money);
//            System.out.print("health : ");
//            System.out.println(oa.health);
//            System.out.println("==============");
//            oa = oa.comingFrom;
//        }
//        System.out.print("visited Nodes: ");
//        System.out.println(g.visited.size());
//        System.out.println("====================================================");
//        g.visited.clear();
//    }


        State state=start;
        System.out.println("best road in normal a star is :");
        while (true) {
            System.out.print("Station : ");
            System.out.println(state.currentStation.name);
            System.out.print("time : ");
            System.out.println(state.timeSpent);
            System.out.print("way : ");
            System.out.println(state.reachedBy);
            System.out.print("cash : ");
            System.out.println(state.money);
            System.out.print("health : ");
            System.out.println(state.health);
            System.out.println("==============");
            if(state.getSimpleNextStatesForAStar(g).isEmpty()){
                break;
            }
            state=state.getTheBestNextState(state.getSimpleNextStatesForAStar(g),g);
        }
        if(!state.currentStation.equals(g.home)){
            System.out.println("You can't reach the home! :(");
        }else{
            System.out.println("Welcome Home :)");
        }

        System.out.println("====================================================");
    }
}