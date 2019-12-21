package Solution;

public class BlockData extends Location {
    private String data;

    BlockData(int lineOffset, int charOffset, String data) {
        super(lineOffset, charOffset);
        this.data = data;
    }

    String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
