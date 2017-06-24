package codeingame.fr;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(listFiles("/Users/tantely/Documents/PERSO"));
	}


	public static String listFiles(String path){ 

		String dirName = "ONN";
		File[] files = null; 
		File directoryToScan = new File(path); 

		if(directoryToScan.getName().equals(dirName)){
			return directoryToScan.getAbsolutePath();
		}

		files = directoryToScan.listFiles(); 
		if((files == null) || (files.length == 0)){
			return null;
		}

		for(File file : files){
			if(!file.isDirectory()) continue;
			
			if(file.getName().equals(dirName)){
				return file.getAbsolutePath();
			}else {
				String res = listFiles(file.getAbsolutePath());
				if(res != null) return res;
			}
			
		}

		return null; 
	} 

	static int getPos(int n){

		if(n == 0) return 0;
		if(n == 1) return 1;
		if(n == 2) return -1;

		int mv = mouv(n);

		return getPos(n-1) + mv;
	}

	static int mouv(int n){
		if(n == 0) return 0;
		if(n == 1) return 1;
		if(n == 2) return -2;

		int m2 = 1;
		int m1 = -2;
		int m = 0;
		for(int i=3; i<=n; i++){
			m = m1 - m2; 
			m2 = m1;
			m1 = m;
		}
		return m;
	}

}
