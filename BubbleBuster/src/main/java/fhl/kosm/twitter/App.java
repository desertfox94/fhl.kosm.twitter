package fhl.kosm.twitter;

import fhl.kosm.twitter.analyse.HashtagAnalyser;

public class App {

	public static void main(String[] args) throws Exception {
		new HashtagAnalyser().relations("elonmusk", "Tesla");
	}

}
