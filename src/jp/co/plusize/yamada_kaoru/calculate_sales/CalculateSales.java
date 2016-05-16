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

	public static void main(String args[]) throws IOException {
		HashMap<String, String> branchMap = new HashMap<String, String>();//支店マップ作成
		HashMap<String, Long> branchSaleMap = new HashMap<String, Long>();//支店売上マップ作成
		BufferedReader br = null;
		if (args.length != 1) {
			err();
			return;
		}
		String branchFilePath = args[0] + File.separator + "branch.lst";
		if(readFilePutMap(branchFilePath, "^[0-9]{3}$", "支店", branchMap, branchSaleMap)) {
			return;
		}

		HashMap<String, String> commodityMap = new HashMap<String, String>();//商品マップ作成
		HashMap<String, Long> commoditySaleMap = new HashMap<String, Long>();//商品売上マップ作成
		String commodityFilePath = args[0] + File.separator + "commodity.lst";


		if(readFilePutMap(commodityFilePath, "^[0-9a-zA-Z_]{8}$", "商品", commodityMap, commoditySaleMap)) {
			return;
		}

		ArrayList<String> rcdList = new ArrayList<String>();//.rcdファイルの番号リスト
		ArrayList<File> saleFileList = new ArrayList<File>();//連番チェック済みリスト
		String sales;//ファイルの内容

		File file = new File(args[0]);
		File files[] = file.listFiles();
		for (int i=0; i<files.length; i++) {
			if(fileTypeSpecification(files[i])) {
				if(files[i].getName().substring(0, 8).matches("^[0-9]{8}$")) {
					rcdList.add(files[i].getName());
					if(serialCheck(rcdList)) {
						saleFileList.add(files[i]);
					} else {
						System.out.println("売上ファイル名が連番になっていません");
						return;
					}
				} else {
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}
			}
		}



		try{
			for(int i = 0; i<saleFileList.size(); i++) {
				String path = saleFileList.get(i).toString();
				String fileName = files[i].getName().toString();
				ArrayList<String> saleList  = new ArrayList<String>();

				FileReader fr = new FileReader(path);
				br = new BufferedReader(fr);
				while((sales = br.readLine()) != null) {
					saleList.add(sales);
				}

				if(saleList.size() != 3) {
					System.out.println(fileName + "のフォーマットが不正です");
				}
				if(!branchSaleMap.containsKey(saleList.get(0))) {
					System.out.println(fileName + "の支店コードが不正です");
				}
				if(!commoditySaleMap.containsKey(saleList.get(1))) {
					System.out.println(fileName + "の商品コードが不正です");
				}
				if(saleIncrement(branchSaleMap, saleList, 0) || saleIncrement(commoditySaleMap, saleList, 1)) {
					System.out.println("合計金額が10桁を超えました");
				}
			}
		} catch(IOException e) {
			err();
		} finally {
			if (br != null){
				br.close();
			}
		}


		List<Entry<String, Long>> branchEntries = new ArrayList<Entry<String, Long>>(branchSaleMap.entrySet());
		sort(branchEntries);
		String branchpath = args[0] + File.separator + "branch.out";
		if(!outPutFile(branchpath, branchMap, branchEntries)) {
			err();
			return;
		}


		List<Entry<String, Long>> commodityEntries = new ArrayList<Entry<String, Long>>(commoditySaleMap.entrySet());
		sort(commodityEntries);
		String commodityPath = args[0] + File.separator + "commodity.out";
		if(!outPutFile(commodityPath, commodityMap, commodityEntries)) {
			err();
			return;
		}
	}

	//ファイルに出力
	static boolean outPutFile(String path, HashMap<String, String> definitionMap,
			List<Entry<String, Long>> entries) throws IOException {
		FileWriter fw;
		BufferedWriter bw = null;

		try{
			File file  = new File(path);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> content : entries) {
				bw.write(content.getKey() + "," + definitionMap.get(content.getKey()) + "," + content.getValue());
				bw.newLine();
			}
		} catch(IOException e) {
			return false;
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
		return true;
	}

	//予期せぬエラー
	static void err() {
		System.out.println("予期せぬエラーが発生しました");
		return;
	}
	//リストを降順に並べる
	static void sort(List<Entry<String, Long>> entries) {
		Collections.sort(entries, new Comparator<Entry<String, Long>>() {
			public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
	}
	//売上加算(10桁以下チェック)
	static boolean saleIncrement(HashMap<String, Long> saleMap, ArrayList<String> confirmingList, int number) {
		long increment = Long.parseLong(confirmingList.get(2));
		long result =  saleMap.get(confirmingList.get(number)) + increment;
		saleMap.put(confirmingList.get(number).toString(), result);
		return(saleMap.get(confirmingList.get(number)).toString().length() > 10); 
	}

	//リストの連番チェック
	static boolean serialCheck(ArrayList<String> checkList) {
		int first = Short.parseShort(checkList.get(0).toString().substring(0, 8));
		int volume =checkList.size();
		int last =Short.parseShort(checkList.get(checkList.size()-1).toString().substring(0, 8));
		return(first + volume - 1 == last) ;
		}

	//rcdかつ12桁のファイルチェック
	static boolean fileTypeSpecification(File checkFile) {
		return(checkFile.isFile() && checkFile.toString().endsWith(".rcd") && checkFile.getName().length() == 12);
	}

	//定義ファイル処理
	public static boolean readFilePutMap(String path, String format, String errMessage,
			HashMap<String, String> definitionMap, HashMap<String, Long> saleMap) throws IOException {
		String line;//読み込んだ内容
		BufferedReader br = null;
		FileReader fr;
		try{
			File file = new File(path);
			if (file.exists()) {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				while((line = br.readLine()) != null) {
					String[] item = line.split(",");
					if(item.length != 2 || item[0].matches(format) != true) {
						System.out.println(errMessage + "定義ファイルのフォーマットが不正です");
						return false;
					}
					definitionMap.put(item[0], item[1]);
					saleMap.put(item[0], (long)  0);
				}
			} else {
				System.out.println(errMessage + "定義ファイルが存在しません");
				return false;
			}
		} catch(IOException e) {
			err();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return true;
	}
}

