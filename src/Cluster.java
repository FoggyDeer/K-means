import java.util.ArrayList;

public class Cluster implements Cloneable{
    private DataVector centroid;
    private ArrayList<DataVector> vectors;

    public Cluster(int vectorDimension) {
        this.centroid = new DataVector(vectorDimension);
        vectors = new ArrayList<>();
    }

    public void add(DataVector vector) {
        vectors.add(vector);
    }

    public void clear() {
        vectors.clear();
    }

    public DataVector getCentroid() {
        return centroid;
    }

    public ArrayList<DataVector> getVectors() {
        return vectors;
    }

    public void calculateCentroid() throws DataVector.VectorSizeException {
        centroid.clear();
        for(DataVector vector : vectors){
            centroid.add(vector);
        }
        centroid.divide(vectors.size());
    }

    public boolean equals(Cluster cluster){
        if(!centroid.equals(cluster.getCentroid()) || vectors.size() != cluster.getVectors().size())
            return false;
        for(DataVector vector : vectors){
            if(!cluster.getVectors().contains(vector))
                return false;
        }
        return true;
    }


    @Override
    public Cluster clone() {
        try {
            Cluster clone = (Cluster) super.clone();
            clone.centroid = centroid.clone();
            clone.vectors = new ArrayList<>(vectors);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
