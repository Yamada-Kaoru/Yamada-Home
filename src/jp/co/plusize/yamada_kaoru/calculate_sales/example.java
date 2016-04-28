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
		HashMap<String,Long> brunchsalemap = new HashMap<String,Long>();//支店売上マップ作成
		String brun;//読み込む内容
		try{
			File file = new File(args[0]+"\\brunch.list");
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while((brun = br.readLine()) != null) {
            	String[] item = brun.split(",");//コード,支店名で分割
            	if(item[0].length()!=3){
            		System.err.println("支店定義ファイルのフォーマットが不正です");
           	}
            	brunchmap.put(item[0],item[1]);//コードと支店名の紐付け
            	brunchsalemap.put(item[0],(long) 0);//コードと支店売上の紐付け
            }
            br.close();
            System.out.println(brunchmap.entrySet());
            System.out.println(brunchsalemap.entrySet());
		}
		catch(FileNotFoundException e){
            System.err.println("支店定義ファイルが存在しません");
            System.exit(1);
		}
		catch (IOException e) {
			 System.err.println("支店定義ファイルのフォーマットが不正です");
		}

		HashMap<String,String> commap = new HashMap<String,String>();//商品マップ作成
		String com;//読み込む内容

	    try{
		   File file = new File(args[0]+"\\commodity.list");
		   FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);

while((com = br.readLine()) != null) {
        	String[] mom = com.split(",");
        	commap.put(mom[0],mom[1]);
        }
        br.close();
	}
	catch(FileNotFoundException e){
        System.err.println("商品定義ファイルが存在しません");
        System.exit(1);
	}
	catch (IOException e) {
		 System.err.println("商品定義ファイルのフォーマットが不正です");
	}



	    ArrayList numberlist = new ArrayList();//.rcdリスト
	    ArrayList salelist = new ArrayList();//連番チェック済みリスト
//	    HashMap<String,String> commoditysalemap = new HashMap<String,String>();//商品売上マップ


		String sales;//読み込む内容

	File file = new File(args[0]);
	File files[] = file.listFiles();

	for (int i=0; i<files.length; i++) {
		if(files[i].toString().endsWith(".rcd")){
		    if(files[i].getName().toString().length()==12){
		    	numberlist.add(files[i].getName());
		    	salelist.add(files[i]);
		    }
		}

	}
	int j = Short.parseShort(numberlist.get(0).toString().substring(0,8));
	int k =numberlist.size();
	int t =Short.parseShort(numberlist.get(numberlist.size()-1).toString().substring(0,8));
	if(j + k - 1!=t){ //連番かどうか判断
		System.out.println("売上ファイルが連番になっていません");
	}

	try{
		for(int f = 0; f<salelist.size(); f++){
		String path = salelist.get(f).toString();
		FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        while((sales = br.readLine()) != null) {
        	String[] code = sales.split("\n");
        	if(brunchsalemap.get(code[0]) != null){

        	}

	}
		}


	}
	catch(IOException e){
	        System.exit(1);
	}
	}

	}






























//				FileReader sr = new FileReader(files[i]);
//	            BufferedReader ssr = new BufferedReader(sr);
//	            String sales;//売上集計ファイルから読み込む内容
//	            while((sales = ssr.readLine()) != null) {
//	            	System.out.println(sales);










