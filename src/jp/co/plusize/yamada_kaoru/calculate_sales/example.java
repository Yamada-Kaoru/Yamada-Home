package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class example {
	public static void main(String args[]){
		HashMap<String,String> brunchmap = new HashMap<String,String>();//支店マップ作成
		HashMap<String,Long> brunchsalemap = new HashMap<String,Long>();//支店売上マップ作成
		String brunch;//読み込む内容
		try{
			File file = new File(args[0]+"\\brunch.list");
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while((brunch = br.readLine()) != null) {
            	String[] item = brunch.split(",");//コード,支店名で分割
            	if(item[0].length()!=3){
            		System.err.println("支店定義ファイルのフォーマットが不正です");
           	}
            	brunchmap.put(item[0],item[1]);//コードと支店名の紐付け
            	brunchsalemap.put(item[0],(long) 0);
            }
            br.close();
//            System.out.println(brunchmap.entrySet());
//            System.out.println(brunchsalemap.entrySet());
		}
		catch(FileNotFoundException e){
            System.err.println("支店定義ファイルが存在しません");
            System.exit(1);
		}
		catch (IOException e) {
			 System.err.println("支店定義ファイルのフォーマットが不正です");
		}

		HashMap<String,String> commap = new HashMap<String,String>();//商品マップ作成
		HashMap<String,Long> comsalemap = new HashMap<String,Long>();//商品売上マップ作成
		String commodity;//読み込む内容

	    try{
		   File file = new File(args[0]+"\\commodity.list");
		   FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);

           while((commodity = br.readLine()) != null) {
        	   String[] mom = commodity.split(",");
        	   commap.put(mom[0],mom[1]);
        	   comsalemap.put(mom[0],(long) 0);
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

		int s = Short.parseShort(numberlist.get(0).toString().substring(0,8));
		int l =numberlist.size();
		int m =Short.parseShort(numberlist.get(numberlist.size()-1).toString().substring(0,8));
		if(s + l - 1!=m){ //連番かどうか判断(
			System.out.println("売上ファイルが連番になっていません");
		}

		try{
			for(int f = 0; f<salelist.size(); f++){
				String path = salelist.get(f).toString();
				FileReader fr = new FileReader(path);
		        BufferedReader br = new BufferedReader(fr);

		        ArrayList<String> examplelist = new ArrayList<String>();
		        while((sales = br.readLine()) != null) {
		        	examplelist.add(sales);
		        }
		        if(examplelist.size() != 3 ){
		    		System.out.println("<"+path+">のフォーマットが不正です");
		        }
		        long o = Long.parseLong(examplelist.get(2));
		        long b = brunchsalemap.get(examplelist.get(0))+ o;
//		        long c = comsalemap.get(examplelist.get(1)) + o;
		        brunchsalemap.put(examplelist.get(0).toString(),b);//支店別売上加算
//		        comsalemap.put(examplelist.get(1).toString(),c);//商品別売上加算

//		        if(brunchsalemap.get(examplelist.get(0)).toString().length() > 10
//		        		|| comsalemap.get(examplelist.get(1)).toString().length() > 10){
//		        	System.out.println("合計金額が10桁を超えました");
//	        	}
		        if(brunchsalemap.containsKey(examplelist.get(0)) != true ){
		        	  System.out.println("<"+path+">の支店コードが不正です");
		        }



		        }
			}
		catch(IOException e){
		        System.exit(1);
		}
		System.out.println(brunchsalemap.entrySet());//支店売上マップ出力
		System.out.println(comsalemap.entrySet());//支店売上マップ出力
		try{
			File brunchfile = new File(args[0]+"\\brunch.out");
			FileWriter fw = new FileWriter(brunchfile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(brunchsalemap.entrySet().toString());
			bw.close();
		}catch (IOException e){
			System.out.println(e);
		}
	}
}

























