package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class example {
	public static void main(String args[]) throws FileNotFoundException{
		HashMap<String,String> brunchmap = new HashMap<String,String>();//支店マップ作成
		HashMap<String,Long> brunchsalemap = new HashMap<String,Long>();//支店売上マップ作成
		String brunch;//読み込む内容

		try{
			File file = new File(args[0]+"\\brunch.lst");
			if (file.exists()){//ファイルがあるかどうか
				FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while((brunch = br.readLine()) != null) {
            	String[] item = brunch.split(",");//コード,支店名で分割
            	Pattern p = Pattern.compile("^[0-9]*$");
            	Matcher m = p.matcher(item[0]);
            	if(item[0].length() !=3 || item.length !=2 || m.find() != true ){
            		System.err.println("支店定義ファイルのフォーマットが不正です");
            		return;
            	}
            	brunchmap.put(item[0],item[1]);//コードと支店名の紐付け
            	brunchsalemap.put(item[0],(long) 0);
        	}
            br.close();
			}else{
				System.err.println("支店定義ファイルが存在しません");
				return;
			}
		}catch(IOException e){
			System.err.println("予期せぬエラーが発生しました");
			return;
		}



		HashMap<String,String> commap = new HashMap<String,String>();//商品マップ作成
		HashMap<String,Long> comsalemap = new HashMap<String,Long>();//商品売上マップ作成
		String commodity;//読み込む内容

	    try{
		   File file = new File(args[0]+"\\commodity.lst");
		   if (file.exists()){//ファイルがあるかどうか
		   FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);

           while((commodity = br.readLine()) != null) {
        	   String[] mom = commodity.split(",");
        	   if(mom[0].length() !=8 ||mom.length !=2 ){
           		System.err.println("商品定義ファイルのフォーマットが不正です");
           		return;
           	}
        	   commap.put(mom[0],mom[1]);
        	   comsalemap.put(mom[0],(long) 0);
           }
           br.close();
		   }else{
				System.err.println("商品定義ファイルが存在しません");
				return;
		   }
	    }catch(IOException e){
	        System.err.println("予期せぬエラーが発生しました");
	        return;
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
			    	int s = Short.parseShort(numberlist.get(0).toString().substring(0,8));
					int l =numberlist.size();
					int m =Short.parseShort(numberlist.get(numberlist.size()-1).toString().substring(0,8));
					if(s + l - 1 == m){
						salelist.add(files[i]);
					}else{
						System.err.println("売上ファイルが連番になっていません");
						return;
					}
			    }
			}
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
		    		return;
		        }
		        long o = Long.parseLong(examplelist.get(2));
		        long b = brunchsalemap.get(examplelist.get(0))+ o;
		        long c = comsalemap.get(examplelist.get(1)) + o;

		        brunchsalemap.put(examplelist.get(0).toString(),b);//支店別売上加算
		        comsalemap.put(examplelist.get(1).toString(),c);//商品別売上加算

		        if(brunchsalemap.get(examplelist.get(0)).toString().length() > 10
		        		|| comsalemap.get(examplelist.get(1)).toString().length() > 10){
		        	System.out.println("合計金額が10桁を超えました");
		        	return;
	        	}
		        if(brunchsalemap.containsKey(examplelist.get(0)) != true ){
		        	  System.out.println("<"+path+">の支店コードが不正です");
		        	  return;
		        }
		        if(comsalemap.containsKey(examplelist.get(1)) != true ){
		        	  System.out.println("<"+path+">の商品コードが不正です");
		        	  return;
		        }
		        br.close();
			}
		}
		catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}


		List<Entry<String,Long>> entries = new ArrayList<Entry<String,Long>>(brunchsalemap.entrySet());
		Collections.sort(entries, new Comparator<Entry<String,Long>>() {
		    public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		});
		try{
			File brunchfile = new File(args[0]+"\\brunch.out");
			FileWriter fw = new FileWriter(brunchfile);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Entry<String, Long> e : entries) {
			bw.write(e.getKey()+","+brunchmap.get(e.getKey())+","+ e.getValue());
			bw.newLine();
			}
			bw.close();
		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}


		List<Entry<String,Long>> entry = new ArrayList<Entry<String,Long>>(comsalemap.entrySet());
		Collections.sort(entry, new Comparator<Entry<String,Long>>() {
		    public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		});
		try{
			File comfile = new File(args[0]+"\\commodity.out");
			FileWriter fw = new FileWriter(comfile);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Entry<String, Long> en : entry) {
			bw.write(en.getKey()+","+commap.get(en.getKey())+","+ en.getValue());
			bw.newLine();
			}
			bw.close();
		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		finally{
		}
	}
}

