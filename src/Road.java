import java.util.Objects;

public class Road {
    Station src, des;
    float distance;
    boolean havingTaxi;
    float busSpeed, taxiSpeed;

    public Road(Station src, Station des, float distance, boolean havingTaxi, float taxiSpeed, float busSpeed) {
        this.src = src;
        this.des = des;
        this.distance = distance * 1000;
        this.busSpeed = busSpeed * 1000 / 60;
        this.taxiSpeed = taxiSpeed * 1000 / 60;
        this.havingTaxi = havingTaxi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return road.src.equals(this.src) && road.des.equals(this.des);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, des, distance, havingTaxi, busSpeed, taxiSpeed);
    }
}
