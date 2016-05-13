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



public class CalculateSales {

	public static void main(String args[]) throws IOException{
		HashMap<String,String> branchMap = new HashMap<String,String>();//支店マップ作成
		HashMap<String,Long> branchSaleMap = new HashMap<String,Long>();//支店売上マップ作成
		String branch;//読み込んだ内容
		BufferedReader br = null;

		try{
 			File file = new File(args[0], "branch.lst");
 			if (args.length != 1){
 				System.out.println("予期せぬエラーが発生しました");
 				return;
 			}
 			if (file.exists()){//ファイルがあるかどうか
 				FileReader fr = new FileReader(file);
 				br = new BufferedReader(fr);
 				while((branch = br.readLine()) != null) {
 	            	String[] item = branch.split(",");
 	            	if(item.length != 2 || item[0].matches("^[0-9]{3}$") != true ){
 	            		System.out.println("支店定義ファイルのフォーマットが不正です");
 	            		return;
 	            	}
 	            	branchMap.put(item[0],item[1]);
 	            	branchSaleMap.put(item[0],(long) 0);
 				}
 			}else{
 				System.out.println("支店定義ファイルが存在しません");
 				return;
 			}
 		}catch(IOException e){
 			System.out.println("予期せぬエラーが発生しました");
 			return;
 		}catch(ArrayIndexOutOfBoundsException e){
 			System.out.println("予期せぬエラーが発生しました");
 			return;
 		}
 		finally{
 			if (br != null) {
 				br.close();
 			}
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
 					if(item.length != 2 || item[0].matches("^[0-9a-zA-Z_]{8}$") != true ){
 						System.out.println("商品定義ファイルのフォーマットが不正です");
 						return;
 					}else{
 					commodityMap.put(item[0],item[1]);
 					commoditySaleMap.put(item[0],(long) 0);
 					}
 				}
 			}else{
 				System.out.println("商品定義ファイルが存在しません");
 				return;
 			}
 			}catch(IOException e){
 				err();
 				}catch(ArrayIndexOutOfBoundsException e){
 					err();
 				}
 		finally{
 			if (br != null) {
 				br.close();
 			}
 		}

	    ArrayList<String> numberList = new ArrayList<String>();//.rcdリスト
	    ArrayList<File> saleList = new ArrayList<File>();//連番チェック済みリスト
	    String sales;//ファイルの内容

	    File file = new File(args[0]);
		File files[] = file.listFiles();
		for (int i=0; i<files.length; i++) {
			if(fileTypeSpecification(files ,  i )){
				if(files[i].getName().substring(0,8).matches("^[0-9]{8}$")){
					numberList.add(files[i].getName());
					if(serialCheck(numberList, i )){
						saleList.add(files[i]);
					}else{
						System.out.println("売上ファイル名が連番になっていません");
						return;
					}
				}else{
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}
			}
		}



		try{
			for(int i = 0; i<saleList.size(); i++){
				String path = saleList.get(i).toString();
				String fileName = files[i].getName().toString();
				ArrayList<String> exampleList = new ArrayList<String>();

				FileReader fr = new FileReader(path);
 		        br = new BufferedReader(fr);
				while((sales = br.readLine()) != null) {
					exampleList.add(sales);
				}

				if(exampleList.size() != 3 ){
					System.out.println(fileName +"のフォーマットが不正です");
					return;
				}
				if(branchSaleMap.containsKey(exampleList.get(0)) != true ){
					System.out.println( fileName +"の支店コードが不正です");
					return;
				}
				if(commoditySaleMap.containsKey(exampleList.get(1)) != true ){
					System.out.println( fileName +"の商品コードが不正です");
					return;
				}
				if(saleIncrement( branchSaleMap,exampleList, 0 )
						|| saleIncrement( commoditySaleMap,exampleList, 1 ) == true){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
			}
		}
		catch(IOException e){
			err();
		}
		finally{
			if (br != null) {
				br.close();
			}
		}


		List<Entry<String,Long>> branchEntries = new ArrayList<Entry<String,Long>>(branchSaleMap.entrySet());
		sort(branchEntries);
        String branchpath = args[0]+File.separator+"branch.out";
		if(outPutFile(branchpath, branchMap , branchEntries ) == false){
			err();
		}


		List<Entry<String,Long>> commodityEntries = new ArrayList<Entry<String,Long>>(commoditySaleMap.entrySet());
		sort(commodityEntries);
		String commodityPath = args[0]+File.separator+"commodity.out";
		if(outPutFile(commodityPath, commodityMap , commodityEntries ) == false){
			err();
		}
	}



//ファイルに出力
	static boolean outPutFile(String path,HashMap<String,String> map, List<Entry<String,Long>> entries) throws IOException{
		BufferedWriter bw = null;
		try{
			File file  = new File(path);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> e : entries) {
				bw.write(e.getKey()+","+map.get(e.getKey())+","+ e.getValue());
				bw.newLine();
			}
		}catch (IOException e){
			return false;
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
		finally{
			if (bw != null) {
				bw.close();
				return true;
			}
		}
		return false;
	}

//予期せぬエラー
	static void err(){
		System.out.println("予期せぬエラーが発生しました");
		return;
	}

//リストを降順に並べる
	static void sort(List<Entry<String,Long>> entries){
		Collections.sort(entries, new Comparator<Entry<String,Long>>() {
		    public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		});
	}

//売上加算(10桁以下チェック)
	static boolean saleIncrement(HashMap<String,Long> map ,ArrayList<String> list, int n){
		long increment = Long.parseLong(list.get(2));
		long result =  map.get(list.get(n)) + increment;
		map.put(list.get(n).toString(),result);
		if(map.get(list.get(n)).toString().length() > 10){
			return true;
		}
		return false;
	}

//連番チェック
	static boolean serialCheck(ArrayList<String> list, int n ){
		int first = Short.parseShort(list.get(0).toString().substring(0,8));
		int volume =list.size();
		int last =Short.parseShort(list.get(list.size()-1).toString().substring(0,8));
		if(first + volume - 1 == last){
			return true;
	}
		return false;
	}
//rcdファイル,12桁チェック
	static boolean fileTypeSpecification(File[] file, int n ){
		if(file[n].isFile() && file[n].toString().endsWith(".rcd") && file[n].getName().length()==12){
			return true;
		}
			return false;
		}

//	要素数とコードをチェックする
//	static boolean definitionFileCheck(String str,String format,HashMap<String,String> map,HashMap<String,Long> saleMap){
//		String[] item = str.split(",");
//		if(item.length != 2 || item[0].matches("format") != true){
//			return true;
//		}else{
//			map.put(item[0],item[1]);
//			saleMap.put(item[0],(long) 0);
//		return false;
//		}
//	}
}









