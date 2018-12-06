//package Search;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SearchMapper extends Mapper<Text, Text, Text, LongWritable> {

	public void map(Text ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		Configuration conf=context.getConfiguration();
		String[] keywords=conf.get("keyWords").split(" ");
		String[] files=ivalue.toString().split(",");
		
		for(String keyWord:keywords) {
			if(ikey.toString().equals(keyWord)) {
				for(String pair:files) {
					String[] info=pair.split("@");
					String path=info[0];
					String frequency=info[1];
					LongWritable sendValue=new LongWritable();
					sendValue.set(Long.valueOf(frequency));
					
					context.write(new Text(path), sendValue);
					
				}
				
			}
			
		}
		
		
//		LongWritable temp=new LongWritable();
//		temp.set(1);;
//		
//		context.write(new Text("test"), temp);
		
	}

}
