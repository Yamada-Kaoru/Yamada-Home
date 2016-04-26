package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            System.exit(1);//エラー処理
		}
		catch (IOException e) {
			 System.err.println("支店定義ファイルのフォーマットが不正です");
		}
		finally{
//			System.out.println(brunchmap.entrySet());//確認
		}

		HashMap<String,String> commap = new HashMap<String,String>();//商品マップ作成
		String com;//読み込む内容

	    try{
		   File file = new File(args[0]+"\\commodity.list");
		   FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);

           while((com = br.readLine()) != null) {
        	//System.out.println(com);//ファイルの読み込み確認
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
        System.exit(1);//エラー処理
	}
	catch (IOException e) {
		 System.err.println("支店定義ファイルのフォーマットが不正です");
	}
	finally{
//		System.out.println(commap.entrySet());//確認
	 }
	    File file = new File(args[0]);
	    File files[] = file.listFiles();{

	    for (int i=0; i<files.length; i++) {
//	        System.out.println(files[i]);
	    	if(files[i].toString().endsWith(".rcd")){
	        	System.out.println(files[i]);//rcdで終わるものだけ出力
	        }
	}
	    }
}
}






