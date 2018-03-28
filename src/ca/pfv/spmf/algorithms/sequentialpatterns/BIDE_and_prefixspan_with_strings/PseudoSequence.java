package ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings;
/* This file is copyright (c) 2008-2013 Philippe Fournier-Viger
* 
* This file is part of the SPMF DATA MINING SOFTWARE
* (http://www.philippe-fournier-viger.com/spmf).
* 
* SPMF is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* SPMF. If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.List;

import ca.pfv.spmf.input.sequence_database_list_strings.Sequence;
/**
 * This represents a Sequence from a projected database (as used in the PrefixSpan algorithm).
 * A projected Sequence keeps a reference to the original Sequence and two pointers indicating respectively (1)
 * at which itemset the Sequence starts and (2) at which item in this itemset the Sequence starts.
 * <br/><br/>
 * 
 * This class is used by PrefixSpan. The subclass PseudoSequenceBIDE is used by the BIDE algorithm
 * 
@see AlgoPrefixSpan_with_Strings
@see Sequence
* @author Philippe Fournier-Viger
 */
class PseudoSequence {

	/** the corresponding Sequence in the original database */
	protected Sequence sequence;
	/** the first itemset of this pseudo-Sequence  in the original Sequence */
	protected int firstItemset; 
	/** the first item of this pseudo-Sequence in the original Sequence */
	protected int firstItem;
	
	/**
	 * Default constructor
	 */
	protected PseudoSequence(){
		
	}

	/**
	 * Create a pseudo-Sequence from a Sequence that is a pseudo Sequence.
	 * @param sequence the original pseudo-Sequence.
	 * @param indexItemset the itemset where the pseudo-Sequence should start in terms of the original Sequence.
	 * @param indexItem the item where the pseudo-Sequence should start in terms of the original Sequence.
	 */
	protected PseudoSequence(PseudoSequence sequence, int indexItemset, int indexItem){
		// remember the original Sequence
		this.sequence = sequence.sequence;
		// record the position of where the pseudo-Sequence starts
		// in terms of the original pseudo-Sequence
		this.firstItemset = indexItemset + sequence.firstItemset;
		if(this.firstItemset == sequence.firstItemset){
			this.firstItem = indexItem + sequence.firstItem;
		}else{
			this.firstItem = indexItem; 
		}
	}
	
	/**
	 * Create a pseudo-Sequence from a Sequence that is an original Sequence.
	 * @param sequence the original Sequence.
	 * @param indexItemset the itemset where the pseudo-Sequence should start in terms of the original Sequence.
	 * @param indexItem the item where the pseudo-Sequence should start in terms of the original Sequence.
	 */
	protected  PseudoSequence(Sequence sequence, int indexItemset, int indexItem){
		// remember the original Sequence
		this.sequence = sequence;
		// remember the starting position of this pseudo-Sequence in terms
		// of the original Sequence.
		this.firstItemset = indexItemset;
		this.firstItem = indexItem;
	}

	/**
	 * Return the size of this pseudo-Sequence in terms of itemsets.
	 * @return the size.
	 */
	protected int size() {
		// the size is the size of the original Sequence minus
		// the itemset where this pseudo-Sequence start
		int size = sequence.size() - firstItemset;
		// if the size is 1 and it the only itemset is empty, return 0
		if(size == 1 && sequence.getItemsets().get(firstItemset).size() == 0){
			return 0;
		}
		// return the size
		return size;
	}

	/**
	 * Return the size in terms of items of an itemset at a given position
	 * (overloaded).
	 * @param index the position of the itemset
	 * @return the number of items in that itemset
	 */
	protected int getSizeOfItemsetAt(int index) {
		// We obtain the size of the itemset by looking at the original
		// Sequence. To obtain the position of the itemset we do
		//   index + firstItemset.
		int size = sequence.getItemsets().get(index + firstItemset).size();
		// if it is the first itemset of the pseudo-Sequence
		if(isFirstItemset(index)){
			// we remove some items if this itemset is cut at left.
			size -=  firstItem;
		}
		return size; // return the size
	}

	/**
	 * Return true if this itemset is cut at left (a postfix).
	 * @param indexItemset the position of the given itemset.
	 * @return true if it is cut at left.
	 */
	protected boolean isPostfix(int indexItemset) {
		// if it is the first itemset of the pseudo-Sequence
		// and it is cut at left, we return true.
		return indexItemset == 0  && firstItem !=0;
	}

	/**
	 * Method to check if an itemset is the first one of a pseudo-Sequence
	 * @param index  the position of an itemset
	 * @return true if it is the first one.
	 */
	protected boolean isFirstItemset(int index) {
		return index == 0;
	}
	
	/**
	 * Method to check if an itemset is the last one of a pseudo-Sequence
	 * @param index  the position of an itemset
	 * @return true if it is the last one.
	 */
	protected boolean isLastItemset(int index) {
		return (index + firstItemset) == sequence.getItemsets().size() -1;
	}

	/**
	 * Get an item at a given position inside a given itemset
	 * @param indexItem the position of the item
	 * @param indexItemset the position of the itemset
	 * @return the item.
	 */
	protected String getItemAtInItemsetAt(int indexItem, int indexItemset) {
		// if it is in the first itemset
		if(isFirstItemset(indexItemset)){
			// we need to consider if the itemset was cut at the left
			// by adding "firstItem"
			return getItemset(indexItemset).get(indexItem + firstItem);
		}else{// otherwise
			return getItemset(indexItemset).get(indexItem);
		}
	}

	/**
	 * Get the itemset at a given position
	 * @param index the position of the itemset
	 * @return the itemset
	 */
	protected List<String> getItemset(int index) {
		return sequence.get(index+firstItemset);
	}

	/**
	 * Get the Sequence ID of this Sequence.
	 * @return a Sequence ID (integer)
	 */
	protected int getId() {
		return sequence.getId();
	}

	/**
	 * Print this pseudo-Sequence to System.out.
	 */
	public void print() {
		System.out.print(toString());
	}

	/**
	 * Get a string representation of this Sequence.
	 */
	public String toString() {
		StringBuffer r = new StringBuffer();
		// for each itemset
		for(int i=0; i < size(); i++){
			// for each item
			for(int j=0; j < getSizeOfItemsetAt(i); j++){
				if(!isLastItemset(i) ){
					// append the item
					r.append(getItemAtInItemsetAt(j, i).toString());
					// if it is in a postfix, we add a "*" symbol beside the item
					if(isPostfix(i)){
						r.append('*');
					}
					r.append(' ');
				}
			}
			r.append(" -1 "); // end of an itemset
		}
		r.append(" -2 ");
		// return the string
		return r.toString();
	}

	/**
	 * Get the position of an item inside an itemset.
	 * @param indexItemset the given itemset position
	 * @param idItem the item that we want to search.
	 * @return the position of the item or -1 if it is not found
	 */
	protected int indexOf(int indexItemset, String idItem) {
		// for each item in that itemset
		for(int i=0; i < getSizeOfItemsetAt(indexItemset); i++){
			// check if equals to the item that we search
			if(getItemAtInItemsetAt(i, indexItemset).equals(idItem)){
				return i; // if equal, return the current position
			}
		}
		return -1; // not found, return -1.
	}

}