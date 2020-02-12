package augustana.birdcounter;

public class Bird {

    String bName;
    int bCount;

    public Bird(String name, int count) {
        bName = name;
        bCount = count;
    }

    public String getBirdName() {
        return bName;
    }

    public int getBirdCount() {
        return bCount;
    }
}
