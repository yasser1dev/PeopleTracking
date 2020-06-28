package DataProcessing;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.ensetm.serviceweb.entities.GpsLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Processing {
    public static Collection<GpsLog> sickGpsData=new ArrayList();
    public static String outputFile="C:\\Users\\Lenovo\\Desktop\\output";
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        mapReduce();
    }

    public static boolean mapReduce() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs=FileSystem.get(conf);
        Job job = Job.getInstance(conf, "Get Contatcs");
        job.setJarByClass(Processing.class);
        job.setMapperClass(MapperTokenizer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path("src/main/resources/input/gpsDataContact.txt"));
        Path outDir=new Path(outputFile);
        if(fs.isDirectory(outDir)) fs.delete(outDir, true);
        FileOutputFormat.setOutputPath(job, outDir);
        boolean exit=job.waitForCompletion(true);
        return exit;
    }


}
