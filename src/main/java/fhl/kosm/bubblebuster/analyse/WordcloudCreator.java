package fhl.kosm.bubblebuster.analyse;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import fhl.kosm.bubblebuster.FileUtil;
import fhl.kosm.bubblebuster.model.Hashtag;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WordcloudCreator {

//    private final ColorPalette COLOR_PALETTE = new ColorPalette(new Color( 	0x0084b4), new Color(0x00aced), new Color(0x1dcaff), new Color(0xc0deed));

    private final ColorPalette COLOR_PALETTE = new ColorPalette(new Color(0x1DA1F2));

    private final int WIDTH = 944;

    private final int HEIGHT = 768;

    private final int MIN_BIRD_WORDCCLOUD_WORDS = 70;

    public WordCloud create(Hashtag hashtag) {
        if (!canCreate(hashtag)) {
            return null;
        }
        List<WordFrequency> wordFrequencies = new ArrayList<>(hashtag.relationsInverted().size());

        final WordCloud wordCloud = new WordCloud(dimension(hashtag), CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(backgroundFor(hashtag));
        wordCloud.setColorPalette(COLOR_PALETTE);
        wordCloud.setFontScalar(new LinearFontScalar(20, 35));
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(140);
        wordCloud.setBackgroundColor(new Color(0, 0, 0, 0));
        wordCloud.build(frequencyAnalyzer.loadWordFrequencies(createWordFrequencies(hashtag)));
        return wordCloud;
    }

    private Dimension dimension(Hashtag hashtag) {
        if (hashtag.relatedHashtags().size()> MIN_BIRD_WORDCCLOUD_WORDS) {
            return  new Dimension(WIDTH, HEIGHT);
        }
        int size = circleRadius(hashtag.relatedHashtags().size()) * 2;
        return new Dimension(size, size);
    }

    private int circleRadius(int hashtagCount) {
        return hashtagCount < 15 ? 250 : hashtagCount < 30 ? 320 : 450;
    }

    private Background backgroundFor(Hashtag hashtag) {
        Background background = null;
        int size = hashtag.relatedHashtags().size();
        if (size > MIN_BIRD_WORDCCLOUD_WORDS) {
            try {
                background = new PixelBoundryBackground(FileUtil.fileInCurrentDirectory("src\\main\\resources\\Twitter_Bird.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (background == null) {
            background = new CircleBackground(circleRadius(size));
        }
        return background;
    }

    private List<WordFrequency> createWordFrequencies(Hashtag hashtag) {
        List<WordFrequency> frequencies = new ArrayList<>(hashtag.relationsInverted().size());
        hashtag.relationsInverted().forEach((k, v) -> frequencies.add(new WordFrequency(k, v.intValue())));
        return frequencies;
    }

    public String asBase64(Hashtag hashtag) {
        File file = new File("D:\\dev\\data\\clouds\\inverted\\" + hashtag.getTag());
        if (file.exists()) {
            try {
                return new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createAsBase64(hashtag);
    }

    public String createAsBase64(Hashtag hashtag) {
        return asBase64(create(hashtag));
    }

    public boolean canCreate(Hashtag hashtag) {
        return hashtag.relations().size() >= 7;
    }

    private String asBase64(WordCloud wordCloud) {
        return wordCloud == null ? null : asBase64(wordCloud.getBufferedImage());
    }

    private String asBase64(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
            return "data:image/png;base64," + data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
