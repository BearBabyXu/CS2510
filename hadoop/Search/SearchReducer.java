//package Search;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SearchReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	public void reduce(Text _key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
		LongWritable res=new LongWritable();
//		if(_key.toString().equals("test")) {
//			
//			LongWritable temp=new LongWritable();
//			temp.set(1);
//			context.write(_key, new LongWritable(1));
//		}
		long count=0L;
		
		for(LongWritable value:values) {
			count=count+value.get();
		}
		
		res.set(count);
		context.write(_key, res);
		
	}

}
