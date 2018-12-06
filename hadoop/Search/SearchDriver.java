//package Search;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class SearchDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String inputSrc=args[0];
		String outputDes=args[1];
		String keyWords="";
		boolean firstKeyWord=true;
		for(int i=2;i<args.length;i++) {
			if(firstKeyWord) {
				keyWords=args[i];
				
			}else {
				
				keyWords=keyWords+" "+args[i];
			}
			
			
		}
		
		conf.set("keyWords",keyWords);
		
		Job job = Job.getInstance(conf, "Search");
		job.setJarByClass(SearchDriver.class);
		// TODO: specify a mapper
		job.setMapperClass(SearchMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(SearchReducer.class);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputDirRecursive(job,true);
		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(inputSrc));
		FileOutputFormat.setOutputPath(job, new Path(outputDes));

		System.exit(job.waitForCompletion(true)?0:1);
	}

}
