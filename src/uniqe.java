import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class uniqe {
	//Input: Bill first and last names, shipping first and last name, card name
	//Output: How many unique names were found
	public static int countUniqueNames(String bfn, String bln, String sfn, String sln, String noc){
		int namecounter=1;//set initial to 1
		boolean fnameflag=false;
		boolean lnameflag=false;
		fnameflag=breakncompare(bfn,sfn,false);//Check if shipping and bill first names are the same
		lnameflag=breakncompare(bln,sln,false);//Check if shipping and bill last names are the same
		if (!fnameflag||!lnameflag){//If none of them are the same, count one more unique name
			namecounter++;
		}
		if(fnameflag&&lnameflag){//If the first two are the same, compare one of them to the third one, same as in the first case
			fnameflag=breakncompare(bfn,noc,true);
			lnameflag=breakncompare(bln,noc,true);
			if (!fnameflag||!lnameflag){
				namecounter++;
			}
		}
		else{//If the first two are not the same, compare both of them to the third one, and if it's not the same as any of them,
			//add another unique name
			boolean fnameflag1=breakncompare(sfn,noc,true);
			boolean lnameflag1=breakncompare(sln,noc,true);
			fnameflag=breakncompare(bfn,noc,true);
			lnameflag=breakncompare(bln,noc,true);
			if ((!fnameflag||!lnameflag)&&(!fnameflag1||!lnameflag1)){
				namecounter++;
			}
		}
		return namecounter;
	}
	//Input: Two Names
	//Output: How many differences are between them
	public static int compareletters(String s1, String s2){
		int errorcount=0;//Number of Differences
		for(int i=0; i<s1.length()&&i<s2.length();i++){//Up to the length of the short string
			if(s1.charAt(i)!=s2.charAt(i)){//If the letter in the same position is not the same
				errorcount++;//count a difference
			}
		}
		errorcount+=Math.abs(s1.length()-s2.length());//Add the letters which are in one string and not in the other
		return errorcount;
	}
	//Input: Two Names and a boolean whether one of them is a card
	// Output: Do They contain the same first word or a variation of it (for not card) 
	//or is one of the words in the second one is the first in the first one or a variation of it (for card)
	public static boolean breakncompare(String str1, String str2, boolean card){
		int j=0;
		int i=0;
		int errors;
		boolean end=true;//whether it reached the end of the string
		String s1="";
		String s2="";
		for(i=0;i<str1.length();i++){//Run through the first string, until a space is found
			if(str1.charAt(i)==' '){
				s1=str1.substring(j,i);//Create a substring from the beginning to the space
				j=i+1;
				int l=0;
				int k=0;
				for(k=0;k<str2.length();k++){//Run through the second string, until a space is found
					if(str2.charAt(k)==' '){
						s2=str2.substring(l,k);//Create a substring from the beginning to the space
						l=k+1;
						errors=compareletters(s1,s2);//Compare the two substrings
						if(errors<=s1.length()/4||errors<=s2.length()/4){//If the names are similar enough, return true
							return true;
						}
						else{//Else, check if one of them is a nickname of the other. If yes, return true
							if(NickCheck(s1,s2)){
								return true;
							}
						}
						if(!card){//If not comparing to card name, stop the loop,
							//else, keep looking through the second string for names
							end=false;//note that you didn't reach the end of the string
							break;
						}
					}
				}
				if(end){//If reached the end of the second string, make a substring to the end and check it
					s2=str2.substring(l,k);
					errors=compareletters(s1,s2);
					if(errors<=s1.length()/4||errors<=s2.length()/4){
						return true;
					}
					else{
						if(NickCheck(s1,s2)){
							return true;
						}
					}
				}
				end=false;
				break;
			}
			if(i==str1.length()-1){//If reached the end of the first string, make a substring to the end and check it
				s1=str1.substring(j,i+1);
				int l=0;
				int k=0;
				for(k=0;k<str2.length();k++){
					if(str2.charAt(k)==' '){
						s2=str2.substring(l,k);
						l=k+1;
						errors=compareletters(s1,s2);
						if(errors<=s1.length()/4||errors<=s2.length()/4){
							return true;
						}
						else{
							if(NickCheck(s1,s2)){
								return true;
							}
						}
					}
				}
				s2=str2.substring(l,k);
				errors=compareletters(s1,s2);
				if(errors<=s1.length()/4||errors<=s2.length()/4){
					return true;
				}
				else{
					if(NickCheck(s1,s2)){
						return true;
					}
				}
			}
		}
		return false;
	}
	//Input: Two Names
	//Output: Whether one of them is a nickname of the other 
	public static boolean NickCheck(String str1, String str2){
		String csvFile = "CSV Nick";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ", ";
		int errors=0;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] Names = line.split(cvsSplitBy);
				//For each one of the string, check if they are similar to the nickname or the name,
				//and if so, compare the other string to the other part, for example, if the first string is like the name,
				//compare the second to the nickname. If the second comperation is good, return true.
				errors=compareletters(str2,Names[1]);
				if(errors<=str2.length()/4||errors<=Names[1].length()/4){
					errors=compareletters(str1,Names[2]);
					if(errors<=str1.length()/4||errors<=Names[1].length()/4){
						return true;
					}
				}
				errors=compareletters(str2,Names[2]);
				if(errors<=str2.length()/4||errors<=Names[2].length()/4){
					errors=compareletters(str1,Names[1]);
					if(errors<=str1.length()/4||errors<=Names[2].length()/4){
						return true;
					}
				}
				errors=compareletters(str1,Names[1]);
				if(errors<=str1.length()/4||errors<=Names[1].length()/4){
					errors=compareletters(str2,Names[2]);
					if(errors<=str2.length()/4||errors<=Names[1].length()/4){
						return true;
					}
				}
				errors=compareletters(str1,Names[2]);
				if(errors<=str1.length()/4||errors<=Names[2].length()/4){
					errors=compareletters(str2,Names[1]);
					if(errors<=str2.length()/4||errors<=Names[2].length()/4){
						return true;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;//If none of the comparisons works, the strings are not name and nickname, return false.
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//				System.out.println(countUniqueNames("Deborah","Egni","Deborah","Egli","Deborah Egli"));
		//				System.out.println(countUniqueNames("Deborah","Egli","Deborah","Egli","Deborah Egli"));
		//				System.out.println(countUniqueNames("Deborah","Egli","Debbie","Egli","Debbie Egli"));
		//				System.out.println(countUniqueNames("Deborah","Egni","Deborah","Egli","Deborah Egli"));
		//				System.out.println(countUniqueNames("Deborah S","Egli","Deborah","Egli","Egli Deborah"));
		//				System.out.println(countUniqueNames("Michele","Egli","Deborah","Egli","Michele Egli"));
		System.out.println(countUniqueNames("Michele","Egli","Deborah","Egli","Egli Michale"));
	}


}
