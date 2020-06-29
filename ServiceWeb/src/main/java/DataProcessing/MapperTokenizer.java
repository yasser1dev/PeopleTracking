package DataProcessing;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.ensetm.serviceweb.entities.GpsLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MapperTokenizer extends Mapper<Object, Text,Text, IntWritable> {
    private final IntWritable one=new IntWritable(1);
    private final IntWritable zeor=new IntWritable(0);
    private Text citizensId = new Text();
    private static final Pattern SEPARATORLine = Pattern.compile("[\\s':]+");
    private static final Pattern SEPARATOR = Pattern.compile("[\\s',]+");



    public void map(Object object, Text value, Mapper.Context context) throws IOException, InterruptedException {
        String line=value.toString();
        //for(String s:SEPARATORLine.split(line)){
            String[] data=SEPARATOR.split(line);
            for(GpsLog gpsLogData:Processing.sickGpsData){
                //System.out.println("id:"+gpsLogData.getId()+" , lat:"+gpsLogData.getLatitude()+" ,long:"+gpsLogData.getLongitude());
                double distance=calculateDistance(gpsLogData.getLatitude(),Double.valueOf(data[1]),gpsLogData.getLongitude(),Double.valueOf(data[2]));
                citizensId.set(data[0]);
                if(distance<100){
                    context.write(citizensId, one);
                }

                //System.out.println("id:"+data[0]+" , lat:"+data[1]+" ,long:"+data[2]+" distance:"+distance);
            }

        //}

    }




    public double calculateDistance(double lat1,double lat2,double lon1,double lon2){
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;// Radius of earth in kilometers.

        double distance =r*c*Math.pow(10,3);

        return distance;

    }

}
