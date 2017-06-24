package codeingame.fr;

public class Main {

	public static void main(String[] args) {
		String s = "Tantely";
		System.out.println(s.charAt(6));
		String ss[] = new String[5];
		
		ss[0]  = "lsdjfmsdfj";
		
		ss[1]  = "mmmmm";
		
		System.out.println(ss[1]);
		
		try{
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		int[] ints = {-9, 14, 37, 102};
		System.out.println(exists(ints, 102)); // true
		System.out.println(exists(ints, 36)); // false
	}

	static boolean exists(int[] ints, int k) {
		int size = ints.length - 1;
		int max = 10000;
		int min = 0;
		int pos;
		while (max>=min){
			pos = (max + min)/2;
			if(k == ints[pos]){
				return true;
			}else if(k > ints[pos]){
				min = pos + 1;			
			}else{
				max = pos - 1;
			}
		}
		return false;

	}
}
