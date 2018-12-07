//package CreatingInvertedIndex;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		String filePath=((FileSplit)context.getInputSplit()).getPath().toString();
		
		String eliminaters = " |$&-!@?^*_=+/#[]/\t/\r;:,.()\'\"";
		String RE_EXP1 = "[a-zA-Z]+_[a-zA-Z]+";
		String RE_EXP2 = "[a-zA-Z]+";
		
		StringTokenizer itr = new StringTokenizer(ivalue.toString(), eliminaters);
		String word = "";
		while(itr.hasMoreTokens()) {
			word = itr.nextToken().toLowerCase();
			if(word.matches(RE_EXP1) || word.matches(RE_EXP2)) {
				context.write(new Text(word), new Text(filePath));
			}
		}
		
		/*
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
		*/
		
	}

}
