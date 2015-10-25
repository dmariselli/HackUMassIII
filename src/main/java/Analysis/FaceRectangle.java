package Analysis;

public class FaceRectangle {

    private int top;
    private int left;
    private int width;
    private int height;

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "\"faceRectangle\":{\"top\":" + top + ",\"left\":" + left + ",\"width\":" + width + ",\"height\":" + height + "}";
    }
}
