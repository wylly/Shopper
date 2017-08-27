package personal.bw.shopper.untils;

import com.google.common.base.Predicate;

import java.util.LinkedList;
import java.util.List;

public class ListFilter <T>
{
	public LinkedList<T> filterResults(List<T> itemList, Predicate<T> predicate)
	{
		LinkedList<T> filteredResults = new LinkedList<>();
		for(T item : itemList){
			if(predicate.apply(item)){
				filteredResults.add(item);
			}
		}
		return filteredResults;
	}
}
