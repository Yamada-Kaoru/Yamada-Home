package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class example {
	public static void main(String args[]){
		HashMap<String,String> brunchmap = new HashMap<String,String>();//支店マップ作成
		String brun;//読み込む内容
		try{
			File file = new File(args[0]+"\\brunch.list");
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while((brun = br.readLine()) != null) {
            	//System.out.println(line);//ファイルの読み込み確認
            	String[] item = brun.split(",");//コード,支店名で分割
        		//System.out.println(item[0]);
        		//System.out.println(item[1]);
            	if(item[0].length()!=3){
            		System.err.println("支店定義ファイルのフォーマットが不正です");
            	}
            	brunchmap.put(item[0],item[1]);//コードと支店名の紐付け
            }
            br.close();
		}
		catch(FileNotFoundException e){
            System.err.println("支店定義ファイルが存在しません");
            System.exit(1);
		}
		catch (IOException e) {
			 System.err.println("支店定義ファイルのフォーマットが不正です");
		}
		finally{
//			System.out.println(brunchmap.entrySet());
		}

		HashMap<String,String> commap = new HashMap<String,String>();//商品マップ作成
		String com;//読み込む内容

	    try{
		   File file = new File(args[0]+"\\commodity.list");
		   FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);

           while((com = br.readLine()) != null) {
        	//System.out.println(com);
        	String[] mom = com.split(",");//コード,商品名で分割
    		//System.out.println(mom[0]);
    		//System.out.println(mom[1]);
        	if(mom[0].length()!=3){
        		//System.err.println("商品定義ファイルのフォーマットが不正です");
        	}
        	commap.put(mom[0],mom[1]);//コードと支店名の紐付け
        }
        br.close();
	}
	catch(FileNotFoundException e){
        System.err.println("支店定義ファイルが存在しません");
        System.exit(1);
	}
	catch (IOException e) {
		 System.err.println("支店定義ファイルのフォーマットが不正です");
	}
	finally{
//		System.out.println(commap.entrySet());
	 }  
	    
	    
	    ArrayList salelist = new ArrayList();//.rcdリスト
	    
	File file = new File(args[0]);
	File files[] = file.listFiles();{  
		
	for (int i=0; i<files.length; i++) {
//	        System.out.println(files[i]);
		if(files[i].toString().endsWith(".rcd")){
	    if(files[i].getName().toString().length()==12){
	    	salelist.add(files[i]);
	    }
	}
   }
  }
	System.out.println(salelist.get(0).toString().substring(30,8));
	}
	
}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	        	int j = Short.parseShort(files[i].getName().substring(0,8));
//	        	salelist.add(j);//数字のみいれたリスト
//	        	System.out.println(salelist.size());//
//	        	System.out.println(salelist.get(0));//rcdかつ12桁のもののリスト


//	    	if(salelist.get(0) + salelist.size()-1 != salelist.get(salelist.size()-1)){
//	    		System.out.println("売上ファイル名が連番になっていません");


	        	
//				FileReader sr = new FileReader(files[i]);
//	            BufferedReader ssr = new BufferedReader(sr);
//	            String sales;//売上集計ファイルから読み込む内容
//	            while((sales = ssr.readLine()) != null) {
//	            	System.out.println(sales);
	  

	    
	        	
	        	
//	        	System.out.println(files[i].getName().substring(0,8));//数字部分のみ
//	        	int j = Short.parseShort(files[i].getName().substring(0,8));//文字列→
                  
	        	  








