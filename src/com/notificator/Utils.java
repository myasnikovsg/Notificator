package com.notificator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.CookieManager;
import java.util.ArrayList;

import model.ClientRequestResult;
import model.Order;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.text.BoringLayout.Metrics;
import android.util.DisplayMetrics;

public class Utils {
	
	public static volatile ArrayList<Order> orders; 
	public static volatile boolean orders_loaded;
	public static volatile ArrayList<Result> result;
	public static volatile boolean results_loaded;
	public static int SCREEN_WIDTH;
	public static int DIALOG_WIDTH;
	public static String property_string[];
	public static String property_result_string[];
	public static String property_delimiter[];
	public static String property_name[];
	public static int property_min_val[];
	
	public static int cur_property[];
	public static int cur_property_index;
	
	public static final int[][][] can_equip = {
		{{1, 0, 0, 1, 0, 1, 1, 1, 1, 0}, {1, 0, 0, 0, 1, 1, 1, 0, 1}, {0, 0, 0, 1}, {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0}, {1, 1, 1}}, //Barbarian
		{{1, 0, 1, 1, 0, 1, 0, 1, 1, 0}, {0, 1, 0, 1, 0, 0, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0}}, //Demon Hunter
		{{1, 0, 0, 1, 1, 1, 0, 1, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0}}, //Monk
		{{1, 1, 0, 1, 0, 1, 0, 1, 1, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 0}}, //Witch Doctor
		{{1, 0, 0, 1, 0, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 0}} // Wizard
		};

	public static final int property_group[] = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1,
		1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4,
		6, 6, 6, 6, 6, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 6, 6, 6,
		6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6,
		6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
		5, 5, 0, 0, 0
	};
	
	public static final int property[][] = {
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 60, 61, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 71, 72, -4, 27, 31, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114, -7, 38, 39, 40}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 26, 29, -5, 33, 34, 35, 36, 37, -6, 43, 44}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 25, 28, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 38, 39, 40, 41, 42}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 56, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 24, 72, -4, 32, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 30, -5, 33, 34, 35, 36, 37, -6, 43, 44}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 56, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114, -7, 38, 39, 40, 41, 42}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 71, -4, 26, 28, 29, 30, 31, 32, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 41, 42}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 25, 28, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 41, 42}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 61, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114, -7, 45, 46, 47, 48}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 26, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 38, 39, 40, 52, 53, 54, 55}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 24, 71, -4, 32, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 49, 50, 51}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 56, 65, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 38, 39, 40, 52, 53, 54, 55}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 71, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114, -7, 45, 46, 47, 48}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -4, 30, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 45, 46, 47, 48}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 63, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, 71, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 45, 46, 47, 48, 49, 50, 51}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, 69, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44, -7, 41, 42, 49, 50, 51}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 56, 63, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, 69, -3, 21, 22, 23, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 114, -7, 45, 46, 47, 48}, 
		{-1, 1, 10, 63, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 70, 71, 72, -4, 27, 31, -5, 33, 34, 35, 36, 37, -6, 43, 84, 85, -7, 53, 54, 55}, 
		{-1, 1, 10, 63, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 70, 71, 72, -4, 25, 28, -5, 33, 34, 35, 36, 37, -6, 43, 84, 85, -7, 79, 80, 81, 82, 83}, 
		{-1, 0, 1, 10, 63, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 70, 71, 72, -4, 26, 29, -5, 33, 34, 35, 36, 37, -6, 43, 84, 85, -7, 73, 74, 75, 76, 77, 78}, 
		{-1, 1, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 116, -2, 13, 14, 15, 16, 17, 18, 19, 20, 69, -3, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 84, 85, -7, 45, 46, 47, 48, 52, 53, 54, 55, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83}, 
		{-1, 0, 8, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, 68, 116, 118, -2, 69, -3, 22, 23, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 84, 85}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, -2, 16, -3, 21, 70, 71, 72, -4, 30, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 86, 87, 88, 89, 90, 91}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, -2, 17, -3, 70, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 114, 115}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, 68, 116, -2, 18, -3, 70, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115}, 
		{-1, 0, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, 68, 116, -2, 18, -3, 21, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 114, 115, -7, 38}, 
		{-1, 0, 10, 56, 57, 58, 60, 61, 62, 63, 64, 65, 66, 67, 68, -2, 69, -3, 70, 71, 72, -4, 26, 29, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 114, 115, -7, 38, 92, 93, 94, 95, 96}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, 66, 67, 68, 116, -3, 70, 71, 72, -4, 26, 29, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 114, 115, -7, 38, 92, 93, 94, 95, 96}, 
		{-1, 0, 11, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, 116, -2, 20, 69, -3, 23, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 38}, 
		{-1, 0, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 116, -2, 15, 18, 69, -3, 24, 70, -4, 25, 27, 28, 31, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113}, 
		{-1, 0, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, -2, 19, 69, -3, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 114, 115}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, 67, 68, 116, -2, 16, -3, 21, 70, 71, 72, -4, 30, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 86, 87, 88, 89, 90, 91}, 
		{-1, 0, 1, 8, 9, 10, 11, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 67, 68, 116, 118, -2, 69, -3, 22, 23, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 84, 85, 114, 115}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, -2, 14, -3, 22, 70, 71, 72, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115}, 
		{-1, 0, 1, 56, 57, 58, 60, 61, 62, 63, 64, 65, 116, -2, 15, 69, -3, 24, 70, 71, 72, -4, 32, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 97, 98, 99, 100, 101, 102, 103}, 
		{-1, 56, 57, 58, 60, 61, 62, 63, 64, 65, 116, -2, 15, 69, -3, 70, 71, 72, -4, 27, 31, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 104, 105, 106, 107}, 
		{-1, 10, 56, 57, 58, 60, 61, 62, 63, 64, 65, 116, -2, 15, 69, -3, 23, 70, 71, 72, -4, 25, 28, -5, 33, 34, 35, 36, 37, -6, 43, 44, 84, 85, 115, -7, 108, 109, 110, 111, 112, 113}, 
		{-1, 11, 56, 57, 60, 61, 62, 64, 65, -3, 22, 23, -5, 33, 34, 35, 36, 37}, 
		{-1, 11, 56, 57, 60, 61, 62, 64, 65, -3, 22, 23, -5, 33, 34, 35, 36, 37}, 
		{-1, 11, 56, 57, 58, 59, 60, 61, 62, 64, 65, -3, 22, 23, -5, 33, 34, 35, 36, 37}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44}, 
		{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -2, 13, 14, 15, 16, 17, 18, 19, 20, -3, 21, 22, 23, -5, 33, 34, 35, 36, 37, -6, 43, 44}
	};
	
	
	public static final int[] item_amount = {10, 9, 4, 15, 3};
	
	public static ArrayList<Integer> stats;
	public static int last_item_id[];
	public static boolean isDisabled[];
	
	static int getOffsettedIndex(int selected_type1, int selected_type2){
		int offset = 0;
		for (int i = 0; i < selected_type1; i++)
			offset += item_amount[i];
		return offset + selected_type2;
	}
	
	public static int login(String name, String pass){
		URL url;
		try {
			url = new URL("http://192.168.1.5:8080/auc/login");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			CookieManager manager = new CookieManager();
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);
			con.setDoOutput(true);
			ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
			out.writeObject(new String[] {name, pass});
			out.close(); 
			ObjectInputStream in = new ObjectInputStream(con.getInputStream());
			ClientRequestResult res = (ClientRequestResult) in.readObject();
			in.close();
			con.disconnect();
			return res.getError_code();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean loginNameAvailable(String name){
		URL url;
		try {
			url = new URL("http://192.168.1.5:8080/auc/checkName");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
			out.writeObject(name);
			out.close();
			ObjectInputStream in = new ObjectInputStream(con.getInputStream());
			ClientRequestResult res = (ClientRequestResult) in.readObject();
			in.close();
			con.disconnect();
			return res.getError_code() == 0 && ((Boolean) res.getResult());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int createAccount(String name, String pass, int game_zone){
		URL url;
		try {
			url = new URL("http://192.168.1.5:8080/auc/registration");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
			out.writeObject(new String[] {name, pass, Integer.toString(game_zone)});
			out.close();
			ObjectInputStream in = new ObjectInputStream(con.getInputStream());
			ClientRequestResult res = (ClientRequestResult) in.readObject();
			in.close();
			con.disconnect();
			return res.getError_code();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int loadOrders() {
		URL url;
		try {
			url = new URL("http://192.168.1.5:8080/auc/orderList");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			ObjectInputStream in = new ObjectInputStream(con.getInputStream());
			ClientRequestResult res = (ClientRequestResult) in.readObject();
			in.close(); 
			con.disconnect();
			if (res.getError_code() == 0) 
				orders = (ArrayList<Order>) res.getResult();
			return res.getError_code();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void loadResult(int order_id){
		Thread thread = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				// TODO here will be request to server
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				result = new ArrayList<Result>();
				Result result1 = new Result();
				result1.setArray(new int[] {0, 1, 1, 1, 2, 3, 4, 5, 2, 1, 2});
				result1.setBid(10000);
				result1.setBuyout(100000);
				result1.setMain_attr(1000);
				result1.setName("Axe000123");
				result1.setProperty_number(3);
				result1.setSocket_number(2);
				
				Result result2 = new Result();
				result2.setArray(new int[] {0, 1, 1, 1, 2, 3, 4, 5, 2, 1, 2});
				result2.setBid(10000);
				result2.setBuyout(100000);
				result2.setMain_attr(1000);
				result2.setName("Axe000123");
				result2.setProperty_number(3);
				result2.setSocket_number(3);
				result.add(result1);
				result.add(result2);
				results_loaded = true;
			}
		});
		thread.start();
	}
	
	public static int sendOrder(Order order) {
		URL url;
		try {
			url = new URL("http://192.168.1.5:8080/auc/newOrder");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
			out.writeObject(order);
			out.close();
			ObjectInputStream in = new ObjectInputStream(con.getInputStream());
			ClientRequestResult res = (ClientRequestResult) in.readObject();
			in.close();
			con.disconnect();
			return res.getError_code();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void init(Activity context){
		property_string = context.getResources().getStringArray(R.array.property_string);
		property_delimiter = context.getResources().getStringArray(R.array.property_delimiter);
		property_name = context.getResources().getStringArray(R.array.property_name);
		property_result_string = context.getResources().getStringArray(R.array.property_result_string);
		property_min_val = new int[130];
		for (int i = 0; i < property_min_val.length; i++)
			property_min_val[i] = i;
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		SCREEN_WIDTH = metrics.widthPixels;
		DIALOG_WIDTH = (int)(SCREEN_WIDTH * 0.95f);
	}
	
	public static int[] listToArray(ArrayList<Integer> list) {
		int res[] = new int[list.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = list.get(i);
		return res;
	}
	
	public static int getItemAbsoluteId(boolean classes[], int primary, boolean secondary[]) {
		boolean flag = true;
		int k = 0;
		for (int i = 0; i < secondary.length; i++) 
			if (flag && secondary[i]) {
				flag = false; // first occurence of true
				k = i;
			}
			else
				if (!flag && secondary[i])
				return getOffsettedIndex(item_amount.length - 1, item_amount[item_amount.length - 1]) + primary + 1; // second occurence. special case
		return getOffsettedIndex(primary, k);
	}
	
	public static void fixProperties(boolean classes[], int primary, boolean secondary[], int property_type[], int property_value[]) {
		cur_property_index = getItemAbsoluteId(classes, primary, secondary);
		cur_property = new int[property[cur_property_index].length];
		for (int i = 0; i < property[cur_property_index].length; i++)
			cur_property[i] = property[cur_property_index][i];
		for (int i = 0; i < property_type.length; i++)
			if (property_type[i] != -10) {
				boolean flag = false;
				for (int j = 0; j < cur_property.length; j++)
					if (cur_property[j] == property_type[i]) {
						flag = true;
						break;
					}
				if (!flag) {
					for (int j = i; j < property_type.length - 1; j++) {
						property_type[j] = property_type[j + 1];
						property_value[j] = property_value[j + 1];
					}
					property_type[property_type.length - 1] = -10;
				} else
					forbidProperty(property_type[i]);
			}
	}
	
	public static void forbidProperty(int property_id){
		for (int i = 0; i < cur_property.length; i++) 
			if (cur_property[i] == property_id) {
				int offset = 1; 
				if (i < cur_property.length - 1 && cur_property[i + 1] < 0)
					offset++;
				for (int j = i; j < cur_property.length - offset; j++)
					cur_property[j] = cur_property[j + offset];
				break;
			}
	}
	
	public static void releaseProperty(int property_id){
		boolean isGroupForbidden = true;
		for (int i = 0; i < cur_property.length; i++)
			if (cur_property[i] == -(property_group[property_id] + 1)) {
				isGroupForbidden = false;
				break;
			}
		int temp[] = new int[cur_property.length + (isGroupForbidden ? 2 : 1)];
		int count = 0;
		int j = 0; // pointer in cur_property array
		int i = 0;
		while (i < property[cur_property_index].length) {
			if (property[cur_property_index][i] == cur_property[j]) 
				temp[count++] = property[cur_property_index][i];
			else 
				if (property[cur_property_index][i] == property_id || property[cur_property_index][i] == -(property_group[property_id] + 1)) {
					temp[count++] = property[cur_property_index][i];
					j--;
				}
			i++;		 
			j++;
		}
		cur_property = temp;
	}
	
	public static int offset;
	public static int disabled_n;
		
	public static String getString(int position) {
		return cur_property[position] > -1 ? property_name[cur_property[position]] : property_delimiter[-(cur_property[position] + 1)];
	}
	
	public static boolean isDelimiter(int position) {
		return cur_property[position] < 0;
	}
	
	public static int getPropertyId(int position) {
		return cur_property[position];
	}
	
	public static BitmapDrawable getBack(double ratio) {
		Bitmap b = Bitmap.createBitmap(SCREEN_WIDTH, 200, Config.ARGB_8888);
	    Canvas c = new Canvas(b);
	    int color = Color.argb(200, (int) (255 * (1 - ratio)), (int) (255 * ratio), 0);
	    int grad_color = Color.argb(200, (int) (255 * (1 - ratio)), (int) (255 * ratio), 100);
	    c.drawColor(color);

	    /* Create your gradient. */
	    LinearGradient grad = new LinearGradient(0, 0, 0, 100, color, grad_color, TileMode.CLAMP);

	    /* Draw your gradient to the top of your bitmap. */
	    Paint p = new Paint();
	    p.setStyle(Style.FILL);
	    p.setShader(grad);
	    c.drawRect(0, 0, (int)(SCREEN_WIDTH * ratio), 100, p);
	    p = new Paint();
	    p.setStyle(Style.FILL);
	    p.setAlpha(255);
	    c.drawRect((int)(SCREEN_WIDTH * ratio), 0, SCREEN_WIDTH, 200, p);
	    return new BitmapDrawable(b);
	}
	
	public static boolean getBit(int number, int pos){
		return (number >> pos & 1) == 1;
	}
	
	public static String getContent(String str[], boolean checked[]) {
		String res = "";
		for (int i = 0; i < str.length; i++)
			if (checked[i])
				res += str[i] + ", " ;
		return (res.length() > 0) ? res.substring(0, res.length() - 2) : str[0];
	}
	
	public static boolean[] getPossibleItemsId(boolean classes[], int primary){
		int len = 0;
		boolean result[] = new boolean[item_amount[primary]];
		for (int i = 0; i < item_amount[primary]; i++){
			boolean flag = true;
			for (int j = 0; j < 5; j++)
				if (classes[j] && can_equip[j][primary][i] == 0) {
					flag = false;
					break;
				}
			result[i] = flag;
		}
		return result;
	}
	
	public static String[] getPossibleItems(boolean classes[], int primary, String[] choice){
		boolean id[] = getPossibleItemsId(classes, primary);
		int count = 0;
		for (int i = 0; i < id.length; i++)
			if (id[i])
				count++;
		String result[] = new String[count];
		count = 0;
		for (int i = 0; i < id.length; i++)
			if (id[i])
				result[count++] = choice[i];
		return result;
	}
	
	public static int getMainAttr(int primary, boolean checked[]){
		if (primary < 2)
			return 1000;
		if (primary == 3)
			return 0;
		return 100;
	}
	
	public static int GetDipsFromPixel(float pixels, float density) {
		return (int) (pixels * density + 0.5f);
	} 
	
	
	public static String[] getPropertyStringArray(int property[], int property_number) {
		int i = 0; 
		String res[] = new String[property_number];
		int j = 0;
		String temp;
		boolean flag;
		int change_index;
		while (i < property.length) { 
			temp = property_result_string[property[i++]];
			while (true) {
				change_index = temp.indexOf('$');
				if (change_index == -1)
					break;
				temp = temp.substring(0, change_index) + property[i++] + temp.substring(change_index + 1, temp.length());
			}
			res[j++] = temp;
		}
		return res;
	}
	
	public static Order c_order;
	
	public static String getPropertyString(int type, int value){
		int change_index = property_string[type].indexOf('$');
		return property_string[type].substring(0, change_index) + value + property_string[type].substring(change_index + 1, property_string[type].length());
	}
	
	public static int getType_1(int offseted_index) {
		int temp = 0;
		for (int i = 0; i < item_amount.length; i++) {
			temp += item_amount[i];
			if (offseted_index - temp < 0)
				return i;
		}
		return -1;
	}
	
	public static int getType_2(int offseted_index) {
		int temp = 0;
		for (int i = 0; i < item_amount.length; i++) {
			if (offseted_index - temp - item_amount[i] < 0)
				return offseted_index - temp;
			temp += item_amount[i];
		}
		return -1;
	}
}
