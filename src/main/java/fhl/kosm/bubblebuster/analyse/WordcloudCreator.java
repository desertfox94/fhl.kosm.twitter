package fhl.kosm.bubblebuster.analyse;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WordcloudCreator {

//    private final ColorPalette COLOR_PALETTE = new ColorPalette(new Color( 	0x0084b4), new Color(0x00aced), new Color(0x1dcaff), new Color(0xc0deed));

    private final ColorPalette COLOR_PALETTE = new ColorPalette(new Color( 	0x1DA1F2));

    private final int  WIDTH = 944;

    private final int  HEIGHT = 768;

    public WordCloud create(Hashtag hashtag) {
            List<WordFrequency> wordFrequencies = new ArrayList<>(hashtag.relations().size());
            hashtag.relations().entrySet().forEach(e -> wordFrequencies.add(new WordFrequency(e.getKey(), e.getValue().intValue())));
            final Dimension dimension = new Dimension(WIDTH, HEIGHT);
            final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(2);
            try {
                wordCloud.setBackground(new PixelBoundryBackground(FileUtil.fileInCurrentDirectory("src\\main\\resources\\Twitter_Bird.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            wordCloud.setColorPalette(COLOR_PALETTE);
            wordCloud.setFontScalar(new LinearFontScalar(30, 80));
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(100);
            wordCloud.setBackgroundColor(new Color(0,0,0,0));
            wordCloud.build(frequencyAnalyzer.loadWordFrequencies(createWordFrequencies(hashtag)));
            return wordCloud;
    }

    private List<WordFrequency> createWordFrequencies(Hashtag hashtag) {
        double relationsCount = hashtag.relationsCount();
        List<WordFrequency> frequencies = new ArrayList<>(hashtag.relations().size());
        hashtag.relations().forEach((k, v) ->frequencies.add(new WordFrequency(k, v.intValue())));
        return frequencies;
    }

    public String asBase64(Hashtag hashtag) {
        return asBase64(create(hashtag).getBufferedImage());
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
