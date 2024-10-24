import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVReader {
    private Scanner scanner;
    public CSVReader(String path) throws FileNotFoundException {
        File file = new File(path);
        scanner = new Scanner(file);
    }
    public ArrayList<DataVector> read(){
        ArrayList<DataVector> data = new ArrayList<>();
        String[] csvArr;
        double[] dataArr;
        while (scanner.hasNextLine()){
            csvArr = scanner.nextLine().split(",");
            dataArr = new double[csvArr.length];
            for(int i = 0; i < dataArr.length; i++){
                dataArr[i] = Double.parseDouble(csvArr[i]);
            }
            data.add(new DataVector(dataArr));
        }
        return data;
    }
}
