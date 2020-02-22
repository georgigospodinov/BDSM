package beans;

import lombok.Value;

@Value
public final class Person {
    private final String name;
    private final int age;

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        Person other = (Person) o;
        // Dummy .equals for bdsm.simple.UnorderedArrayTest#indexOfProper
        return this.name.equals(other.name);
    }
}
