package Solution;

abstract public class Location {
    protected int lineOffset;
    protected int charOffset;

    Location(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    public int getLineOffset() {
        return this.lineOffset;
    }

    public void setCharOffset(int charOffset) {
        this.charOffset = charOffset;
    }

    public int getCharOffset() {
        return this.charOffset;
    }


}
