package com.baozun.nebula.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JavaCodeLines {
	private static int fileNum = 0;
	private static int lineNum = 0;

	private static void listNext(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				listNext(files[i]);
			} else {
				try {
					if (files[i].getName().endsWith(".java")
							||files[i].getName().endsWith(".jsp")
							||files[i].getName().endsWith(".js")
							||files[i].getName().endsWith(".xml")
							) {
						fileNum++;
						BufferedReader br = new BufferedReader(new FileReader(
								files[i]));
						while (br.readLine() != null) {
							lineNum++;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		String PROJECT_DIR = "D:/baocun-ws/nebula-repo-5.2.0b/src/main";
		File root = new File(PROJECT_DIR);
		listNext(root);
		
		PROJECT_DIR = "D:/baocun-ws/nebula-pts-5.2.0b-SNAPSHOT/src/main";
		root = new File(PROJECT_DIR);
		listNext(root);
		
		PROJECT_DIR = "D:/baocun-ws/nebula-helix-5.2.0b-SNAPSHOT/src/main";
		root = new File(PROJECT_DIR);
		listNext(root);
		
		System.out.println("Java files number: " + fileNum);
		System.out.println("Java code lines: " + lineNum);
	}

}
