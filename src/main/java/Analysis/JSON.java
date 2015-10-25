package analysis;

public class JSON {

    private String faceId;
    private FaceRectangle faceRectangle;
    private Attributes attributes;

    public String getFaceId() {
        return faceId;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "{\"faceId\":\"" + faceId + "\"," + faceRectangle + "," + attributes;
    }



}
