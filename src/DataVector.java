import java.util.Arrays;

public class DataVector implements Cloneable{
    private double[] dataArray;

    public DataVector(double[] inputData){
        dataArray = new double[inputData.length];
        System.arraycopy(inputData, 0, dataArray, 0, inputData.length);
    }

    public DataVector(int size){
        dataArray = new double[size];
    }

    public double[] getArray() {
        return dataArray;
    }

    public int getSize(){
        return dataArray.length;
    }

    public DataVector divide(int value){
        for(int i = 0; i < dataArray.length; i++){
            dataArray[i] /= value;
        }
        return this;
    }

    public void clear(){
        Arrays.fill(dataArray, 0);
    }

    public double mod2(DataVector dataVector){
        double result = 0;
        double[] b = dataVector.getArray();
        for(int i = 0; i < dataArray.length; i++){
            result += Math.pow(dataArray[i] - b[i], 2);
        }
        return result;
    }

    public DataVector add(DataVector dataVector) throws VectorSizeException {
        if(dataArray.length != dataVector.getSize()){
            throw new VectorSizeException();
        }
        double[] b = dataVector.getArray();

        for(int i = 0; i < dataArray.length; i++){
            dataArray[i] += b[i];
        }
        return this;
    }

    @Override
    public DataVector clone() {
        try {
            DataVector clone = (DataVector) super.clone();
            clone.dataArray = dataArray.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class VectorSizeException extends Exception{
        VectorSizeException(){
            super("Provided wrong vector size!");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DataVector) || dataArray.length != ((DataVector) obj).getSize())
            return false;

        if(obj == this)
            return true;

        double[] b = ((DataVector) obj).getArray();
        for(int i = 0; i < dataArray.length; i++){
            if(dataArray[i] != b[i])
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("[ ");
        for(double j : dataArray)
            result.append(j).append(", ");
        result.delete(result.length()-2, result.length()).append(" ]");
        return result.toString();
    }
}
