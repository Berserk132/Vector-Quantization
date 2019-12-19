import java.io.Serializable;
import java.util.ArrayList;

public class BookCodes implements Serializable {

    double[][] bookCode;
    double[][] bookCodeAvg;
    ArrayList<Blocks> blocks = new ArrayList<>();

    public BookCodes(double[][] bookCode) {
        this.bookCode = bookCode;
    }

    public BookCodes() {
    }
}
