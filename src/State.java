import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
    float health, money, timeSpent;
    Station currentStation;
    State comingFrom;
    boolean inBus;
    Bus currentBus;
    String reachedBy;

    public State(float health, float money, float timeSpent, Station currentStation, State comingFrom, boolean inBus, Bus currentBus, String reachedBy) {
        this.health = health;
        this.money = money;
        this.timeSpent = timeSpent;
        this.currentStation = currentStation;
        this.comingFrom = comingFrom;
        this.inBus = inBus;
        this.currentBus = currentBus;
        this.reachedBy = reachedBy;
    }


    //get next states for a road with multiple choices
    public List<State> getNextStates(State current, Road road) {
        List<State> nextStates = new ArrayList<>();

        if (road.havingTaxi) {
            //Taxi State
            nextStates.add(new State(
                    current.health + healthCost(2, road),
                    current.money - moneyCost(2, road),
                    current.timeSpent + timeCost(2, road, current.currentStation),
                    road.des,
                    current,
                    false,
                    null,
                    "Taxi"));


            //searching for a bus
            Bus b = null;
            for (Bus bus : Graph.buses
            ) {
                if (bus.roads.contains(road)) {
                    b = bus;
                    break;
                }
            }

            //if in bus or not
            if (current.inBus) {
                //if in bus and the bus can take the road
                if (current.currentBus.roads.contains(road)) {
                    nextStates.add(new State(
                            current.health + healthCost(1, road),
                            current.money,
                            current.timeSpent + timeCost(1, road, current.currentStation) - current.currentStation.waitingTimeBus,
                            road.des,
                            current,
                            true,
                            current.currentBus,
                            "Bus"));

                }
                // if in bus and the bus can`t take the road
                else {
                    if (b != null) {
                        nextStates.add(new State(
                                current.health + healthCost(1, road),
                                current.money - moneyCost(1, road),
                                current.timeSpent + timeCost(1, road, current.currentStation),
                                road.des,
                                current,
                                true,
                                b,
                                "Bus"));
                    }

                }


            }
            // if not in the bus
            else {
                if (b != null) {
                    nextStates.add(new State(
                            current.health + healthCost(1, road),
                            current.money - moneyCost(1, road),
                            current.timeSpent + timeCost(1, road, current.currentStation),
                            road.des,
                            current,
                            true,
                            b,
                            "Bus"));
                }

            }

        }

        nextStates.add(new State(
                current.health + healthCost(0, road),
                current.money,
                current.timeSpent + timeCost(0, road, current.currentStation),
                road.des,
                current,
                false,
                null,
                "walking"));

        return nextStates;
    }

    //get the money cost
    public float moneyCost(int type, Road road) {
        return switch (type) {
            case 1 -> 400;
            case 2 -> road.distance;
            default -> 0;
        };
    }

    //get the health cost
    public float healthCost(int type, Road road) {
        return switch (type) {
            case 1 -> -5 * road.distance / 1000;
            case 2 -> 5 * road.distance / 1000;
            default -> -10 * road.distance / 1000;
        };
    }

    //get the time cost
    public float timeCost(int type, Road road, Station station) {
        return switch (type) {
            case 1 -> station.waitingTimeBus + road.distance / road.busSpeed;
            case 2 -> station.waitingTimeTaxi + (road.distance) / road.taxiSpeed;
            default -> road.distance / (5.5f * 1000 / 60);
        };


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(currentStation, state.currentStation) && Objects.equals(reachedBy, state.reachedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentStation);
    }


    Station s1 = new Station("1", 0, 0, 1, 0);

    State getTheBestNextState(List<State> nextStates, Graph g) {
        State bestNextState = new State(100, 5000, 121231, s1, null, false, null, "start");
        for (int i = 0; i < nextStates.size(); i++) {

            if (bestNextState.timeSpent + g.heuristic(bestNextState.currentStation) > nextStates.get(i).timeSpent + g.heuristic(nextStates.get(i).currentStation)) {
                bestNextState = nextStates.get(i);
            }

        }
        return bestNextState;
    }


    //get the next state for the simple a star algorithm
    public List<State> getSimpleNextStatesForAStar(Graph g) {

        List<State> nextStates = new ArrayList<>();

        for (int i = 0; i < g.getNextRoads(this.currentStation).size(); i++) {

            Road road = g.getNextRoads(this.currentStation).get(i);

            if (road.havingTaxi) {
                if (moneyCost(2, road) <= this.money) {
                    //Taxi State
                    nextStates.add(new State(
                            this.health + healthCost(2, road),
                            this.money - moneyCost(2, road),
                            this.timeSpent + timeCost(2, road, this.currentStation),
                            road.des,
                            this,
                            false,
                            null,
                            "Taxi"));
                }
                //searching for a bus
                Bus b = null;
                for (Bus bus : Graph.buses
                ) {
                    if (bus.roads.contains(road)) {
                        b = bus;
                        break;
                    }
                }
                //if in bus or not
                if (this.inBus) {
//                    if in bus and the bus can take the road
                    if (this.currentBus.roads.contains(road)) {
                        if (-healthCost(1, road) < this.health) {

                            nextStates.add(new State(
                                    this.health + healthCost(1, road),
                                    this.money,
                                    this.timeSpent + timeCost(1, road, this.currentStation) - this.currentStation.waitingTimeBus,
                                    road.des,
                                    this,
                                    true,
                                    this.currentBus,
                                    "Bus"));
                        }
                    }
                    // if in bus and the bus can`t take the road
                    else {
                        if (b != null) {
                            if (moneyCost(1, road) <= this.money) {
                                if (-healthCost(1, road) < this.health) {
                                    nextStates.add(new State(
                                            this.health + healthCost(1, road),
                                            this.money - moneyCost(1, road),
                                            this.timeSpent + timeCost(1, road, this.currentStation),
                                            road.des,
                                            this,
                                            true,
                                            b,
                                            "Bus"));
                                }
                            }
                        }
                    }
                }
                // if not in the bus
                else {
                    if (b != null) {
                        if ((moneyCost(1, road) <= this.money) && (-healthCost(1, road) < this.health)) {
                            nextStates.add(new State(
                                    this.health + healthCost(1, road),
                                    this.money - moneyCost(1, road),
                                    this.timeSpent + timeCost(1, road, this.currentStation),
                                    road.des,
                                    this,
                                    true,
                                    b,
                                    "Bus"));
                        }
                    }

                }
            }
            if (-healthCost(0, road) < this.health) {

                nextStates.add(new State(

                        this.health + healthCost(0, road),
                        this.money,
                        this.timeSpent + timeCost(0, road, this.currentStation),
                        road.des,
                        this,
                        false,
                        null,
                        "walking"));
            }
        }
        return nextStates;
    }
}
