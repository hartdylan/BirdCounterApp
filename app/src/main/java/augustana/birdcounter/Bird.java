package augustana.birdcounter;



public class Bird {

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

    public void setName(String m) {
        this.name = m;
    }

    public void setCount(long d) {
        this.count = d;
    }
}
