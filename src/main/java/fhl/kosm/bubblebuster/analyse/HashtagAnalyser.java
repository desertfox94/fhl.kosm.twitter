package fhl.kosm.bubblebuster.analyse;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import fhl.kosm.bubblebuster.FileUtil;
import fhl.kosm.bubblebuster.TweetService;
import fhl.kosm.bubblebuster.model.AnalyzationResult;
import fhl.kosm.bubblebuster.model.Hashtag;
import fhl.kosm.bubblebuster.model.TweetRelation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HashtagAnalyser {

	private TweetService service =new TweetService();
	
	public AnalyzationResult relations(String... tags) {
		List<Hashtag> hashtags = new ArrayList<>(tags.length);
		Hashtag hashtag;
		for (String tag : tags) {
			hashtag = service.get(tag);
			System.out.println(hashtag);
			if (hashtag != null) {
				hashtags.add(service.get(tag));
			}
		}
		if (hashtags.isEmpty()) {
			return null;
		}
		AnalyzationResult result = new AnalyzationResult();
		for (Hashtag h1 : hashtags) {
//			try {
//				result.addPathOfHashtagWordcloud(createWordcloud(h1));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			for (Hashtag h2 : hashtags) {
				if (h1 == h2)
					continue;
				result.addRelation(h1, h2);
			}
		}
		return result;
	}

	private String createWordcloud(Hashtag hashtag) throws IOException {
		List<WordFrequency> wordFrequencies = new ArrayList<>(hashtag.relations().size());
		hashtag.relations().entrySet().forEach(e -> wordFrequencies.add(new WordFrequency(e.getKey(), e.getValue().intValue())));

		final Dimension dimension = new Dimension(944, 768);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
		wordCloud.setPadding(2);
			wordCloud.setBackground(new PixelBoundryBackground(FileUtil.fileInCurrentDirectory("src\\main\\resources\\Twitter_Bird.png")));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setFontScalar(new LinearFontScalar(20, 50));
		wordCloud.build(wordFrequencies);
		File file = FileUtil.fileInCurrentDirectory("src\\main\\resources\\static\\clouds\\" + hashtag + ".png");
		wordCloud.writeToFile(file.getPath());
		return "/resources/clouds/" + hashtag + ".png";
	}
	
}
