package ENTITY;

public class sanpham {
    String id;
    String ten;
    String path;
    String time;
    String price;

    public sanpham() {
    }

    public sanpham(String id, String ten, String path, String price) {
        this.id = id;
        this.ten = ten;
        this.path = path;
        this.price = price;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
