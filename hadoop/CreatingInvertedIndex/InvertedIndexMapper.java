//package CreatingInvertedIndex;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		String filePath=((FileSplit)context.getInputSplit()).getPath().toString();
		String line=ivalue.toString().toLowerCase();
		
		String words[]=line.split(" ");
		for(String term:words) {
			if(term.length()!=0) {
			char initial=term.charAt(0);
			if(initial>=97 && initial <=122) {
				context.write(new Text(term), new Text(filePath));
				
			}
			
		}
		}
		
	}

}
