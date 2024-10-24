import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class GraphingData extends JPanel {
    private static ArrayList<Color> colors = new ArrayList<>();
    final int PAD = 20;

    private static int dimension;
    private volatile static ArrayList<Cluster> clusters = new ArrayList<>();
    private volatile static ArrayList<Cluster> clusters_last = new ArrayList<>();
    private static ArrayList<DataVector> inputData = new ArrayList<>();
    private Thread mainThread;

    public  GraphingData(String path) throws FileNotFoundException {
        ArrayList<DataVector> vectors = new CSVReader(path).read();
        dimension = vectors.get(0).getSize();
        inputData = new ArrayList<>(vectors);
    }

    public void init(int k){
        clusters = new ArrayList<>();
        clusters_last = new ArrayList<>();
        ArrayList<DataVector> vectors = new ArrayList<>(inputData);
        for(int i = 0; i < k; i++){
            colors.add(new Color((int)(Math.random() * 0x1000000)));
        }
        k = Math.min(k, vectors.size());
        for(int i = 0; i < k; i++){
            clusters.add(new Cluster(dimension));
        }

        while (!vectors.isEmpty()){
            for(Cluster cluster : clusters){
                cluster.add(vectors.remove((int)(Math.random() * (vectors.size() - 1))));
                if(vectors.isEmpty())
                    break;
            }
        }
    }

    public void start() throws InterruptedException {
        mainThread = new Thread(this::run);
        mainThread.start();
    }

    public void stop(){
        if(mainThread != null && mainThread.isAlive())
            mainThread.interrupt();
    }

    private void run() {
        int iteration = 0;
        try {
            do {
                try {
                    calculateCentroids();
                } catch (DataVector.VectorSizeException e) {
                    System.out.println("Wrong vector size!");
                }
                saveClusters();

                for (Cluster cluster : clusters) {
                    cluster.clear();
                }

                TreeMap<Double, Cluster> distances = new TreeMap<>();
                for (DataVector vector : inputData) {
                    for (Cluster cluster : clusters) {
                        Double distance = cluster.getCentroid().mod2(vector);
                        distances.put(distance, cluster);
                    }
                    distances.firstEntry().getValue().add(vector);
                    distances.clear();
                }
                for(Cluster cluster : clusters){
                    if(cluster.getVectors().isEmpty()){
                        cluster.add(clusters.stream().max(Comparator.comparingInt(o -> o.getVectors().size())).get().getVectors().remove(0));
                    }
                }
                printData(iteration++);
                Thread.sleep(64);
                repaint();
            }while (!equalsClusters());
        } catch (InterruptedException ignored) {}
    }


    private static void calculateCentroids() throws DataVector.VectorSizeException {
        for(Cluster cluster : clusters){
            cluster.calculateCentroid();
        }
    }

    public static boolean equalsClusters(){
        for(int i = 0; i < clusters.size(); i++){
            if(!clusters.get(i).equals(clusters_last.get(i)))
                return false;
        }
        return true;
    }

    private static void saveClusters(){
        clusters_last.clear();
        for(Cluster cluster : clusters){
            clusters_last.add(cluster.clone());
        }
    }

    public void printData(int iteration){
        System.out.println("Iteration "+iteration+" -------------------------------------------------");
        for(int i = 0; i < clusters.size(); i++){
            System.out.println("Cluster "+(i+1)+": ");
            for(DataVector vector : clusters.get(i).getVectors()){
                System.out.println("\t"+vector);
            }
            System.out.println("Σ d^2(x, y): " + calculateSum(clusters.get(i)));
        }
    }

    private double calculateSum(Cluster cluster){
        double sum = 0;
        for(DataVector vector_1 : cluster.getVectors()){
            for(DataVector vector_2 : cluster.getVectors()){
                if(!vector_1.equals(vector_2))
                    sum += vector_1.mod2(vector_2);
            }
        }
        return sum;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));

        for(int i = 0; i < inputData.size()-1; i++) {
            int j = 0;
            for(Cluster cluster : clusters){
                if(cluster.getVectors().contains(inputData.get(i))){
                    g2.setPaint(colors.get(j));
                    g2.fill(new Ellipse2D.Double(inputData.get(i).getArray()[0] *100.0-5-100, getHeight() - inputData.get(i).getArray()[1] *100.0-5, 10, 10));

                }
                g2.setPaint(new Color(0,0,0));
                g2.fill(new Ellipse2D.Double(cluster.getCentroid().getArray()[0] *100.0 - 9-100, getHeight() - cluster.getCentroid().getArray()[1] *100.0 - 9, 18, 18));
                g2.setPaint(new Color(250,250, 250));
                g2.fill(new Ellipse2D.Double(cluster.getCentroid().getArray()[0] *100.0 - 7-100, getHeight() - cluster.getCentroid().getArray()[1] *100.0 - 7, 14, 14));
                j++;
            }
        }
        g2.setPaint(Color.red);

    }
}