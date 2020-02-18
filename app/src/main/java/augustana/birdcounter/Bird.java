package augustana.birdcounter;

/**
 * Class to provide the functionality of creating a bird object and implements the comparable
 * interface so that two birds (counts) can be compared.
 */

public class Bird  implements Comparable<Bird> {

    // fields
    String name;
    long count;

    // constructor

    public Bird() {
        this.name = "";
        this.count = (long) 0;
    }

    public Bird(String n, long c) {
        this.name = n;
        this.count = c;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setCount(long n) {
        this.count = n;
    }

    /**
     * Method used to compare the count values of a bird object.
     * @param o - Bird being compared.
     * @return - The difference of the two with the larger result being the preferred value.
     */
    @Override
    public int compareTo(Bird o) {
        return (int) (this.getCount() - o.getCount());
    }
}
