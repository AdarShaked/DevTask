package Solution;

public class WordLocation extends Location {

    WordLocation(int lineOffset, int charOffset) {
        super(lineOffset, charOffset);
    }

    @Override
    public String toString() {
        return "[lineOffset= " + lineOffset + ", charOffset=" + charOffset + "]";
    }
}
