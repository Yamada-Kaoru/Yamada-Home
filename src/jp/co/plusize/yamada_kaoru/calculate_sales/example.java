package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;



public class example {
	public static void main(String args[]) throws IOException{
		HashMap<String,String> brunchMap = new HashMap<String,String>();//支店マップ作成
		HashMap<String,Long> brunchSaleMap = new HashMap<String,Long>();//支店売上マップ作成
		String brunch;//読み込んだ内容
		BufferedReader br = null;

		try{
			File file = new File(args[0], "brunch.lst");
			if (file.exists()){//ファイルがあるかどうか
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);
				while((brunch = br.readLine()) != null) {
	            	String[] item = brunch.split(",");//コード,支店名で分割
	            	if(item[0].length() != 3 || item.length != 2 || item[0].matches("^[0-9]*$") != true ){
	            		System.out.println("支店定義ファイルのフォーマットが不正です");
	            		return;
	            	}
	            	brunchMap.put(item[0],item[1]);//コードと支店名の紐付け
	            	brunchSaleMap.put(item[0],(long) 0);
				}
			}else{
				System.out.println("支店定義ファイルが存在しません");
				return;
			}
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		finally{
			br.close();
		}


		HashMap<String,String> commodityMap = new HashMap<String,String>();//商品マップ作成
		HashMap<String,Long> commoditySaleMap = new HashMap<String,Long>();//商品売上マップ作成
		String commodity;//読み込む内容

	    try{
		   File file = new File(args[0], "commodity.lst");
		   if (file.exists()){
			   FileReader fr = new FileReader(file);
	           br = new BufferedReader(fr);
	           while((commodity = br.readLine()) != null) {
	        	   String[] item = commodity.split(",");
	        	   if(item[0].length() !=8 ||item.length !=2 ){
	           		System.err.println("商品定義ファイルのフォーマットが不正です");
	           		return;
	           	}
	        	   commodityMap.put(item[0],item[1]);
	        	   commoditySaleMap.put(item[0],(long) 0);
	           }
		   }else{
			   System.out.println("商品定義ファイルが存在しません");
			   return;
		   }
	    }catch(IOException e){
	        System.out.println("予期せぬエラーが発生しました");
	        return;
		}
	    finally{
	    	br.close();
		}

	    ArrayList numberList = new ArrayList();//.rcdリスト
	    ArrayList saleList = new ArrayList();//連番チェック済みリスト
	    String sales;//読み込む内容


	    File file = new File(args[0]);
		File files[] = file.listFiles();
		for (int i=0; i<files.length; i++) {
			if(files[i].toString().endsWith(".rcd")){
			    if(files[i].getName().toString().length()==12){
			    	numberList.add(files[i].getName());
			    	int j = Short.parseShort(numberList.get(0).toString().substring(0,8));
					int k =numberList.size();
					int l =Short.parseShort(numberList.get(numberList.size()-1).toString().substring(0,8));
					if(j + k - 1 == l){
						saleList.add(files[i]);
					}else{
						System.out.println("売上ファイルが連番になっていません");
						return;
					}
			    }
			}
		}


		try{
			for(int i = 0; i<saleList.size(); i++){
				String path = saleList.get(i).toString();
				FileReader fr = new FileReader(path);
		        br = new BufferedReader(fr);

		        ArrayList<String> exampleList = new ArrayList<String>();
		        while((sales = br.readLine()) != null) {
		        	exampleList.add(sales);
		        }
		        if(exampleList.size() != 3 ){
		    		System.out.println("<"+path+">のフォーマットが不正です");
		    		return;
		        }
		        long j = Long.parseLong(exampleList.get(2));
		        long k = brunchSaleMap.get(exampleList.get(0))+ j;
		        long l = commoditySaleMap.get(exampleList.get(1)) + j;

		        brunchSaleMap.put(exampleList.get(0).toString(),k);//支店別売上加算
		        commoditySaleMap.put(exampleList.get(1).toString(),l);//商品別売上加算

		        if(brunchSaleMap.get(exampleList.get(0)).toString().length() > 10|| commoditySaleMap.get(exampleList.get(1)).toString().length() > 10){
		        	System.out.println("合計金額が10桁を超えました");
		        	return;
	        	}
		        if(brunchSaleMap.containsKey(exampleList.get(0)) != true ){
		        	System.out.println("<"+path+">の支店コードが不正です");
		        	  return;
		        }
		        if(commoditySaleMap.containsKey(exampleList.get(1)) != true ){
		        	System.out.println("<"+path+">の商品コードが不正です");
		        	  return;
		        }
			}
		}
		catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		finally{
			br.close();
		}


		List<Entry<String,Long>> brunchEntries = new ArrayList<Entry<String,Long>>(brunchSaleMap.entrySet());
		BufferedWriter bw = null;

		Collections.sort(brunchEntries, new Comparator<Entry<String,Long>>() {
		    public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		});
		try{
			File brunchFile = new File(args[0], "\\brunch.out");
			FileWriter fw = new FileWriter(brunchFile);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> be : brunchEntries) {
				bw.write(be.getKey()+","+brunchMap.get(be.getKey())+","+ be.getValue());
				bw.newLine();
			}
		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		finally{
			bw.close();
		}


		List<Entry<String,Long>> commodityEntries = new ArrayList<Entry<String,Long>>(commoditySaleMap.entrySet());
		Collections.sort(commodityEntries, new Comparator<Entry<String,Long>>() {
		    public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		});
		try{
			File commodityFile = new File(args[0], "\\commodity.out");
			FileWriter fw = new FileWriter(commodityFile);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> ce : commodityEntries) {
				bw.write(ce.getKey()+","+commodityMap.get(ce.getKey())+","+ ce.getValue());
				bw.newLine();
			}

		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		finally{
			bw.close();
		}
	}
}

