package jp.co.plusize.yamada_kaoru.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class example {
	

	public static void main(String args[]){
		HashMap<String,String> brunchmap = new HashMap<String,String>();
		try{
			File file = new File(args[0]+"\\brunch.list");
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            String line;
            while((line = br.readLine()) != null) {
            	System.out.println(line);//ファイルの読み込み出力
            }
            String[] item = line.split(",", 0);//コード,支店名で分割
            System.out.println(item[0]);
            System.out.println(item[1]);
            
            brunchmap.put(item[0],item[1]);//コードと支店名の紐付け

        System.out.println(brunchmap.entrySet());//確認
            br.close();
		}catch(FileNotFoundException e){
            System.err.println("支店定義ファイルが存在しません");
            System.exit(1);//エラー処理
		} catch (IOException e) {
			 System.err.println("支店定義ファイルのフォーマットが不正です");
		}
	}
}




