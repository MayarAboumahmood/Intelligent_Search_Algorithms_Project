import java.util.Objects;

public class Station {
    float x, y;
    String name;

    public Station(String name, float x, float y, float waitingTimeBus, float waitingTimeTaxi) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.waitingTimeBus = waitingTimeBus;
        this.waitingTimeTaxi = waitingTimeTaxi;
    }

    float waitingTimeBus, waitingTimeTaxi;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Float.compare(station.x, x) == 0 && Float.compare(station.y, y) == 0 && Float.compare(station.waitingTimeBus, waitingTimeBus) == 0 && Float.compare(station.waitingTimeTaxi, waitingTimeTaxi) == 0 && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name, waitingTimeBus, waitingTimeTaxi);
    }
}
