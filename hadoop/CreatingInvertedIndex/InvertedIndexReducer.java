//package CreatingInvertedIndex;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> map=new HashMap<String,Integer>();
		
		for(Text term:values) {
			String str=term.toString();
			
			if(map.containsKey(str)) {
				map.put(str,map.get(str)+1);
				
			}else {
				map.put(str, 1);
				
			}	
			
		}
		
		String res="";
		boolean ifFirst=true;
		
		for(String key:map.keySet()) {
			if(ifFirst) {
				res=key+"@"+map.get(key);
				ifFirst=false;
			}else {
			res=res+","+key+"@"+map.get(key);
			}
		}
		
		context.write(_key, new Text(res));
	}

}
