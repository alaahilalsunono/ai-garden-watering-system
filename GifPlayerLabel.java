package app;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GifPlayerLabel extends JLabel {

    private final List<ImageIcon> frames = new ArrayList<>();
    private final List<Integer> delays = new ArrayList<>();
    private int index = 0;
    private Timer timer;

    public GifPlayerLabel(URL gifUrl) {
        loadGifFrames(gifUrl);

        if (!frames.isEmpty()) {
            setIcon(frames.get(0));
            playFrame(0);
        }
    }

    private void loadGifFrames(URL gifUrl) {
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
            if (!readers.hasNext()) {
                throw new RuntimeException("No GIF reader found");
            }

            ImageReader reader = readers.next();
            ImageInputStream stream = ImageIO.createImageInputStream(gifUrl.openStream());
            reader.setInput(stream, false);

            int numFrames = reader.getNumImages(true);

            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                frames.add(new ImageIcon(frame));

                IIOMetadata metadata = reader.getImageMetadata(i);
                String metaFormat = metadata.getNativeMetadataFormatName();
                Node root = metadata.getAsTree(metaFormat);

                int delay = extractDelay(root);
                if (delay <= 0) {
                    delay = 100;
                }

                delays.add(delay);
            }

            reader.dispose();
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load GIF frames: " + e.getMessage());
        }
    }

    private int extractDelay(Node root) {
        Node node = root.getFirstChild();

        while (node != null) {
            if ("GraphicControlExtension".equals(node.getNodeName())) {
                NamedNodeMap attrs = node.getAttributes();
                Node delayNode = attrs.getNamedItem("delayTime");

                if (delayNode != null) {
                    int delayCs = Integer.parseInt(delayNode.getNodeValue()); // centiseconds
                    return delayCs * 10; // convert to milliseconds
                }
            }

            int childDelay = searchChildrenForDelay(node);
            if (childDelay != -1) {
                return childDelay;
            }

            node = node.getNextSibling();
        }

        return 100;
    }

    private int searchChildrenForDelay(Node parent) {
        Node child = parent.getFirstChild();

        while (child != null) {
            if ("GraphicControlExtension".equals(child.getNodeName())) {
                NamedNodeMap attrs = child.getAttributes();
                Node delayNode = attrs.getNamedItem("delayTime");

                if (delayNode != null) {
                    int delayCs = Integer.parseInt(delayNode.getNodeValue());
                    return delayCs * 10;
                }
            }

            int result = searchChildrenForDelay(child);
            if (result != -1) {
                return result;
            }

            child = child.getNextSibling();
        }

        return -1;
    }

    private void playFrame(int frameIndex) {
        if (frames.isEmpty()) {
            return;
        }

        setIcon(frames.get(frameIndex));

        int delay = delays.get(frameIndex);

        timer = new Timer(delay, e -> {
            timer.stop();
            index = (index + 1) % frames.size();
            playFrame(index);
        });

        timer.setRepeats(false);
        timer.start();
    }
}