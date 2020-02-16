package augustana.birdcounter;

public class Bird  implements Comparable<Bird> {

    String name;
    long count;

    public Bird() {

    }

    public Bird(String n, long c) {
        this.name = n;
        this.count = c;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    @Override
    public int compareTo(Bird o) {
        return (int) (this.getCount() - o.getCount());
    }
}
